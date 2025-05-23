package com.example.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.entity.Admin;
import com.example.entity.Employee;
import com.example.entity.LeaveRequest;
import com.example.service.LeaveManagementService;

import jakarta.annotation.PostConstruct;

import com.example.service.EmailService;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class LeaveManagementController {

    @Autowired
    private LeaveManagementService leaveManagementService;
    @Autowired
    private EmailService emailService;

    // Admin Endpoints

    @GetMapping("/admins")
    public ResponseEntity<List<Admin>> getAdmins() {
        List<Admin> admins = leaveManagementService.getAllAdmins();
        return ResponseEntity.ok(admins);  // 200 OK
    }
    
    @GetMapping("/admins/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable Integer id) {
        return ResponseEntity.ok(leaveManagementService.getAdminById(id));
    }

    @PostMapping("/admins")
    public ResponseEntity<Admin> createAdmin(@RequestBody Admin admin) {
        Admin createdAdmin = leaveManagementService.createAdmin(admin);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAdmin); // 201 Created
    }

    @PutMapping("/admins/{id}")
    public ResponseEntity<Admin> updateAdmin(@PathVariable Integer id, @RequestBody Admin admin) {
        Admin updatedAdmin = leaveManagementService.updateAdmin(id, admin);
        return ResponseEntity.ok(updatedAdmin); // 200 OK
    }

    @DeleteMapping("/admins/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Integer id) {
        leaveManagementService.deleteAdmin(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // Employee Endpoints

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getEmployees() {
        List<Employee> employees = leaveManagementService.getAllEmployees();
        return ResponseEntity.ok(employees); // 200 OK
    }
    
    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Integer id) {
        return ResponseEntity.ok(leaveManagementService.getEmployeeById(id));
    }

    @PostMapping("/employees")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee createdEmployee = leaveManagementService.createEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee); // 201 Created
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Integer id, @RequestBody Employee employee) {
        Employee updatedEmployee = leaveManagementService.updateEmployee(id, employee);
        return ResponseEntity.ok(updatedEmployee); // 200 OK
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Integer id) {
        leaveManagementService.deleteEmployee(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // Leave Request Endpoints

    @GetMapping("/leave-requests")
    public ResponseEntity<List<LeaveRequest>> getLeaveRequests() {
        List<LeaveRequest> leaveRequests = leaveManagementService.getAllLeaveRequests();
        return ResponseEntity.ok(leaveRequests); // 200 OK
    }
    
    @GetMapping("/leave-requests/{id}")
    public ResponseEntity<LeaveRequest> getLeaveRequestById(@PathVariable Integer id) {
        return ResponseEntity.ok(leaveManagementService.getLeaveRequestById(id));
    }

    @PostMapping("/leave-requests")
    public ResponseEntity<LeaveRequest> createLeaveRequest(@RequestBody LeaveRequest leaveRequest) {
        LeaveRequest createdLeaveRequest = leaveManagementService.createLeaveRequest(leaveRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLeaveRequest);
    }
  


    @PutMapping("/leave-requests/{id}")
    public ResponseEntity<LeaveRequest> updateLeaveRequest(@PathVariable Integer id, @RequestBody LeaveRequest leaveRequest) {
        LeaveRequest updatedLeaveRequest = leaveManagementService.updateLeaveRequest(id, leaveRequest);
        return ResponseEntity.ok(updatedLeaveRequest); // 200 OK
    }

    @DeleteMapping("/leave-requests/{id}")
    public ResponseEntity<Void> deleteLeaveRequest(@PathVariable Integer id) {
        leaveManagementService.deleteLeaveRequest(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
    
    
    
    
    // login endpoints
    
    @PostMapping("/login/admin")
    public ResponseEntity<?> adminLogin(@RequestBody Map<String, String> credentials) {
        try {
            Admin admin = leaveManagementService.loginAdmin(
                credentials.get("email"), credentials.get("password")
            );
            return ResponseEntity.ok(admin); // Success
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/login/employee")
    public ResponseEntity<?> employeeLogin(@RequestBody Map<String, String> credentials) {
        try {
            Employee employee = leaveManagementService.loginEmployee(
                credentials.get("email"), credentials.get("password")
            );
            return ResponseEntity.ok(employee); // Success
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    
    
    // dashboard endpoint
    
    @GetMapping("/dashboard/stats")
    public ResponseEntity<Map<String, Long>> getDashboardStats() {
        Map<String, Long> stats = leaveManagementService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }
    
    
    
    // Approve and Reject endpoint
    
    @PutMapping("/leave-requests/{id}/approve")
    public ResponseEntity<Void> approveLeaveRequest(
            @PathVariable Integer id,
            @RequestParam(required = false) String remarks) {
        leaveManagementService.approveLeaveRequest(id, remarks);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/leave-requests/{id}/reject")
    public ResponseEntity<Void> rejectLeaveRequest(
            @PathVariable Integer id,
            @RequestParam(required = false) String remarks) {
        leaveManagementService.rejectLeaveRequest(id, remarks);
        return ResponseEntity.ok().build();
    }
    
    
    // End point for get Approved Leave Request by Id and get Rejected Leave Request by Id
    
    @GetMapping("/leave-requests/approved/{requestId}")
    public ResponseEntity<LeaveRequest> getApprovedLeaveByRequestId(@PathVariable Integer requestId) {
        LeaveRequest leave = leaveManagementService.getApprovedLeaveByRequestId(requestId);
        return ResponseEntity.ok(leave);
    }
    
    
    @GetMapping("/leave-requests/rejected/{requestId}")
    public ResponseEntity<LeaveRequest> getRejectedLeaveByRequestId(@PathVariable Integer requestId) {
        LeaveRequest leave = leaveManagementService.getRejectedLeaveByRequestId(requestId);
        return ResponseEntity.ok(leave);
    }

    
    
    
    // Endpoint for get leave summary
    
    @GetMapping("/employees/{id}/leave-summary")
    public ResponseEntity<Map<String, Map<String, Integer>>> getLeaveSummary(@PathVariable Integer id) {
        Map<String, Map<String, Integer>> summary = leaveManagementService.getLeaveSummaryForEmployee(id);
        return ResponseEntity.ok(summary);
    }
    
    

    @PostMapping("/leave")
    public ResponseEntity<LeaveRequest> submitLeaveRequest(@RequestBody LeaveRequest request) {
        // Save the leave request and ensure Employee is fetched/attached properly
        LeaveRequest createdRequest = leaveManagementService.submitLeaveRequest(request);

        // Get employee name from the saved request
        String employeeName = createdRequest.getEmployee().getName();

        // Create the email body
        String body = "A new leave request from " + employeeName + " needs your attention.";
        String subject = "New Leave Request Submitted";

        // Fetch all admin emails
        List<Admin> admins = leaveManagementService.getAllAdmins();
        List<String> adminEmails = admins.stream()
                .map(Admin::getEmail)
                .toList();

        // Send email to all admins
        emailService.sendMailToMultipleAdmins(adminEmails, subject, body);

        return ResponseEntity.ok(createdRequest);
    }
    
    @GetMapping("/test-email")
    public String testEmail() {
        emailService.sendMail("dansampurna@gmail.com", "Test Subject", "This is a test email.");
        return "Email Sent";
    }




}
