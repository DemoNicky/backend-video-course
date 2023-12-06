package com.binar.backendonlinecourseapp.Service.Impl;

import com.binar.backendonlinecourseapp.DTO.Request.CourseCreateRequest;
import com.binar.backendonlinecourseapp.DTO.Request.CourseUpdateRequest;
import com.binar.backendonlinecourseapp.DTO.Response.*;
import com.binar.backendonlinecourseapp.Entity.*;
import com.binar.backendonlinecourseapp.Repository.*;
import com.binar.backendonlinecourseapp.Service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Override
    public ResponseHandling<CourseCreateResponse> createCourse(CourseCreateRequest courseRequest) {
        ResponseHandling<CourseCreateResponse> response = new ResponseHandling<>();
        Optional<Category> category = categoryRepository.findByCategoryName(courseRequest.getKategori());
        Optional<User> user = userRepository.findByEmail(getAuth());
        Optional<Course> courseCodeCheck = courseRepository.findByCourseCode(courseRequest.getKodeKelas());
        Optional<Course> courseNameCheck = courseRepository.findByClassName(courseRequest.getNamaKelas());
        if (courseCodeCheck.isPresent()){
            response.setMessage("course code already exists");
            response.setErrors(true);
            return response;
        }else if (courseNameCheck.isPresent()){
            response.setMessage("course name already exists");
            response.setErrors(true);
            return response;
        }else if (!category.isPresent()){
            response.setMessage("category not found");
            response.setErrors(true);
            return response;
        }
        Category category1 = category.get();
        Course course = new Course();
        course.setCourseCode(courseRequest.getKodeKelas());
        course.setClassName(courseRequest.getNamaKelas());
        course.setLevel(courseRequest.getLevel());
        course.setPrice(courseRequest.getHarga());
        course.setAuthor(user.get().getNama());
        course.setMateri(courseRequest.getMateri());
        course.setPublish(new Date());
        course.setClassType(courseRequest.getTipeKelas());
        course.setCategories(category1);
        Random random = new Random();

        double randomNumber = 1.0 + new Random().nextDouble() * (5.0 - 1.0);
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        double formattedNumber = Double.parseDouble(decimalFormat.format(randomNumber));

        course.setRating(formattedNumber);

        course.setModul(new Random().nextInt(10) + 1);
        courseRepository.save(course);
        courseRequest.getInsertVideo().stream().map((p)->{
            Video video = new Video();
            video.setVideoCode(getUUIDCode());
            video.setVideoTitle(p.getJudulVideo());
            video.setVideoLink(p.getLinkVideo());
            video.setPremium(p.getIsPremium());
            video.setChapter(p.getChapter());
            video.setCourse(course);
            videoRepository.save(video);
            return video;
        }).collect(Collectors.toList());

        CourseCreateResponse courseCreateResponse = new CourseCreateResponse();
        courseCreateResponse.setNamaKelas(courseRequest.getNamaKelas());
        courseCreateResponse.setKodeKelas(courseRequest.getKodeKelas());
        courseCreateResponse.setKategori(courseRequest.getKategori());
        courseCreateResponse.setHarga(courseRequest.getHarga());
        courseCreateResponse.setMateri(courseRequest.getMateri());
        response.setData(courseCreateResponse);
        response.setMessage("sucess create new course");
        response.setErrors(false);
        return response;
    }

    @Override
    public ResponseHandling<List<CourseGetResponse>> getCourse(Pageable pageable) {
        ResponseHandling<List<CourseGetResponse>> response = new ResponseHandling<>();
        Page<Course> course = courseRepository.findAll(pageable);
        if (course.isEmpty() || course == null){
            response.setMessage("course data is null");
            response.setErrors(true);
            return response;
        }
        List<CourseGetResponse> courseGetResponses = course.stream().map((p)-> {
            CourseGetResponse courseGetResponse = new CourseGetResponse();
            courseGetResponse.setKodeKelas(p.getCourseCode());
            courseGetResponse.setNamaKelas(p.getClassName());
            courseGetResponse.setKategori(p.getCategories().getCategoryName());
            courseGetResponse.setLevel(p.getLevel());
            courseGetResponse.setHarga(p.getPrice());
            courseGetResponse.setAuthor(p.getAuthor());
            courseGetResponse.setRating(p.getRating());
            courseGetResponse.setModul(p.getModul());
            courseGetResponse.setTipeKelas(p.getClassType());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String outputDate = dateFormat.format(p.getPublish());

            courseGetResponse.setPublish(outputDate);
            return courseGetResponse;
        }).collect(Collectors.toList());
        response.setData(courseGetResponses);
        response.setMessage("success get data");
        response.setErrors(false);
        return response;
    }

    @Override
    public ResponseHandling<List<CourseGetResponse>> searchCourse(String courseName, Pageable pageable) {
        ResponseHandling<List<CourseGetResponse>> response = new ResponseHandling<>();
        Page<Course> courses = courseRepository.findByClassNameOrTeacherJPQL(courseName, pageable);
        if (courses.isEmpty()){
            response.setMessage("course not found");
            response.setErrors(true);
            return response;
        }
        List<CourseGetResponse> courseGetResponses = courses.stream().map((p) -> {
            CourseGetResponse courseGetResponse = new CourseGetResponse();
            courseGetResponse.setKodeKelas(p.getCourseCode());
            courseGetResponse.setNamaKelas(p.getClassName());
            courseGetResponse.setKategori(p.getCategories().getCategoryName());
            courseGetResponse.setLevel(p.getLevel());
            courseGetResponse.setHarga(p.getPrice());
            courseGetResponse.setAuthor(p.getAuthor());
            courseGetResponse.setRating(p.getRating());
            courseGetResponse.setModul(p.getModul());
            courseGetResponse.setTipeKelas(p.getClassType());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String outputDate = dateFormat.format(p.getPublish());

            courseGetResponse.setPublish(outputDate);
            return courseGetResponse;
        }).collect(Collectors.toList());
        response.setData(courseGetResponses);
        response.setMessage("success get data");
        response.setErrors(false);

        return response;
    }

    @Override
    public ResponseHandling<GetCourseResponse> hitGetCourse(String courseCode) {
        ResponseHandling<GetCourseResponse> response = new ResponseHandling<>();
        Optional<User> user = userRepository.findByEmail(getAuth());
        Optional<Course> course = courseRepository.findByCourseCode(courseCode);
        if (!course.isPresent()){
            response.setMessage("fail hit data");
            response.setErrors(true);
            return response;
        }
        Optional<Order> order = orderRepository.findOrdersByUserAndCourse(user.get(), course.get());
        if (!order.isPresent() || order.get().getCompletePaid() == false){
            Course courseGet = course.get();
            GetCourseResponse getCourseResponse = new GetCourseResponse();
            getCourseResponse.setKodeKelas(courseGet.getCourseCode());
            getCourseResponse.setNamaKelas(courseGet.getClassName());
            getCourseResponse.setKategori(courseGet.getCategories().getCategoryName());
            getCourseResponse.setLevel(courseGet.getLevel());
            getCourseResponse.setHarga(courseGet.getPrice());
            getCourseResponse.setAuthor(courseGet.getAuthor());
            getCourseResponse.setRating(courseGet.getRating());
            getCourseResponse.setModul(courseGet.getModul());
            getCourseResponse.setDeskripsi(courseGet.getMateri());

            List<GetVideoResponse> getVideoResponses = courseGet.getVideos().stream().map((p)->{
                GetVideoResponse getVideoResponse = new GetVideoResponse();
                getVideoResponse.setVideoCode(p.getVideoCode());
                getVideoResponse.setJudulVideo(p.getVideoTitle());
                if (p.getPremium()==true){
                    getVideoResponse.setLinkVideo(null);
                }else {
                    getVideoResponse.setLinkVideo(p.getVideoLink());
                }
                getVideoResponse.setPremium(p.getPremium());
                getVideoResponse.setChapter(p.getChapter());
                return getVideoResponse;
            }).collect(Collectors.toList());

            getCourseResponse.setGetVideoResponses(getVideoResponses);

            response.setData(getCourseResponse);
            response.setMessage("successfully get data");
            response.setErrors(false);
            return response;
        }

        Course courseGet = course.get();
        GetCourseResponse getCourseResponse = new GetCourseResponse();
        getCourseResponse.setKodeKelas(courseGet.getCourseCode());
        getCourseResponse.setNamaKelas(courseGet.getClassName());
        getCourseResponse.setKategori(courseGet.getCategories().getCategoryName());
        getCourseResponse.setLevel(courseGet.getLevel());
        getCourseResponse.setHarga(courseGet.getPrice());
        getCourseResponse.setAuthor(courseGet.getAuthor());
        getCourseResponse.setRating(courseGet.getRating());
        getCourseResponse.setModul(courseGet.getModul());
        getCourseResponse.setDeskripsi(courseGet.getMateri());
        List<GetVideoResponse> getVideoResponses = courseGet.getVideos().stream().map((p)->{
            GetVideoResponse getVideoResponse = new GetVideoResponse();
            getVideoResponse.setVideoCode(p.getVideoCode());
            getVideoResponse.setJudulVideo(p.getVideoTitle());
            getVideoResponse.setLinkVideo(p.getVideoLink());
            getVideoResponse.setPremium(p.getPremium());
            getVideoResponse.setChapter(p.getChapter());
            return getVideoResponse;
        }).collect(Collectors.toList());

        getCourseResponse.setGetVideoResponses(getVideoResponses);

        response.setData(getCourseResponse);
        response.setMessage("successfully get data");
        response.setErrors(false);
        return response;
    }

    @Override
    public ResponseHandling<List<PaymentHistoryResponse>> getPaymentHistory() {
        ResponseHandling<List<PaymentHistoryResponse>> response = new ResponseHandling<>();
        Optional<User> user = userRepository.findByEmail(getAuth());
        Optional<List<Order>> order = orderRepository.findOrdersByUser(user.get());
        if (!order.isPresent()){
            response.setMessage("payment history not found");
            response.setErrors(true);
            return response;
        }
        List<PaymentHistoryResponse> paymentHistoryResponse = order.get().stream().map((p)->{
            PaymentHistoryResponse paymentHistory = new PaymentHistoryResponse();
            paymentHistory.setOrderCode(p.getOrderCode());
            paymentHistory.setKodeKelas(p.getCourse().getCourseCode());
            paymentHistory.setNamaKelas(p.getCourse().getClassName());
            paymentHistory.setKategori(p.getCourse().getCategories().getCategoryName());
            paymentHistory.setLevel(p.getCourse().getLevel());
            paymentHistory.setAuthor(p.getCourse().getAuthor());
            paymentHistory.setCompletePaid(p.getCompletePaid());
            return paymentHistory;
        }).collect(Collectors.toList());
        response.setData(paymentHistoryResponse);
        response.setMessage("success get payment history");
        response.setErrors(false);
        return response;
    }

    @Override
    public ResponseHandling<CourseUpdateResponse> updateCourse(CourseUpdateRequest courseUpdateRequest) {
        ResponseHandling<CourseUpdateResponse> response = new ResponseHandling<>();
        Optional<Course> course = courseRepository.findByCourseCode(courseUpdateRequest.getKodeKelas());
        if (!course.isPresent()){
            response.setMessage("course not found");
            response.setErrors(true);
            return response;
        }

        Course course1 = course.get();

        course1.setClassType(courseUpdateRequest.getTipeKelas());
        course1.setLevel(courseUpdateRequest.getLevel());
        course1.setPrice(courseUpdateRequest.getHarga());
        course1.setMateri(courseUpdateRequest.getMateri());

        Video video = new Video();
        video.setVideoCode(getUUIDCode());
        video.setVideoTitle(courseUpdateRequest.getJudulVideo());
        video.setVideoLink(courseUpdateRequest.getLinkVideo());
        video.setPremium(courseUpdateRequest.getIsPremium());
        video.setChapter(courseUpdateRequest.getChapter());
        video.setCourse(course1);
        videoRepository.save(video);

        courseRepository.save(course1);

        CourseUpdateResponse courseUpdateResponse = new CourseUpdateResponse();
        courseUpdateResponse.setNamaKelas(course1.getClassName());
        courseUpdateResponse.setKategori(course1.getCategories().getCategoryName());
        courseUpdateResponse.setKodeKelas(course1.getCourseCode());
        courseUpdateResponse.setHarga(course1.getPrice());
        courseUpdateResponse.setMateri(course1.getMateri());
        response.setData(courseUpdateResponse);
        response.setMessage("success update course");
        response.setErrors(false);
        return response;
    }

    @Override
    public ResponseHandling<List<GetPremiumClassResponse>> getPremiumClass() {
        ResponseHandling<List<GetPremiumClassResponse>> response = new ResponseHandling<>();
        List<Course> courses = courseRepository.findPremiumCourses();
        List<GetPremiumClassResponse> getPremiumClassResponses = courses.stream().map((p) -> {
            GetPremiumClassResponse getPremiumClassResponse = new GetPremiumClassResponse();
            getPremiumClassResponse.setKodeKelas(p.getCourseCode());
            getPremiumClassResponse.setNamaKelas(p.getClassName());
            getPremiumClassResponse.setKategori(p.getCategories().getCategoryName());
            getPremiumClassResponse.setLevel(p.getLevel());
            getPremiumClassResponse.setHarga(p.getPrice());
            getPremiumClassResponse.setAuthor(p.getAuthor());
            getPremiumClassResponse.setTipeKelas(p.getClassType());
            getPremiumClassResponse.setRating(p.getRating());
            getPremiumClassResponse.setModul(p.getModul());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String outputDate = dateFormat.format(p.getPublish());
            getPremiumClassResponse.setPublish(outputDate);

            return getPremiumClassResponse;
        }).collect(Collectors.toList());
        response.setData(getPremiumClassResponses);
        response.setMessage("success get data");
        response.setErrors(false);

        return response;
    }

    @Override
    public ResponseHandling<List<CourseGetResponse>> getFreeClass() {
        ResponseHandling<List<CourseGetResponse>> response = new ResponseHandling<>();
        List<Course> courses = courseRepository.findFreeCourses();
        List<CourseGetResponse> courseGetResponsee = courses.stream().map((p) -> {
            CourseGetResponse courseGetResponse = new CourseGetResponse();
            courseGetResponse.setKodeKelas(p.getCourseCode());
            courseGetResponse.setNamaKelas(p.getClassName());
            courseGetResponse.setKategori(p.getCategories().getCategoryName());
            courseGetResponse.setLevel(p.getLevel());
            courseGetResponse.setHarga(p.getPrice());
            courseGetResponse.setAuthor(p.getAuthor());
            courseGetResponse.setTipeKelas(p.getClassType());
            courseGetResponse.setRating(p.getRating());
            courseGetResponse.setModul(p.getModul());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String outputDate = dateFormat.format(p.getPublish());
            courseGetResponse.setPublish(outputDate);

            return courseGetResponse;
        }).collect(Collectors.toList());

        response.setData(courseGetResponsee);
        response.setMessage("success get data");
        response.setErrors(false);
        return response;
    }

    private String getAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return email;
    }

    private String getUUIDCode() {
        UUID uuid = UUID.randomUUID();
        String kode = uuid.toString().substring(0, 6);
        return kode;
    }

}
