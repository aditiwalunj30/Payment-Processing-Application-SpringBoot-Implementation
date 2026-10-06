package com.abw.dto;

import java.time.LocalDateTime;

import com.abw.entity.TransactionStatus;

import lombok.Data;
@Data
public class TransferResponseDto {

	private Long transactionId;
    private String fromAccountNumber;
    private String toAccountNumber;
    private Double amount;
    private TransactionStatus status;
    private String remarks;
    private LocalDateTime timestamp;
}
