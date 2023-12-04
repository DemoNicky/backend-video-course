package com.binar.backendonlinecourseapp.Controller;

import com.binar.backendonlinecourseapp.DTO.Request.CourseCreateRequest;
import com.binar.backendonlinecourseapp.DTO.Request.CourseUpdateRequest;
import com.binar.backendonlinecourseapp.DTO.Response.*;
import com.binar.backendonlinecourseapp.Service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping(path = "/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<CourseCreateResponse>>createCourse(@RequestBody CourseCreateRequest courseCreateRequest){
        ResponseHandling<CourseCreateResponse> response = courseService.createCourse(courseCreateRequest);
        if (response.getData() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(path = "/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<CourseUpdateResponse>>updateCourse(@RequestBody CourseUpdateRequest courseUpdateRequest){
        ResponseHandling<CourseUpdateResponse> response = courseService.updateCourse(courseUpdateRequest);
        if (response.getData() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }


    @GetMapping(path = "/{page}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<CourseGetResponse>>>getCourse(@PathVariable("page")int page){
        Pageable pageable = PageRequest.of(page, 10);
        ResponseHandling<List<CourseGetResponse>> response =courseService.getCourse(pageable);
        if (response.getData() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(path = "/search/{page}/{course}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<CourseGetResponse>>>getSearchCourse(@PathVariable("course")String courseName, @PathVariable("page")int page){
        Pageable pageable = PageRequest.of(page, 10);
        ResponseHandling<List<CourseGetResponse>> response = courseService.searchCourse(courseName, pageable);
        if (response.getData() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @GetMapping(path = "/get/{course}")
    public ResponseEntity<ResponseHandling<GetCourseResponse>>hitGetCourse(@PathVariable("course")String courseCode){
        ResponseHandling<GetCourseResponse> response = courseService.hitGetCourse(courseCode);
        if (response.getData()==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(
            path = "/payment-history",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<PaymentHistoryResponse>>>getPaymentHistory(){
        ResponseHandling<List<PaymentHistoryResponse>> response = courseService.getPaymentHistory();
        if (response.getData()==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(
            path = "/get-premium",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<GetPremiumClassResponse>>>getFreeClass(){
        ResponseHandling<List<GetPremiumClassResponse>> response = courseService.getPremiumClass();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(
            path = "/get-free",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<CourseGetResponse>>>getPremiumClass(){
        ResponseHandling<List<CourseGetResponse>> response = courseService.getFreeClass();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
