package com.binar.backendonlinecourseapp.Service.Impl;

import com.binar.backendonlinecourseapp.DTO.Response.GetClassResponse;
import com.binar.backendonlinecourseapp.DTO.Response.ResponseHandling;
import com.binar.backendonlinecourseapp.Entity.Admin;
import com.binar.backendonlinecourseapp.Repository.AdminRepository;
import com.binar.backendonlinecourseapp.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public ResponseHandling<List<GetClassResponse>> getActiveClasses() {
        try {
            List<Admin> activeClasses = adminRepository.findByIsActiveTrue();
            List<GetClassResponse> response = activeClasses.stream()
                    .map(this::mapToGetClassResponse)
                    .collect(Collectors.toList());

            return ResponseHandling.<List<GetClassResponse>>builder()
                    .data(response)
                    .message("Successfully retrieved active classes.")
                    .errors(false)
                    .build();
        } catch (Exception e) {
            return ResponseHandling.<List<GetClassResponse>>builder()
                    .data(null)
                    .message("Failed to retrieve active classes.")
                    .errors(true)
                    .build();
        }
    }

    @Override
    public ResponseHandling<List<GetClassResponse>> getPremiumClasses() {
        try {
            List<Admin> premiumClasses = adminRepository.findByClassTypeAndIsActiveTrue(ClassType.PREMIUM);
            List<GetClassResponse> response = premiumClasses.stream()
                    .map(this::mapToGetClassResponse)
                    .collect(Collectors.toList());

            return ResponseHandling.<List<GetClassResponse>>builder()
                    .data(response)
                    .message("Successfully retrieved premium classes.")
                    .errors(false)
                    .build();
        } catch (Exception e) {
            return ResponseHandling.<List<GetClassResponse>>builder()
                    .data(null)
                    .message("Failed to retrieve premium classes.")
                    .errors(true)
                    .build();
        }
    }

    private GetClassResponse mapToGetClassResponse(Admin admin) {
        return GetClassResponse.builder()
                .className(admin.getClassName())
                .isActive(admin.isActive())
                .classType(admin.getClassType())
                .build();
    }
}