package com.abw.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abw.dto.UserRequestDto;
import com.abw.dto.UserResponseDto;
import com.abw.entity.User;
import com.abw.repo.UserRepository;

@Service
public class UserService {

	
	@Autowired
	private UserRepository userRepository;
	
	public UserResponseDto createUser(UserRequestDto requestDto) {
		User user = new User();
		user.setUser_name(requestDto.getUserName());
		user.setEmail_id(requestDto.getEmailId());
		user.setMobilenumber(requestDto.getMobileNumber());
		user.setUpiId(java.util.UUID.randomUUID().toString());
		
		User savedUser = userRepository.save(user);
		
		return convertToDto(savedUser);
	}
	
	public List<UserResponseDto> getAllUsers(){
		List<User> users = userRepository.findAll();
		
		List<UserResponseDto> dtoList = new ArrayList<>();
		
		for(User user: users) {
			dtoList.add(convertToDto(user));
		}
		return dtoList;
	}
	
	private UserResponseDto convertToDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setUpiId(user.getUpiId());
        dto.setUserName(user.getUser_name());
        dto.setEmailId(user.getEmail_id());
        dto.setMobileNumber(user.getMobilenumber());
        return dto;
    }
}
