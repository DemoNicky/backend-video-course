package com.binar.backendonlinecourseapp.Service;

import com.binar.backendonlinecourseapp.DTO.Response.GetClassResponse;
import com.binar.backendonlinecourseapp.DTO.Response.ResponseHandling;

import java.util.List;

public interface AdminService {
    ResponseHandling<List<GetClassResponse>> getActiveClasses();
    ResponseHandling<List<GetClassResponse>> getPremiumClasses();
}
