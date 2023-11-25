package com.binar.backendonlinecourseapp.Service;

import com.binar.backendonlinecourseapp.DTO.Request.CourseCreateRequest;
import com.binar.backendonlinecourseapp.DTO.Response.CourseCreateResponse;
import com.binar.backendonlinecourseapp.DTO.Response.ResponseHandling;

public interface CourseService {
    ResponseHandling<CourseCreateResponse> createCourse(CourseCreateRequest courseCreateRequest);
}
