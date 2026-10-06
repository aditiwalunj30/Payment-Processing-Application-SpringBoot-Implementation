package com.abw.dto;

import lombok.Data;

@Data
public class TransferRequestDto {

	private String fromAccountNumber;
	private String toAccountNumber;
	private Double amount;
}
