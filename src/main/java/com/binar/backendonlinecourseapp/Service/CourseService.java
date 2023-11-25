package com.binar.backendonlinecourseapp.Service;

import com.binar.backendonlinecourseapp.DTO.Request.CourseCreateRequest;
import com.binar.backendonlinecourseapp.DTO.Response.CourseCreateResponse;
import com.binar.backendonlinecourseapp.DTO.Response.CourseGetResponse;
import com.binar.backendonlinecourseapp.DTO.Response.ResponseHandling;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService {

    ResponseHandling<CourseCreateResponse> createCourse(CourseCreateRequest courseCreateRequest);

    ResponseHandling<List<CourseGetResponse>> getCourse(Pageable pageable);
}
