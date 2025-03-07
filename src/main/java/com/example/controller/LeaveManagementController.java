// Controller Layer
package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Admin;
import com.example.entity.Employee;
import com.example.entity.LeaveRequest;
import com.example.service.LeaveManagementService;

@RestController
@RequestMapping("/api")
public class LeaveManagementController {

    @Autowired
    private LeaveManagementService leaveManagementService;
    
    
    // Admin Endpoints
    
    @GetMapping("/admins")
    public List<Admin> getAdmins() {
        return leaveManagementService.getAllAdmins();
    }
    
    @PostMapping("/admins")
    public Admin createAdmin(@RequestBody Admin admin) {
        return leaveManagementService.createAdmin(admin);
    }

    @PutMapping("/admins/{id}")
    public Admin updateAdmin(@PathVariable Integer id, @RequestBody Admin admin) {
        return leaveManagementService.updateAdmin(id, admin);
    }

    @DeleteMapping("/admins/{id}")
    public void deleteAdmin(@PathVariable Integer id) {
        leaveManagementService.deleteAdmin(id);
    }
    
    
    // Employee Endpoints

    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        return leaveManagementService.getAllEmployees();
    }
    
    @PostMapping("/employees")
    public Employee createEmployee(@RequestBody Employee employee) {
        return leaveManagementService.createEmployee(employee);
    }

    @PutMapping("/employees/{id}")
    public Employee updateEmployee(@PathVariable Integer id, @RequestBody Employee employee) {
        return leaveManagementService.updateEmployee(id, employee);
    }

    @DeleteMapping("/employees/{id}")
    public void deleteEmployee(@PathVariable Integer id) {
        leaveManagementService.deleteEmployee(id);
    }
    
    
    // Leave Request Endpoints

    @GetMapping("/leave-requests")
    public List<LeaveRequest> getLeaveRequests() {
        return leaveManagementService.getAllLeaveRequests();
    }
    
    @PostMapping("/leave-requests")
    public LeaveRequest createLeaveRequest(@RequestBody LeaveRequest leaveRequest) {
        return leaveManagementService.createLeaveRequest(leaveRequest);
    }

    @PutMapping("/leave-requests/{id}")
    public LeaveRequest updateLeaveRequest(@PathVariable Integer id, @RequestBody LeaveRequest leaveRequest) {
        return leaveManagementService.updateLeaveRequest(id, leaveRequest);
    }

    @DeleteMapping("/leave-requests/{id}")
    public void deleteLeaveRequest(@PathVariable Integer id) {
        leaveManagementService.deleteLeaveRequest(id);
    }
}