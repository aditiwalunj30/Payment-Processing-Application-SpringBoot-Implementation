package com.abw.dto;

import com.abw.entity.AccountType;

import lombok.Data;

@Data
public class AccountRequestDto {
	
	private String accountNumber;
	private String bankname;
	private AccountType accountType;
	private String accountHolderName;
	private String accountHolderAddress;
	private String upiId;

}
