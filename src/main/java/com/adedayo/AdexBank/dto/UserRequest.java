package com.adedayo.AdexBank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private  String dateOfBirth;
    private String address;
    private String stateOfOrigin;
    private String phoneNumber;
}
