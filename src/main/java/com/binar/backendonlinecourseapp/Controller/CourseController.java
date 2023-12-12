package com.binar.backendonlinecourseapp.Controller;

import com.binar.backendonlinecourseapp.DTO.Request.CourseCreateRequest;
import com.binar.backendonlinecourseapp.DTO.Request.CourseFilterRequest;
import com.binar.backendonlinecourseapp.DTO.Request.CourseUpdateRequest;
import com.binar.backendonlinecourseapp.DTO.Response.*;
import com.binar.backendonlinecourseapp.Entity.Enum.Level;
import com.binar.backendonlinecourseapp.Service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping(
            path = "/create",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<CourseCreateResponse>>createCourse(@RequestPart("file") MultipartFile file,
                                                                              @RequestPart("course") CourseCreateRequest courseCreateRequest) throws IOException {
        ResponseHandling<CourseCreateResponse> response = courseService.createCourse(courseCreateRequest, file);
        if (response.getData() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(
            path = "/filter",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<CourseGetResponse>>>filter(@RequestParam Boolean isNewest,
                                                                           @RequestParam Boolean isPopular,
                                                                           @RequestParam List<String> category,
                                                                           @RequestParam List<Level> level) throws IOException {

        ResponseHandling<List<CourseGetResponse>> response = courseService.filter(isNewest, isPopular, category, level);
        if (response.getData()==null || response.getErrors() == true){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @PostMapping(
            path = "/watched/{video}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String>videoWatchTrigger(@PathVariable("video")String videoCode){
        courseService.videoTrigger(videoCode);
        return ResponseEntity.status(HttpStatus.OK).body("sukses");
    }

//    @PutMapping(path = "/update",
//            consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    public ResponseEntity<ResponseHandling<CourseUpdateResponse>>updateCourse(@RequestBody CourseUpdateRequest courseUpdateRequest){
//        ResponseHandling<CourseUpdateResponse> response = courseService.updateCourse(courseUpdateRequest);
//        if (response.getData() == null){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(response);
//
//    }

    @GetMapping(path = "/get-course",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<CourseGetResponse>>>getCourse(){
        ResponseHandling<List<CourseGetResponse>> response =courseService.getCourse();
        if (response.getData() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(path = "/search/{course}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<CourseGetResponse>>>getSearchCourse(@PathVariable("course")String courseName){
        ResponseHandling<List<CourseGetResponse>> response = courseService.searchCourse(courseName);
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
    public ResponseEntity<ResponseHandling<List<CourseGetResponse>>>getPremiumClass(){
        ResponseHandling<List<CourseGetResponse>> response = courseService.getPremiumClass();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(
            path = "/get-free",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<CourseGetResponse>>>getFreelass(){
        ResponseHandling<List<CourseGetResponse>> response = courseService.getFreeClass();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(
            path = "/get/get-in-progress",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<UserWatchProgressResponse>>>getProgressResponse(){
        ResponseHandling<List<UserWatchProgressResponse>> response = courseService.getProgressResponse();
        if (response.getData() == null){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(
            path = "/get/get-finished",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<UserWatchProgressResponse>>>getFinishedClass(){
        ResponseHandling<List<UserWatchProgressResponse>> response = courseService.getFinishedClass();
        if (response.getData() == null){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(
            path = "/popular-course",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<CourseGetResponse>>>getPopularClass(@RequestParam String category){
        ResponseHandling<List<CourseGetResponse>> response = courseService.getPopularClass(category);
        if (response.getData() == null){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
