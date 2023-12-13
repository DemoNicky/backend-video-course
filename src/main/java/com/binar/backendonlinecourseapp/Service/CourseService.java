package com.binar.backendonlinecourseapp.Service;

import com.binar.backendonlinecourseapp.DTO.Request.CourseCreateRequest;
import com.binar.backendonlinecourseapp.DTO.Response.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService {

    ResponseHandling<CourseCreateResponse> createCourse(CourseCreateRequest courseCreateRequest);

    ResponseHandling<List<CourseGetResponse>> getCourse(Pageable pageable);

    ResponseHandling<List<CourseGetResponse>> searchCourse(String courseName, Pageable pageable);

    ResponseHandling<GetCourseResponse> hitGetCourse(String courseCode);

    ResponseHandling<List<GetClassResponse>> getActiveClass();

}
