package com.binar.backendonlinecourseapp.Controller;

import com.binar.backendonlinecourseapp.DTO.Request.CourseCreateRequest;
import com.binar.backendonlinecourseapp.DTO.Request.CourseUpdateRequest;
import com.binar.backendonlinecourseapp.DTO.Response.*;
import com.binar.backendonlinecourseapp.Entity.Enum.CardType;
import com.binar.backendonlinecourseapp.Entity.Enum.ClassType;
import com.binar.backendonlinecourseapp.Entity.Enum.Level;
import com.binar.backendonlinecourseapp.Entity.Enum.ProgressType;
import com.binar.backendonlinecourseapp.Service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping(
            path = "/v2/create-course",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<CourseCreateResponse>>createCourse(@RequestBody CourseCreateRequest courseRequest) throws IOException {
        ResponseHandling<CourseCreateResponse> response = courseService.createCourseNew(courseRequest);
        if (response.getData() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

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

    @GetMapping(
            path = "/get-class-data/{kodekelas}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<GetClassDataResponse>>getClassData(@PathVariable("kodekelas")String kodekelas){
        ResponseHandling<GetClassDataResponse> response = courseService.getClassData(kodekelas);
        if (response.getData() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(
            path = "/search-manage-class",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<ManageClassResponse>>>searchManageClass(@RequestParam("keyword")String keyword,
                                                                                        @RequestParam(required = false) Integer page){
        ResponseHandling<List<ManageClassResponse>> response = courseService.searchManageClass(keyword, page);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(
            path = "/delete-course/{coursecode}"
    )
    public ResponseEntity<ResponseHandling<DeleteCourseResponse>>deleteData(@PathVariable("coursecode")String coursecode){
        ResponseHandling<DeleteCourseResponse> response = courseService.deleteUserData(coursecode);
        if (response.getData() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(
            path = "/v2/update-class-new/{kodekelas}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<UpdateClassResponse>>updateClassDataNew(@PathVariable("kodekelas")String kodekelas,
                                                                                @RequestBody CourseUpdateRequest courseUpdateRequest) throws IOException {
        ResponseHandling<UpdateClassResponse> response = courseService.updateClassDataNew(kodekelas, courseUpdateRequest);
        if (response.getData()==null || response.getErrors() == true){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(
            path = "/update-class/{kodekelas}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<UpdateClassResponse>>updateClassData(@PathVariable("kodekelas")String kodekelas,
                                                                                @RequestPart("file") MultipartFile file,
                                                                                @RequestPart("course") CourseUpdateRequest courseCreateRequest) throws IOException {
        ResponseHandling<UpdateClassResponse> response = courseService.updateClassData(kodekelas, file, courseCreateRequest);
        if (response.getData()==null || response.getErrors() == true){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(
            path = "/filter",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<CourseGetResponse>>>filter(@RequestParam Boolean isNewest,
                                                                           @RequestParam Boolean isPopular,
                                                                           @RequestParam(required = false) ClassType classType,
                                                                           @RequestParam List<String> category,
                                                                           @RequestParam List<Level> level) throws IOException {

        ResponseHandling<List<CourseGetResponse>> response = courseService.filter(isNewest, isPopular, classType, category, level);
        if (response.getData()==null || response.getErrors() == true){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @GetMapping(
            path = "/filter-porgress",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<UserWatchProgressResponse>>>filterProgress(@RequestParam Boolean isNewest,
                                                                                   @RequestParam Boolean isPopular,
                                                                                   @RequestParam ProgressType progressType,
                                                                                   @RequestParam List<String> category,
                                                                                   @RequestParam List<Level> level) throws IOException {

        ResponseHandling<List<UserWatchProgressResponse>> response = courseService.filterProgress(isNewest, isPopular, progressType, category, level);
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
    public ResponseEntity<ResponseHandling<List<CourseGetResponse>>>getCourse(@RequestParam(required = false) Integer page){
        ResponseHandling<List<CourseGetResponse>> response = courseService.getCourse(page);

        if (response.getData() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(path = "/search/{course}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<CourseGetResponse>>>getSearchCourse(@PathVariable("course")String courseName,
                                                                                    @RequestParam(required = false) Integer page){
        ResponseHandling<List<CourseGetResponse>> response = courseService.searchCourse(courseName, page);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @GetMapping(path = "/search-all-course/{course}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<CourseGetResponse>>>getSearchAllCourse(@PathVariable("course")String courseName,
                                                                                    @RequestParam(required = false) Integer page){
        ResponseHandling<List<CourseGetResponse>> response = courseService.getSearchAllCourse(courseName, page);
        if (response.getData() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @GetMapping(path = "/search-premium/{course}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<CourseGetResponse>>>getSearchCoursePremium(@PathVariable("course")String courseName,
                                                                                    @RequestParam(required = false) Integer page){
        ResponseHandling<List<CourseGetResponse>> response = courseService.searchCoursePremium(courseName, page);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(path = "/search-free/{course}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<CourseGetResponse>>>getSearchCourseFree(@PathVariable("course")String courseName,
                                                                                           @RequestParam(required = false) Integer page){
        ResponseHandling<List<CourseGetResponse>> response = courseService.searchCourseFree(courseName, page);
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
    public ResponseEntity<ResponseHandling<List<PaymentHistoryResponse>>>getPaymentHistory(@RequestParam(required = false) Integer page){
        ResponseHandling<List<PaymentHistoryResponse>> response = courseService.getPaymentHistory(page);
        if (response.getData()==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(
            path = "/get-premium",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<CourseGetResponse>>>getPremiumClass(@RequestParam(required = false) Integer page){
        ResponseHandling<List<CourseGetResponse>> response = courseService.getPremiumClass(page);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(
            path = "/get-free",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<CourseGetResponse>>>getFreelass(@RequestParam(required = false) Integer page){
        ResponseHandling<List<CourseGetResponse>> response = courseService.getFreeClass(page);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(
            path = "/search/search-progress/{course}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<UserWatchProgressResponse>>>searchProgress(@PathVariable("course")String courseName,
                                                                                           @RequestParam(required = false) Integer page){
        ResponseHandling<List<UserWatchProgressResponse>> response = courseService.searchProgress(courseName, page);
        if (response.getData() == null){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(
            path = "/search/search-finished/{course}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<UserWatchProgressResponse>>>searchFinished(@PathVariable("course")String courseName,
                                                                                           @RequestParam(required = false) Integer page){
        ResponseHandling<List<UserWatchProgressResponse>> response = courseService.searchFinished(courseName, page);
        if (response.getData() == null){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(
            path = "/search/search-progress-finish/{course}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<UserWatchProgressResponse>>>searchProgressAndFinished(@PathVariable("course")String courseName,
                                                                                           @RequestParam(required = false) Integer page){
        ResponseHandling<List<UserWatchProgressResponse>> response = courseService.searchProgressAndFinished(courseName, page);
        if (response.getData() == null){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(
            path = "/get/get-progress-finish",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<UserWatchProgressResponse>>>getProgressAndFinished(@RequestParam(required = false) Integer page){
        ResponseHandling<List<UserWatchProgressResponse>> response = courseService.getProgressAndFinished(page);
        if (response.getData() == null){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(
            path = "/get/get-in-progress",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<UserWatchProgressResponse>>>getProgressResponse(@RequestParam(required = false) Integer page){
        ResponseHandling<List<UserWatchProgressResponse>> response = courseService.getProgressResponse(page);
        if (response.getData() == null){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(
            path = "/get/get-finished",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<UserWatchProgressResponse>>>getFinishedClass(@RequestParam(required = false) Integer page){
        ResponseHandling<List<UserWatchProgressResponse>> response = courseService.getFinishedClass(page);
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

    @GetMapping(
            path = "/get-payment-status",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<PaymentStatusResponse>>>dashboard(@RequestParam(required = false) Integer page){
        ResponseHandling<List<PaymentStatusResponse>> response = courseService.dashboard(page);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @GetMapping(
            path = "/get-active",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<DashboardResponse>>getActivedashboard(){
        ResponseHandling<DashboardResponse> response = courseService.getActivedashboard();
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @GetMapping(
            path = "/dashboard-filter",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<PaymentStatusResponse>>>dashboardFilter(@RequestParam Boolean isOldest,
                                                                                    @RequestParam Boolean isAlreadyPaid,
                                                                                    @RequestParam Boolean isNoPaid,
                                                                                    @RequestParam List<CardType> paymentMethod,
                                                                                    @RequestParam List<String> category,
                                                                                    @RequestParam(required = false) Integer page) throws IOException {

        ResponseHandling<List<PaymentStatusResponse>> response = courseService.dashboardFilter(isOldest, isAlreadyPaid, isNoPaid, paymentMethod, category, page);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(
            path = "/search-dashboard",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<PaymentStatusResponse>>>searchDashboard(@RequestParam(required = false) String keyword,
                                                                                        @RequestParam(required = false) Integer page){
        ResponseHandling<List<PaymentStatusResponse>> response = courseService.searchDashboard(keyword, page);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(
            path = "/get-manage-class",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<ManageClassResponse>>>getManageClass(@RequestParam(required = false) Integer page){
        ResponseHandling<List<ManageClassResponse>> response = courseService.getManageClass(page);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
