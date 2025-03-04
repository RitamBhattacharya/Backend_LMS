// Controller Layer
package com.example.controller;

import com.example.entity.Admin;
import com.example.entity.Employee;
import com.example.entity.LeaveRequest;
import com.example.service.LeaveManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LeaveManagementController {

    @Autowired
    private LeaveManagementService leaveManagementService;

    @GetMapping("/admins")
    public List<Admin> getAdmins() {
        return leaveManagementService.getAllAdmins();
    }

    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        return leaveManagementService.getAllEmployees();
    }

    @GetMapping("/leave-requests")
    public List<LeaveRequest> getLeaveRequests() {
        return leaveManagementService.getAllLeaveRequests();
    }
}