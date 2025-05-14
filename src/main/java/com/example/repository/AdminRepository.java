package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
	Optional<Admin> findByEmailAndPassword(String email, String password);
	
	Optional<Admin> findByRole(String role);


}
