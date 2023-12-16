package com.binar.backendonlinecourseapp.Service;

import com.binar.backendonlinecourseapp.DTO.Request.CourseCreateRequest;
import com.binar.backendonlinecourseapp.DTO.Response.*;
import com.binar.backendonlinecourseapp.Entity.Enum.ClassType;
import com.binar.backendonlinecourseapp.Entity.Enum.Level;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CourseService {

    ResponseHandling<CourseCreateResponse> createCourse(CourseCreateRequest courseCreateRequest, MultipartFile file) throws IOException;

    ResponseHandling<List<CourseGetResponse>> getCourse(Integer page);

    ResponseHandling<List<CourseGetResponse>> searchCourse(String courseName, Integer page);

    ResponseHandling<GetCourseResponse> hitGetCourse(String courseCode);

    ResponseHandling<List<PaymentHistoryResponse>> getPaymentHistory(Integer page);

//    ResponseHandling<CourseUpdateResponse> updateCourse(CourseUpdateRequest courseUpdateRequest);

    ResponseHandling<List<CourseGetResponse>> getPremiumClass(Integer page);

    ResponseHandling<List<CourseGetResponse>> getFreeClass(Integer page);

    void videoTrigger(String videoCode);

    ResponseHandling<List<UserWatchProgressResponse>> getProgressResponse(Integer page);

    ResponseHandling<List<UserWatchProgressResponse>> getFinishedClass(Integer page);

    ResponseHandling<List<CourseGetResponse>> getPopularClass(String category);

    ResponseHandling<List<CourseGetResponse>> filter(Boolean isNewest, Boolean isPopular, ClassType classType, List<String> category, List<Level> level);
//
//    ResponseHandling<DashboardResponse> dashboard(Integer page);

    ResponseHandling<List<CourseGetResponse>> searchCoursePremium(String courseName, Integer page);

    ResponseHandling<List<CourseGetResponse>> searchCourseFree(String courseName, Integer page);

    ResponseHandling<List<UserWatchProgressResponse>> getProgressAndFinished(Integer page);

    ResponseHandling<List<UserWatchProgressResponse>> searchProgress(String courseName, Integer page);

    ResponseHandling<List<UserWatchProgressResponse>> searchFinished(String courseName, Integer page);

    ResponseHandling<List<UserWatchProgressResponse>> searchProgressAndFinished(String courseName, Integer page);
}
