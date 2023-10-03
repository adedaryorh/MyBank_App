package com.adedayo.AdexBank.utils;

import jakarta.servlet.http.PushBuilder;

import java.time.Year;

public class AccountUtils {
    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_CREATION_CODE = "002";
    public static final String ACCOUNT_CREATION_MESSAGE = "Account created successfully";
    public static final String ACCOUNT_NOT_EXIST_CODE = "003";
    public static final String ACCOUNT_NOT_EXIST_MESSAGE = "User with this acct number is not found";
    public static final String ACCOUNT_USER_CODE = "004";
    public static final String ACCOUNT_USER_FOUND_MSG = "This user exist in the system";
    public static final String ACCOUNT_EXISTS_MESSAGE = "This user already has an account with this mail";

    public static final String ACCOUNT_CREDIT_STATUS_CODE = "005";
    public static final String ACCOUNT_CREDIT_STATUS_MSG = "User Bal updated successfully";

    public static final String INSUFFICIENT_BAL_CODE = "006";
    public static final String INSUFFICIENT_BAL_MSG = "Insufficient bal";

    public static final String ACCOUNT_DEBITED_SUCCESS = "007";
    public static final String ACCOUNT_DEBITED_MSG = "Account debited successfully";
    public static final String  TRANSFER_SUCCESS_CODE = "008";
    public static final String  TRANSFER_SUCCESS_MSG = "Transfer Successfully";



    public static void main(String[] args) {
         generateAccountNumber();
    }

    public static String generateAccountNumber() {
        //Using Current year and G digits format btw 100000 and 999999
        Year currentYear = Year.now();
        System.out.println(currentYear);

        int min = 100000;
        int max = 999999;
        // Generate a random number between min and max
        int randNumber = (int) (Math.random() * (max - min + 1) + min);
        // Concatenate them to String
        String year = String.valueOf(currentYear);
        String randomNumber = String.valueOf(randNumber);
        // Ensure the random number has 6 digits by padding with zeros if needed
        randomNumber = String.format("%06d", randNumber);
        return year + randomNumber;
    }

}
