package com.abw.service;

import java.util.ArrayList;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abw.dto.AccountRequestDto;
import com.abw.dto.AccountResponseDto;
import com.abw.entity.Account;
import com.abw.entity.User;
import com.abw.repo.AccountRepository;
import com.abw.repo.UserRepository;


@Service
public class AccountService{
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	
	public AccountResponseDto createAccount(AccountRequestDto requestDto) {
		User user = userRepository.findByUpiId(requestDto.getUpiId())
		        .orElseThrow(() -> new NoSuchElementException("User not found with upiId: " + requestDto.getUpiId()));
		Account account = new Account();
        account.setBankName(requestDto.getBankname());
        account.setAccountType(requestDto.getAccountType());
        account.setAccountNumber(requestDto.getAccountNumber());
        account.setAccountHolderName(requestDto.getAccountHolderName());
        account.setAccountHolderAddress(requestDto.getAccountHolderAddress());
        account.setUser(user);
        account.setBalance(0.0);

        Account savedAccount = accountRepository.save(account);
        return convertToDto(savedAccount);
	}
	public List<AccountResponseDto> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        List<AccountResponseDto> dtoList = new ArrayList<>();
        for (Account account : accounts) {
            dtoList.add(convertToDto(account));
        }
        return dtoList;
    }
	private AccountResponseDto convertToDto(Account account) {
        AccountResponseDto dto = new AccountResponseDto();
        dto.setId(account.getId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setBankName(account.getBankName());
        dto.setAccountType(account.getAccountType());
        dto.setAccountHolderName(account.getAccountHolderName());
        dto.setAccountHolderAddress(account.getAccountHolderAddress());
        dto.setBalance(account.getBalance());
        dto.setUpiId(account.getUser().getUpiId());
        return dto;
    }
	
	
}