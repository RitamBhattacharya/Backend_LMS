package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.entity.Employee;
import com.example.entity.LeaveRequest;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    @Async
    public void sendMailToMultipleAdmins(Iterable<String> toEmails, String subject, String body) {
        for (String toEmail : toEmails) {
            sendMail(toEmail, subject, body);
        }
    }
    
    
    public void sendMail(String toEmail, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("leavemanagement25@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body, true); // 'true' enables HTML content

            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Failed to send mail: " + e.getMessage());
        }
    }
    
    @Async
    public void sendApprovalMailToEmployee(LeaveRequest leave) {
        Employee emp = leave.getEmployee();
        String toEmail = emp.getEmail();
        String subject = "Your Leave Request Has Been Approved";

        String body = "<p>Dear " + emp.getName() + ",</p>"
                + "<p>Your leave request from <strong>" + leave.getStartDate() + "</strong> to <strong>" + leave.getEndDate() + "</strong> has been "
                + "<span style='color:green;'>approved</span>.</p>"
                + "<p><strong>Admin Remarks:</strong> " + (leave.getAdminComments()== null || leave.getAdminComments().isBlank() ? "None" : leave.getAdminComments()) + "</p>"
                + "<p>Regards,<br/>Leave Management System</p>";

        sendMail(toEmail, subject, body);
    }

    @Async
    public void sendRejectionMailToEmployee(LeaveRequest leave) {
        Employee emp = leave.getEmployee();
        String toEmail = emp.getEmail();
        String subject = "Your Leave Request Has Been Rejected";

        String body = "<p>Dear " + emp.getName() + ",</p>"
                + "<p>Your leave request from <strong>" + leave.getStartDate() + "</strong> to <strong>" + leave.getEndDate() + "</strong> has been "
                + "<span style='color:red;'>rejected</span>.</p>"
                + "<p><strong>Admin Remarks:</strong> " + (leave.getAdminComments() == null || leave.getAdminComments().isBlank() ? "None" : leave.getAdminComments()) + "</p>"
                + "<p>Regards,<br/>Leave Management System</p>";

        sendMail(toEmail, subject, body);
    }



   
}
