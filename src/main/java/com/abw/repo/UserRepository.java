package com.abw.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abw.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	// in UserRepository.java
	Optional<User> findByUpiId(String upiId);
}
