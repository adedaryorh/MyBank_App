package com.adedayo.AdexBank.controller;

import com.adedayo.AdexBank.dto.*;
import com.adedayo.AdexBank.model.User;
import com.adedayo.AdexBank.service.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/user")
@Tag(name = "User Acct Management API's")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping(path = "{userId}")
    User getAUserById(@PathVariable ("userId") Long id){
        return userService.getUserId();
    }

    @GetMapping(value = "all")
    List<User> getUsers() {
        return userService.getUser();
    }
    @Operation(
            summary = "Create New User Account",
            description = "Creating a new User and assigning an account ID"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 Created"
    )
    @PostMapping
    public BankResponse createAnAccount(@RequestBody UserRequest userRequest){
        return userService.createAccount(userRequest);
    }
    @Operation(
            summary = "Balance Enquiry",
            description = "Given an Account Number, check how much the user has in."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 Success"
    )
    @GetMapping("balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return userService.balEnquiry(enquiryRequest);
    }

    @GetMapping("nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return userService.nameEnquiry(enquiryRequest);
    }
    @PostMapping("addFund")
    public BankResponse creditAccount(@RequestBody CrDrRequest crDrRequest){
        return userService.creditAccount(crDrRequest);
    }
    @PostMapping("removeFund")
    public BankResponse debitAccount(@RequestBody CrDrRequest crDrRequest){
        return userService.debitAccount(crDrRequest);
    }
    @PostMapping("transfer")
    public BankResponse transferFund(@RequestBody TransferRequest transferRequest){
        return userService.transfer(transferRequest);
    }
}
