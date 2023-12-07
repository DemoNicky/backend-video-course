package com.binar.backendonlinecourseapp.Service;

import com.binar.backendonlinecourseapp.DTO.Request.CourseCreateRequest;
import com.binar.backendonlinecourseapp.DTO.Request.CourseUpdateRequest;
import com.binar.backendonlinecourseapp.DTO.Response.*;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CourseService {

    ResponseHandling<CourseCreateResponse> createCourse(CourseCreateRequest courseCreateRequest);

    ResponseHandling<List<CourseGetResponse>> getCourse(Pageable pageable);

    ResponseHandling<List<CourseGetResponse>> searchCourse(String courseName, Pageable pageable);

    ResponseHandling<GetCourseResponse> hitGetCourse(String courseCode);

    ResponseHandling<List<PaymentHistoryResponse>> getPaymentHistory();

    ResponseHandling<CourseUpdateResponse> updateCourse(CourseUpdateRequest courseUpdateRequest);

    ResponseHandling<List<GetPremiumClassResponse>> getPremiumClass();

    ResponseHandling<List<CourseGetResponse>> getFreeClass();

    String uploadImage(MultipartFile upload, String course) throws IOException;
}
