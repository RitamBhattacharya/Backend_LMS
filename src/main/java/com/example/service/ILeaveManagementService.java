package com.example.service;

import java.util.List;
import java.util.Map;

import com.example.entity.Admin;
import com.example.entity.Employee;
import com.example.entity.LeaveRequest;

public interface ILeaveManagementService {
	// Admin CRUD
    List<Admin> getAllAdmins();
    Admin getAdminById(Integer id);
    Admin createAdmin(Admin admin);
    Admin updateAdmin(Integer id, Admin updatedAdmin);
    void deleteAdmin(Integer id);

    // Employee CRUD
    List<Employee> getAllEmployees();
    Employee getEmployeeById(Integer id);
    Employee createEmployee(Employee employee);
    Employee updateEmployee(Integer id, Employee updatedEmployee);
    void deleteEmployee(Integer id);

    // Leave Request CRUD
    List<LeaveRequest> getAllLeaveRequests();
    LeaveRequest getLeaveRequestById(Integer id);
    LeaveRequest createLeaveRequest(LeaveRequest leaveRequest);
    LeaveRequest updateLeaveRequest(Integer id, LeaveRequest updatedRequest);
    void deleteLeaveRequest(Integer id);
    
    
    Admin loginAdmin(String email, String password);
    Employee loginEmployee(String email, String password);
    
    Map<String, Long> getDashboardStats();
    

    
    void rejectLeaveRequest(Integer requestId, String remarks);
    void approveLeaveRequest(Integer requestId, String remarks);
    
    LeaveRequest getApprovedLeaveByRequestId(Integer requestId);
    
    LeaveRequest getRejectedLeaveByRequestId(Integer requestId);
    

    Map<String, Map<String, Integer>> getLeaveSummaryForEmployee(Integer employeeId);

}
