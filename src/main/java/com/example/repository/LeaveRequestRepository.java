package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.entity.Employee;
import com.example.entity.LeaveRequest;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer> {
	long countByStatusIgnoreCase(String status);
	 List<LeaveRequest> findByEmployee(Employee employee);
	 
	 @Query("SELECT lr.leaveType, SUM(DATEDIFF(lr.endDate, lr.startDate) + 1) " +
		       "FROM LeaveRequest lr " +
		       "WHERE lr.employee.id = :employeeId AND lr.status = 'Approved' " +
		       "GROUP BY lr.leaveType")
		List<Object[]> findTakenLeavesGroupedByType(@Param("employeeId") Integer employeeId);




}
