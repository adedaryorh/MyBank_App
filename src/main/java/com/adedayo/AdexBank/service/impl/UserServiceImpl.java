package com.adedayo.AdexBank.service.impl;

import com.adedayo.AdexBank.dto.*;
import com.adedayo.AdexBank.model.User;
import com.adedayo.AdexBank.repository.UserRepository;
import com.adedayo.AdexBank.utils.AccountUtils;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;


@Service
@Builder
public class UserServiceImpl implements UserService{
    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    TransactionService transactionService;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {

        if(userRepository.existsByEmail(userRequest.getEmail())){
            BankResponse response = BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
            return response;
        }
//if entity is a new user
        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .gender(userRequest.getGender())
                .email(userRequest.getEmail())
                .dateOfBirth(userRequest.getDateOfBirth())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .phoneNumber(userRequest.getPhoneNumber())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .phoneNumber(userRequest.getPhoneNumber())
                .status("ACTIVE")
                .build();

        User saveUser = userRepository.save(newUser);
        //send email alert
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(saveUser.getEmail())
                .message(
                        "ACCOUNT CREATION"
                )
                .messageBody(
                        "Congratulations! Ur acct has been created.\nYour accct details are \n" +
                        "Acct Name: " + saveUser.getFirstName()
                                + " " + saveUser.getLastName() + " "
                                + "\nAcc Number"
                                + saveUser.getAccountNumber()
                        )
                .build();
        emailService.sendEmailAlert(emailDetails);
        BankResponse response = BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(saveUser.getFirstName() + " "+ saveUser.getLastName())
                        .accountBalance(saveUser.getAccountBalance())
                        .accountNumber(saveUser.getAccountNumber())
                        .build())
                .build();
        return response;
    }

    @Override
    public BankResponse balEnquiry(EnquiryRequest enquiryRequest) {
        boolean isAccountExist = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if (!isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        //now if the acct exits
        User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_USER_CODE)
                .responseMessage(AccountUtils.ACCOUNT_USER_FOUND_MSG)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUser.getAccountBalance())
                        .accountNumber(foundUser.getAccountNumber())
                        .accountName(
                                foundUser.getFirstName() + " "
                                        + foundUser.getLastName() + " "
                                        + foundUser.getAddress())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        boolean isAccountExist = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if (!isAccountExist){
           return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
        }
        User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return foundUser.getFirstName()+ " " + foundUser.getLastName()+ " " + foundUser.getEmail();
    }

    @Override
    public BankResponse creditAccount(CrDrRequest crDrRequest) {
        //check if the acct exist
        boolean isAccountExist = userRepository.existsByAccountNumber(crDrRequest.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User userToCredit = userRepository.findByAccountNumber(crDrRequest.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(crDrRequest.getAmount()));
        userRepository.save(userToCredit);

        //save a translation
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .transactionType("CREDIT")
                .amount(crDrRequest.getAmount())
                .build();
        transactionService.saveTransaction(transactionDTO);


         return BankResponse.builder()
                 .responseCode(AccountUtils.ACCOUNT_CREDIT_STATUS_CODE)
                 .responseMessage(AccountUtils.ACCOUNT_CREDIT_STATUS_MSG)
                 .accountInfo(AccountInfo.builder()
                         .accountName(userToCredit.getFirstName()+" "+ userToCredit.getLastName()+" "+userToCredit.getEmail())
                         .accountBalance(userToCredit.getAccountBalance())
                         .accountNumber(userToCredit.getAccountNumber())
                         .build())
                 .build();
    }

    @Override
    public BankResponse debitAccount(CrDrRequest crDrRequest) {
        //check if acc exit
        boolean isAccountExist = userRepository.existsByAccountNumber(crDrRequest.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        //check if withdrawal is less dan current bal
        User userToDebit = userRepository.findByAccountNumber(crDrRequest.getAccountNumber());
        BigInteger availableBal = userToDebit.getAccountBalance().toBigInteger();
        BigInteger debitAmount = crDrRequest.getAmount().toBigInteger();
        if (availableBal.intValue() < debitAmount.intValue() ){
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BAL_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BAL_MSG)
                    .accountInfo(null)
                    .build();
        } else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(crDrRequest.getAmount()));
            userRepository.save(userToDebit);
            //save a translation
            TransactionDTO transactionDTO = TransactionDTO.builder()
                    .accountNumber(userToDebit.getAccountNumber())
                    .transactionType("DEBIT")
                    .amount(crDrRequest.getAmount())
                    .build();
            transactionService.saveTransaction(transactionDTO);
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS)
                    .responseMessage(AccountUtils.ACCOUNT_DEBITED_MSG)
                    .accountInfo(AccountInfo.builder()
                            .accountNumber(crDrRequest.getAccountNumber())
                            .accountName(userToDebit.getFirstName()+" "+userToDebit.getLastName()+" "+userToDebit.getEmail())
                            .accountBalance(userToDebit.getAccountBalance())
                            .build())
                    .build();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BankResponse transfer(TransferRequest transferRequest) {
        //get acct to debit(confirm if it exist)

        User sourceAccountUser = userRepository.findByAccountNumber(transferRequest.getSourceAccountNumber());
        if (transferRequest.getAmount().compareTo(sourceAccountUser.getAccountBalance()) > 0){
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BAL_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BAL_MSG)
                    .accountInfo(null)
                    .build();
        }
        sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(transferRequest.getAmount()));
        String sourceUserName = sourceAccountUser.getFirstName()+" "+sourceAccountUser.getLastName()+" "+sourceAccountUser.getEmail();
        userRepository.save(sourceAccountUser);
        //send transaction alert notification
        EmailDetails debitAlert = EmailDetails.builder()
                .recipient(sourceAccountUser.getEmail())
                .messageBody("The Sum of " + transferRequest.getAmount()+ "was deducted from your acct! and your new bal is "+ sourceAccountUser.getAccountNumber())
                .message("DEBIT ALERT")
                .build();

        emailService.sendEmailAlert(debitAlert);

        User destinationAccountUser = userRepository.findByAccountNumber(transferRequest.getDestinationAccountNumber());
        destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(transferRequest.getAmount()));
        //String recipientUserName = destinationAccountUser.getFirstName() +" "+ destinationAccountUser.getLastName() +" "+ destinationAccountUser.getEmail();

        boolean isDestinationAccountExist = userRepository.existsByAccountNumber(transferRequest.getDestinationAccountNumber());
        if (!isDestinationAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        userRepository.save(destinationAccountUser);
        //send transaction alert notification
        EmailDetails creditAlert = EmailDetails.builder()
                .recipient(sourceAccountUser.getEmail())
                .messageBody("The Sum of " + transferRequest.getAmount()+ "was credited to your acct! an your from "+ sourceUserName+"new bal is "+ destinationAccountUser.getAccountNumber())
                .message("CREDIT ALERT")
                .build();
        emailService.sendEmailAlert(creditAlert);

        //save a translation
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .accountNumber(destinationAccountUser.getAccountNumber())
                .transactionType("TRANSFER")
                .amount(transferRequest.getAmount())
                .build();
        transactionService.saveTransaction(transactionDTO);

        return BankResponse.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESS_CODE)
                .responseMessage(AccountUtils.TRANSFER_SUCCESS_MSG)
                .accountInfo(null)
                .build();

        //check amount to debit is !> Current bal
        //debit the acct
        //get acc to credit
        // it
    }


    @Override
    public List<User> getUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUserId() {
        return null;
    }

    public Optional<User> getUserIdN(Long id) {
        return userRepository.findById(id);

        //Funding<One way>(CR, DB) & <2 way>(Transfer) and Enquiry
    }

}
