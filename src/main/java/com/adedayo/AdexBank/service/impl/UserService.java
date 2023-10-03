package com.adedayo.AdexBank.service.impl;

import com.adedayo.AdexBank.dto.*;
import com.adedayo.AdexBank.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
    BankResponse balEnquiry(EnquiryRequest enquiryRequest);

    String nameEnquiry(EnquiryRequest enquiryRequest);
    BankResponse creditAccount(CrDrRequest crDrRequest);
    BankResponse debitAccount(CrDrRequest crDrRequest);
    BankResponse transfer(TransferRequest transferRequest);

    List<User> getUser();
    User getUserId();

}
