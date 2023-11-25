package com.binar.backendonlinecourseapp.Service.Impl;

import com.binar.backendonlinecourseapp.DTO.Request.CourseCreateRequest;
import com.binar.backendonlinecourseapp.DTO.Response.CourseCreateResponse;
import com.binar.backendonlinecourseapp.DTO.Response.ResponseHandling;
import com.binar.backendonlinecourseapp.Entity.Category;
import com.binar.backendonlinecourseapp.Entity.Course;
import com.binar.backendonlinecourseapp.Entity.User;
import com.binar.backendonlinecourseapp.Entity.Video;
import com.binar.backendonlinecourseapp.Repository.CategoryRepository;
import com.binar.backendonlinecourseapp.Repository.CourseRepository;
import com.binar.backendonlinecourseapp.Repository.UserRepository;
import com.binar.backendonlinecourseapp.Repository.VideoRepository;
import com.binar.backendonlinecourseapp.Service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Override
    public ResponseHandling<CourseCreateResponse> createCourse(CourseCreateRequest courseRequest) {
        ResponseHandling<CourseCreateResponse> response = new ResponseHandling<>();
        Optional<Category> category = categoryRepository.findByCategoryName(courseRequest.getKategori());
        Optional<User> user = userRepository.findByEmail(getAuth());

        if (!category.isPresent()){
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
        course.setTeacher(user.get().getNama());
        course.setMateri(courseRequest.getMateri());
        course.setPublish(new Date());
        course.setClassType(courseRequest.getTipeKelas());
        course.setCategories(category1);
        courseRepository.save(course);
        List<Video> videos = courseRequest.getInsertVideo().stream().map((p)->{
            Video video = new Video();
            video.setVideoCode(getUUIDCode());
            video.setVideoTitle(p.getJudulVideo());
            video.setVideoLink(p.getLinkVideo());
            video.setDescription(p.getDesc());
            video.setPremium(p.isPremium());
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
