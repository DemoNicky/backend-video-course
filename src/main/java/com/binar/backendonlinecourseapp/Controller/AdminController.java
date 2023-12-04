package com.binar.backendonlinecourseapp.Controller;

import com.binar.backendonlinecourseapp.DTO.Response.GetClassResponse;
import com.binar.backendonlinecourseapp.DTO.Response.ResponseHandling;
import com.binar.backendonlinecourseapp.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/active-classes")
    public ResponseHandling<List<GetClassResponse>> getActiveClasses() {
        return adminService.getActiveClasses();
    }

    @GetMapping("/premium-classes")
    public ResponseHandling<List<GetClassResponse>> getPremiumClasses() {
        return adminService.getPremiumClasses();
    }
}