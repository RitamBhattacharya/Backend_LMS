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
	
	@Autowired
	private EmailService emailService;

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
	public void approveLeaveRequest(Integer requestId, String remarks) {
	    LeaveRequest leave = leaveRequestRepository.findById(requestId)
	        .orElseThrow(() -> new RuntimeException("Leave not found"));

	    leave.setStatus("Approved");
	    leave.setAdminComments(remarks);
	    leaveRequestRepository.save(leave);

	    // ✅ Refetch to ensure data is updated in memory before sending email
	    LeaveRequest updatedLeave = leaveRequestRepository.findById(requestId)
	        .orElseThrow(() -> new RuntimeException("Leave not found after saving"));

	    emailService.sendApprovalMailToEmployee(updatedLeave);
	}




	
	@Override
	public void rejectLeaveRequest(Integer requestId, String remarks) {
	    LeaveRequest leave = leaveRequestRepository.findById(requestId)
	        .orElseThrow(() -> new RuntimeException("Leave not found"));

	    leave.setStatus("Rejected");
	    leave.setAdminComments(remarks);
	    leaveRequestRepository.save(leave);

	    LeaveRequest updatedLeave = leaveRequestRepository.findById(requestId)
	        .orElseThrow(() -> new RuntimeException("Leave not found after saving"));

	    emailService.sendRejectionMailToEmployee(updatedLeave);
	}


	
	
	@Override
	public LeaveRequest getApprovedLeaveByRequestId(Integer requestId) {
	    LeaveRequest leave = leaveRequestRepository.findById(requestId)
	        .orElseThrow(() -> new RuntimeException("Leave Request not found"));

	    if (!"Approved".equalsIgnoreCase(leave.getStatus())) {
	        throw new RuntimeException("Leave Request is not approved");
	    }

	    return leave;
	}

	
	@Override
	public LeaveRequest getRejectedLeaveByRequestId(Integer requestId) {
	    LeaveRequest leave = leaveRequestRepository.findById(requestId)
	        .orElseThrow(() -> new RuntimeException("Leave Request not found"));

	    if (!"Rejected".equalsIgnoreCase(leave.getStatus())) {
	        throw new RuntimeException("Leave Request is not rejected");
	    }

	    return leave;
	}
	
	
	
	@Override
	public Map<String, Map<String, Integer>> getLeaveSummaryForEmployee(Integer employeeId) {
	    // Updated total leaves
	    Map<String, Integer> totalLeaves = new HashMap<>();
	    totalLeaves.put("CL", 15);
	    totalLeaves.put("SL", 9);
	    totalLeaves.put("EL", 15); // set EL to 15 instead of 10

	    // Fetch taken leave from DB
	    List<Object[]> results = leaveRequestRepository.findTakenLeavesGroupedByType(employeeId);
	    Map<String, Integer> takenLeaves = new HashMap<>();
	    for (Object[] row : results) {
	        String type = ((String) row[0]).toUpperCase();
	        int days = ((Number) row[1]).intValue();
	        if (type.equals("CASUAL")) type = "CL";
	        else if (type.equals("SICK")) type = "SL";
	        else if (type.equals("EMERGENCY")) type = "EL";
	        takenLeaves.put(type, days);
	    }

	    // Set 0 if a type is missing
	    for (String type : totalLeaves.keySet()) {
	        takenLeaves.putIfAbsent(type, 0);
	    }

	    // Calculate remaining
	    Map<String, Integer> remainingLeaves = new HashMap<>();
	    for (String type : totalLeaves.keySet()) {
	        int taken = takenLeaves.get(type);
	        int total = totalLeaves.get(type);
	        remainingLeaves.put(type, total - taken);
	    }

	    Map<String, Map<String, Integer>> result = new HashMap<>();
	    result.put("Total", totalLeaves);
	    result.put("Taken", takenLeaves);
	    result.put("Remaining", remainingLeaves);

	    return result;
	}
	
	@Override
	public LeaveRequest submitLeaveRequest(LeaveRequest request) {
	    Integer employeeId = request.getEmployee().getEmployeeID();
	    Employee employee = employeeRepository.findById(employeeId)
	            .orElseThrow(() -> new RuntimeException("Employee not found"));

	    request.setEmployee(employee);
	    LeaveRequest savedRequest = leaveRequestRepository.save(request);

	    List<Admin> admins = adminRepository.findAll();
	    List<String> adminEmails = admins.stream()
	            .map(Admin::getEmail)
	            .distinct()
	            .toList();

	    String subject = "New Leave Request Submitted";
	    String frontendUrl = "http://localhost:3000/pending"; // update as needed
	    String body = "<p>A new leave request from <strong>" + employee.getName() + "</strong> needs your attention.</p>"
	            + "<p><a href='http://localhost:3000/pending' style='text-decoration:none; color:#1a73e8;'>Click here to view</a></p>";


	    emailService.sendMailToMultipleAdmins(adminEmails, subject, body);

	    return savedRequest;
	}



	
//	private String mapToShortForm(String type) {
//	    return switch (type.toUpperCase()) {
//	        case "SICK" -> "SL";
//	        case "CASUAL" -> "CL";
//	        case "EMERGENCY" -> "EL";
//	        default -> type.toUpperCase();
//	    };
//	}


}