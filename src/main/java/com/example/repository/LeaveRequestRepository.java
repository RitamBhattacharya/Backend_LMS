package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.LeaveRequest;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer> {

}
