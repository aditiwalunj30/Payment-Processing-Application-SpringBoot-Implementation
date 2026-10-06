package com.abw.dto;

import com.abw.entity.AccountType;

import lombok.Data;

@Data
public class AccountResponseDto {

	private Long id;
	private String accountNumber;
    private String bankName;
    private AccountType accountType;
    private String accountHolderName;
    private String accountHolderAddress;
    private Double balance;
    private String upiId;
}
