// Service Layer
package com.example.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Admin;
import com.example.entity.Employee;
import com.example.entity.LeaveRequest;
import com.example.repository.AdminRepository;
import com.example.repository.EmployeeRepository;
import com.example.repository.LeaveRequestRepository;

@Service
public class LeaveManagementService implements ILeaveManagementService{

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private LeaveRequestRepository leaveRequestRepository;

	// Admin CRUD
	
	@Override
	public List<Admin> getAllAdmins() {
		return adminRepository.findAll();
	}
	
	@Override
    public Admin getAdminById(Integer id) {
        return adminRepository.findById(id).orElseThrow(() -> new RuntimeException("Admin not found"));
    }
	
	@Override
	public Admin createAdmin(Admin admin) {
		return adminRepository.save(admin);
	}
	
	@Override
	public Admin updateAdmin(Integer id, Admin updatedAdmin) {
	    return adminRepository.findById(id).map(admin -> {
	        BeanUtils.copyProperties(updatedAdmin, admin, "adminID");
	        return adminRepository.save(admin);
	    }).orElseThrow(() -> new RuntimeException("Admin not found"));
	}
	
	@Override
	public void deleteAdmin(Integer id) {
		adminRepository.deleteById(id);
	}

	
	// Employee CRUD
	
	@Override
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}
	
	@Override
    public Employee getEmployeeById(Integer id) {
        return employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
    }
	
	@Override
	public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }
	
	@Override
	public Employee updateEmployee(Integer id, Employee updatedEmployee) {
	    return employeeRepository.findById(id).map(employee -> {
	        BeanUtils.copyProperties(updatedEmployee, employee, "employeeID", "leaveRequests");
	        return employeeRepository.save(employee);
	    }).orElseThrow(() -> new RuntimeException("Employee not found"));
	}

	@Override
    public void deleteEmployee(Integer id) {
        employeeRepository.deleteById(id);
    }
    
    
    
    // Leave Request CRUD
	
	@Override
	public List<LeaveRequest> getAllLeaveRequests() {
		return leaveRequestRepository.findAll();
	}
	
	@Override
    public LeaveRequest getLeaveRequestById(Integer id) {
        return leaveRequestRepository.findById(id).orElseThrow(() -> new RuntimeException("Leave Request not found"));
    }
	

	@Override
	public LeaveRequest createLeaveRequest(LeaveRequest leaveRequest) {
        return leaveRequestRepository.save(leaveRequest);
    }



	@Override
	public LeaveRequest updateLeaveRequest(Integer id, LeaveRequest updatedRequest) {
	    return leaveRequestRepository.findById(id).map(request -> {
	        BeanUtils.copyProperties(updatedRequest, request, "requestID", "employee", "reviewedByAdmin");
	        return leaveRequestRepository.save(request);
	    }).orElseThrow(() -> new RuntimeException("Leave Request not found"));
	}

	@Override
    public void deleteLeaveRequest(Integer id) {
        leaveRequestRepository.deleteById(id);
    }
	
	
	
	
	@Override
	public Admin loginAdmin(String email, String password) {
	    return adminRepository.findByEmailAndPassword(email, password)
	            .orElseThrow(() -> new RuntimeException("Invalid admin credentials"));
	}

	@Override
	public Employee loginEmployee(String email, String password) {
	    return employeeRepository.findByEmailAndPassword(email, password)
	            .orElseThrow(() -> new RuntimeException("Invalid employee credentials"));
	}
	
	
	
	@Override
	public Map<String, Long> getDashboardStats() {
	    long employeeCount = employeeRepository.count();
	    long pendingCount = leaveRequestRepository.countByStatusIgnoreCase("Pending");
	    long approvedCount = leaveRequestRepository.countByStatusIgnoreCase("Approved");
	    long declinedCount = leaveRequestRepository.countByStatusIgnoreCase("Rejected");

	    Map<String, Long> stats = new HashMap<>();
	    stats.put("employees", employeeCount);
	    stats.put("pending", pendingCount);
	    stats.put("approved", approvedCount);
	    stats.put("declined", declinedCount);

	    return stats;
	}
	
	
	@Override
	public LeaveRequest updateStatus(Integer id, String status) {
	    LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Leave Request not found"));

	    // Update the status
	    leaveRequest.setStatus(status);

	    // Save the updated leave request
	    return leaveRequestRepository.save(leaveRequest);
	}


}