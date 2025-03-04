// Service Layer
package com.example.service;

import com.example.entity.Admin;
import com.example.entity.Employee;
import com.example.entity.LeaveRequest;
import com.example.repository.AdminRepository;
import com.example.repository.EmployeeRepository;
import com.example.repository.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveManagementService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestRepository.findAll();
    }
}