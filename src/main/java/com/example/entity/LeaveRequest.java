package com.example.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Data
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer requestID;

    @ManyToOne
    @JoinColumn(name = "employeeID", nullable = false)
    @JsonBackReference
    private Employee employee;

    @Column(nullable = false, length = 50)
    private String leaveType;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date endDate;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(length = 20)
    private String status;

    @Column(columnDefinition = "TEXT")
    private String adminComments;

    @ManyToOne
    @JoinColumn(name = "reviewedByAdminID")
    private Admin reviewedByAdmin;
}
