package com.binar.backendonlinecourseapp.Service.Impl;

import com.binar.backendonlinecourseapp.DTO.Request.*;
import com.binar.backendonlinecourseapp.DTO.Response.*;
import com.binar.backendonlinecourseapp.Entity.*;
import com.binar.backendonlinecourseapp.Entity.Enum.CardType;
import com.binar.backendonlinecourseapp.Entity.Enum.ClassType;
import com.binar.backendonlinecourseapp.Entity.Enum.Level;
import com.binar.backendonlinecourseapp.Entity.Enum.ProgressType;
import com.binar.backendonlinecourseapp.Repository.*;
import com.binar.backendonlinecourseapp.Service.CourseService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
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

    @Autowired
    private UserVideoRepository userVideoRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    private final Cloudinary cloudinary;

    @Transactional
    @Override
    public ResponseHandling<CourseCreateResponse> createCourseNew(CourseCreateRequest courseRequest) {
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
        course.setPictureUrl(category1.getPictureUrl());
        course.setLevel(courseRequest.getLevel());
        course.setPrice(courseRequest.getHarga());
        course.setAuthor(user.get().getNama());
        course.setMateri(courseRequest.getMateri());
        course.setPublish(new Date());
        course.setClassType(courseRequest.getTipeKelas());
        course.setCategories(category1);

        int randomNumber1 = getRandomNumberrr();

        course.setTime(randomNumber1);

        Random random = new Random();

        double randomNumber = 1.0 + new Random().nextDouble() * (5.0 - 1.0);
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        double formattedNumber = Double.parseDouble(decimalFormat.format(randomNumber));

        course.setRating(formattedNumber);

        course.setModul(new Random().nextInt(10) + 1);

        courseRepository.save(course);

        AtomicInteger counter = new AtomicInteger(chapterRepository.findAll().size() + 1);
        courseRequest.getChapterInsertRequests().stream().map((p) -> {
            Chapter chapter = new Chapter();
            chapter.setChaptertitle(p.getChaptertitle());
            chapter.setNumber(counter.incrementAndGet());
            int chapterTime = new Random().nextInt(51) + 10;
            chapter.setChapterTime(chapterTime);
            chapter.setCourse(course);


            chapterRepository.save(chapter);
            AtomicInteger counterVideo = new AtomicInteger(videoRepository.findAll().size() + 1);
            p.getInsertVideoRequests().stream().map((x) -> {
                Video video = new Video();
                video.setVideoCode(getUUIDCode());
                video.setNumber(counterVideo.incrementAndGet());
                video.setVideoTitle(x.getJudulVideo());
                video.setVideoLink(x.getLinkVideo());
                video.setPremium(x.getIsPremium());
                video.setChapter(chapter);
                videoRepository.save(video);
                return video;
            }).collect(Collectors.toList());

            return chapter;
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

    @Transactional
    @Override
    public ResponseHandling<CourseCreateResponse> createCourse(CourseCreateRequest courseRequest, MultipartFile file) throws IOException {
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

        Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        String imageUrl = result.get("url").toString();

        Category category1 = category.get();
        Course course = new Course();
        course.setCourseCode(courseRequest.getKodeKelas());
        course.setClassName(courseRequest.getNamaKelas());
        course.setPictureUrl(imageUrl);
        course.setLevel(courseRequest.getLevel());
        course.setPrice(courseRequest.getHarga());
        course.setAuthor(user.get().getNama());
        course.setMateri(courseRequest.getMateri());
        course.setPublish(new Date());
        course.setClassType(courseRequest.getTipeKelas());
        course.setCategories(category1);
        int randomNumber1 = getRandomNumberrr();

        course.setTime(randomNumber1);

        Random random = new Random();

        double randomNumber = 1.0 + new Random().nextDouble() * (5.0 - 1.0);
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        double formattedNumber = Double.parseDouble(decimalFormat.format(randomNumber));

        course.setRating(formattedNumber);

        course.setModul(new Random().nextInt(10) + 1);

        courseRepository.save(course);

        courseRequest.getChapterInsertRequests().stream().map((p) -> {
            Chapter chapter = new Chapter();
            chapter.setChaptertitle(p.getChaptertitle());
            int chapterTime = new Random().nextInt(51) + 10;
            chapter.setChapterTime(chapterTime);
            chapter.setCourse(course);

            p.getInsertVideoRequests().stream().map((x) -> {
                    Video video = new Video();
                    video.setVideoCode(getUUIDCode());
                    video.setVideoTitle(x.getJudulVideo());
                    video.setVideoLink(x.getLinkVideo());
                    video.setPremium(x.getIsPremium());
                    video.setChapter(chapter);
                    videoRepository.save(video);
                    return video;
                }).collect(Collectors.toList());

            chapterRepository.save(chapter);

            return chapter;
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

    @Transactional
    @Override
    public ResponseHandling<UpdateClassResponse> updateClassDataNew(String kodekelas, CourseUpdateRequest courseUpdateRequest) {
        ResponseHandling<UpdateClassResponse> response = new ResponseHandling<>();
        Optional<Course> course = courseRepository.findByCourseCode(kodekelas);
        Optional<Category> category = categoryRepository.findByCategoryName(courseUpdateRequest.getKategori());
        if (!course.isPresent()){
            response.setMessage("Cant get data, class code invalid");
            response.setErrors(true);
            return response;
        }

        Course courseGet = course.get();
        courseGet.setPictureUrl(category.get().getPictureUrl());
        courseGet.setClassName(courseUpdateRequest.getNamaKelas());
        courseGet.setCategories(category.get());
        courseGet.setClassType(courseUpdateRequest.getTipeKelas());
        courseGet.setLevel(courseUpdateRequest.getLevel());
        courseGet.setPrice(courseUpdateRequest.getHarga());
        courseGet.setMateri(courseUpdateRequest.getMateri());

        AtomicInteger counter = new AtomicInteger(chapterRepository.findAll().size() + 1);
        courseUpdateRequest.getChapterResponses().stream().map((p)->{

            Chapter chapter = new Chapter();

            if (p.getChapterCode().isEmpty() || p.getChapterCode()==null){
                chapter.setChaptertitle(p.getChaptertitle());
                chapter.setNumber(counter.incrementAndGet());
                int chapterTime = new Random().nextInt(51) + 10;
                chapter.setChapterTime(chapterTime);
                chapter.setCourse(courseGet);
            }else {
                Optional<Chapter> chapterOptional = chapterRepository.findById(p.getChapterCode());
                if (chapterOptional.isPresent()){
                    Chapter chapterGet = chapterOptional.get();
                    chapterGet.setChaptertitle(p.getChaptertitle());
                    chapter = chapterGet;
                }else {
                    response.setMessage("Chapter Code tidak di temukan");
                    response.setErrors(true);
                    return response;
                }
            }
            chapterRepository.save(chapter);

            AtomicInteger counterVideo = new AtomicInteger(videoRepository.findAll().size() + 1);
            Chapter finalChapter = chapter;
            p.getVideoResponseData().stream().map((x)->{
                Video video = new Video();
                if (x.getVideoCode().isEmpty() || x.getVideoCode() == null){
                    video.setVideoCode(getUUIDCode());
                    video.setVideoTitle(x.getJudulVideo());
                    video.setNumber(counterVideo.incrementAndGet());
                    video.setVideoLink(x.getLinkVideo());
                    video.setPremium(x.getIsPremium());

                    video.setChapter(finalChapter);
                    videoRepository.save(video);
                }else {
                    Optional<Video> videogett = videoRepository.findByVideoCode(x.getVideoCode());
                    if (videogett.isPresent()){
                        Video videoGet = videogett.get();
                        videoGet.setVideoTitle(x.getJudulVideo());
                        videoGet.setVideoLink(x.getLinkVideo());
                        videoGet.setPremium(x.getIsPremium());
                        videoRepository.save(videoGet);
                    }else {
                        response.setMessage("Video Code tidak di temukan");
                        response.setErrors(true);
                        return response;
                    }
                }
                return video;
            }).collect(Collectors.toList());
            return chapter;
        }).collect(Collectors.toList());

        courseRepository.save(courseGet);

        UpdateClassResponse updateClassResponse = new UpdateClassResponse();
        updateClassResponse.setCourseCode(courseGet.getCourseCode());

        response.setData(updateClassResponse);
        response.setMessage("suksess update data");
        response.setErrors(false);

        return response;
    }

    @Transactional
    @Override
    public ResponseHandling<UpdateClassResponse> updateClassData(String kodekelas, MultipartFile file, CourseUpdateRequest courseUpdateRequest) throws IOException {
        ResponseHandling<UpdateClassResponse> response = new ResponseHandling<>();
        Optional<Course> course = courseRepository.findByCourseCode(kodekelas);
        Optional<Category> category = categoryRepository.findByCategoryName(courseUpdateRequest.getKategori());
        if (!course.isPresent()){
            response.setMessage("Cant get data, class code invalid");
            response.setErrors(true);
            return response;
        }

        Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        String imageUrl = result.get("url").toString();

        Course courseGet = course.get();
        courseGet.setPictureUrl(imageUrl);
        courseGet.setClassName(courseUpdateRequest.getNamaKelas());
        courseGet.setCategories(category.get());
        courseGet.setClassType(courseUpdateRequest.getTipeKelas());
        courseGet.setLevel(courseUpdateRequest.getLevel());
        courseGet.setPrice(courseUpdateRequest.getHarga());
        courseGet.setMateri(courseUpdateRequest.getMateri());

        courseUpdateRequest.getChapterResponses().stream().map((p)->{
            Chapter chapter = new Chapter();
            if (p.getChapterCode().isEmpty() || p.getChapterCode()==null){
                chapter.setChaptertitle(p.getChaptertitle());
                int chapterTime = new Random().nextInt(51) + 10;
                chapter.setChapterTime(chapterTime);
                chapter.setCourse(courseGet);
            }else {
                Optional<Chapter> chapterOptional = chapterRepository.findById(p.getChapterCode());
                if (chapterOptional.isPresent()){
                    Chapter chapterGet = chapterOptional.get();
                    chapterGet.setChaptertitle(p.getChaptertitle());
                    chapter = chapterGet;
                }else {
                    response.setMessage("Chapter Code tidak di temukan");
                    response.setErrors(true);
                    return response;
                }
            }
            chapterRepository.save(chapter);

            p.getVideoResponseData().stream().map((x)->{
                Video video = new Video();
                if (x.getVideoCode().isEmpty() || x.getVideoCode() == null){
                    video.setVideoCode(getUUIDCode());
                    video.setVideoTitle(x.getJudulVideo());
                    video.setVideoLink(x.getLinkVideo());
                    video.setPremium(x.getIsPremium());
                    Optional<Chapter> chapterrr = chapterRepository.findByChaptertitle(p.getChaptertitle());
                    video.setChapter(chapterrr.get());
                    videoRepository.save(video);
                }else {
                    Optional<Video> videogett = videoRepository.findByVideoCode(x.getVideoCode());
                    if (videogett.isPresent()){
                        Video videoGet = videogett.get();
                        videoGet.setVideoTitle(x.getJudulVideo());
                        videoGet.setVideoLink(x.getLinkVideo());
                        videoGet.setPremium(x.getIsPremium());
                        videoRepository.save(videoGet);
                    }else {
                        response.setMessage("Video Code tidak di temukan");
                        response.setErrors(true);
                        return response;
                    }
                }
                return video;
            }).collect(Collectors.toList());
            return chapter;
        }).collect(Collectors.toList());

        courseRepository.save(courseGet);

        UpdateClassResponse updateClassResponse = new UpdateClassResponse();
        updateClassResponse.setCourseCode(courseGet.getCourseCode());

        response.setData(updateClassResponse);
        response.setMessage("suksess update data");
        response.setErrors(false);

        return response;

    }

    private int getRandomNumberrr() {
        Random random1 = new Random();
        int randomNumber1 = random1.nextInt(191) + 10;
        return randomNumber1;
    }

    @Override
    public ResponseHandling<List<CourseGetResponse>> getCourse(Integer page) {
        ResponseHandling<List<CourseGetResponse>> response = new ResponseHandling<>();
        List<Course> course;

        if (page == null) {
            course = courseRepository.findAll();
        } else {
            Pageable pageable = PageRequest.of(page, 10);
            Page<Course> coursePage = courseRepository.findAll(pageable);
            course = coursePage.getContent();
        }

        if (course.isEmpty() || course == null){
            response.setMessage("course data is null");
            response.setErrors(true);
            return response;
        }
        List<CourseGetResponse> courseGetResponses = course.stream().map((p)-> {
            CourseGetResponse courseGetResponse = new CourseGetResponse();
            courseGetResponse.setKodeKelas(p.getCourseCode());
            courseGetResponse.setNamaKelas(p.getClassName());
            courseGetResponse.setImageUrl(p.getPictureUrl());
            courseGetResponse.setKategori(p.getCategories().getCategoryName());
            courseGetResponse.setLevel(p.getLevel());
            courseGetResponse.setHarga(p.getPrice());
            courseGetResponse.setAuthor(p.getAuthor());
            courseGetResponse.setRating(p.getRating());
            courseGetResponse.setTime(p.getTime());
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
    public ResponseHandling<List<CourseGetResponse>> getSearchAllCourse(String courseName, Integer page) {
        ResponseHandling<List<CourseGetResponse>> response = new ResponseHandling<>();
        List<Course> courses;

        if (page == null) {
            courses = courseRepository.findByClassNameOrTeacherJPQL(courseName);
        } else {
            Pageable pageable = PageRequest.of(page, 10);
            Page<Course> coursePage = courseRepository.findByClassNameOrTeacherJPQLPage(courseName, pageable);
            courses = coursePage.getContent();
        }
        if (courses.isEmpty()){
            response.setMessage("course not found");
            response.setErrors(true);
            return response;
        }
        List<CourseGetResponse> courseGetResponses = courses.stream().map((p) -> {
            CourseGetResponse courseGetResponse = new CourseGetResponse();
            courseGetResponse.setKodeKelas(p.getCourseCode());
            courseGetResponse.setNamaKelas(p.getClassName());
            courseGetResponse.setImageUrl(p.getPictureUrl());
            courseGetResponse.setTime(p.getTime());
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
    public ResponseHandling<GetClassDataResponse> getClassData(String kodekelas) {
        ResponseHandling<GetClassDataResponse> response = new ResponseHandling<>();
        Optional<Course> course = courseRepository.findByCourseCode(kodekelas);
        if (!course.isPresent()){
            response.setMessage("Cant get data, class code invalid");
            response.setErrors(true);
            return response;
        }
        Course courseGet = course.get();
        GetClassDataResponse getClassDataResponse = new GetClassDataResponse();
        getClassDataResponse.setNamaKelas(courseGet.getClassName());
        getClassDataResponse.setKategori(courseGet.getCategories().getCategoryName());
        getClassDataResponse.setTipeKelas(courseGet.getClassType());
        getClassDataResponse.setLevel(courseGet.getLevel());
        getClassDataResponse.setHarga(courseGet.getPrice());
        getClassDataResponse.setMateri(courseGet.getMateri());

        List<Chapter> chapters = chapterRepository.findByCourseOrderByNumberAsc(courseGet);
        List<ChapterResponse> chapterInsertRequests = chapters.stream().map((p)->{
            ChapterResponse chapterInsertRequest = new ChapterResponse();
            chapterInsertRequest.setChapterCode(p.getId());
            chapterInsertRequest.setChaptertitle(p.getChaptertitle());

            List<Video> videos = videoRepository.findByChapterOrderByNumberVideo(p.getId());
            List<VideoResponseData> insertVideoRequests = videos.stream().map((x)->{
                VideoResponseData insertVideoRequest = new VideoResponseData();
                insertVideoRequest.setVideoCode(x.getVideoCode());
                insertVideoRequest.setJudulVideo(x.getVideoTitle());
                insertVideoRequest.setLinkVideo(x.getVideoLink());
                insertVideoRequest.setIsPremium(x.getPremium());
                return insertVideoRequest;
            }).collect(Collectors.toList());
            chapterInsertRequest.setVideoResponseData(insertVideoRequests);
            return chapterInsertRequest;
        }).collect(Collectors.toList());
        getClassDataResponse.setChapterResponses(chapterInsertRequests);
        response.setData(getClassDataResponse);
        response.setMessage("success get data");
        response.setErrors(false);
        return response;

    }

    @Override
    public ResponseHandling<List<CourseGetResponse>> searchCourse(String courseName, Integer page) {
        ResponseHandling<List<CourseGetResponse>> response = new ResponseHandling<>();
        List<Course> courses;

        if (page == null) {
            courses = courseRepository.findByClassNameOrTeacherJPQL(courseName);
        } else {
            Pageable pageable = PageRequest.of(page, 10);
            Page<Course> coursePage = courseRepository.findByClassNameOrTeacherJPQLPage(courseName, pageable);
            courses = coursePage.getContent();
        }
        if (courses.isEmpty()){
            response.setMessage("course not found");
            response.setErrors(true);
            return response;
        }
        List<CourseGetResponse> courseGetResponses = courses.stream().map((p) -> {
            CourseGetResponse courseGetResponse = new CourseGetResponse();
            courseGetResponse.setKodeKelas(p.getCourseCode());
            courseGetResponse.setNamaKelas(p.getClassName());
            courseGetResponse.setImageUrl(p.getPictureUrl());
            courseGetResponse.setTime(p.getTime());
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
    public ResponseHandling<List<CourseGetResponse>> searchCoursePremium(String courseName, Integer page) {
        ResponseHandling<List<CourseGetResponse>> response = new ResponseHandling<>();
        List<Course> courses;

        if (page == null) {
            courses = courseRepository.findByClassNameOrAuthorAndClassType(courseName);
        } else {
            Pageable pageable = PageRequest.of(page, 10);
            Page<Course> coursePage = courseRepository.findByClassNameOrAuthorAndClassTypepage(courseName, pageable);
            courses = coursePage.getContent();
        }
        if (courses.isEmpty()){
            response.setMessage("course not found");
            response.setErrors(true);
            return response;
        }
        List<CourseGetResponse> courseGetResponses = courses.stream().map((p) -> {
            CourseGetResponse courseGetResponse = new CourseGetResponse();
            courseGetResponse.setKodeKelas(p.getCourseCode());
            courseGetResponse.setNamaKelas(p.getClassName());
            courseGetResponse.setImageUrl(p.getPictureUrl());
            courseGetResponse.setTime(p.getTime());
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
    public ResponseHandling<List<CourseGetResponse>> searchCourseFree(String courseName, Integer page) {
        ResponseHandling<List<CourseGetResponse>> response = new ResponseHandling<>();
        List<Course> courses;

        if (page == null) {
            courses = courseRepository.findByClassNameOrAuthorAndClassTypeFree(courseName);
        } else {
            Pageable pageable = PageRequest.of(page, 10);
            Page<Course> coursePage = courseRepository.findByClassNameOrAuthorAndClassTypeFreepage(courseName, pageable);
            courses = coursePage.getContent();
        }
        if (courses.isEmpty()){
            response.setMessage("course not found");
            response.setErrors(true);
            return response;
        }
        List<CourseGetResponse> courseGetResponses = courses.stream().map((p) -> {
            CourseGetResponse courseGetResponse = new CourseGetResponse();
            courseGetResponse.setKodeKelas(p.getCourseCode());
            courseGetResponse.setNamaKelas(p.getClassName());
            courseGetResponse.setImageUrl(p.getPictureUrl());
            courseGetResponse.setTime(p.getTime());
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
        Optional<List<UserVideo>> userVideo = userVideoRepository.findByUser(user);

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
            getCourseResponse.setImageUrl(courseGet.getPictureUrl());
            getCourseResponse.setTime(courseGet.getTime());
            getCourseResponse.setKategori(courseGet.getCategories().getCategoryName());
            getCourseResponse.setLevel(courseGet.getLevel());
            getCourseResponse.setHarga(courseGet.getPrice());
            getCourseResponse.setAuthor(courseGet.getAuthor());
            getCourseResponse.setRating(courseGet.getRating());
            getCourseResponse.setModul(courseGet.getModul());
            getCourseResponse.setDeskripsi(courseGet.getMateri());

            int count = 0;

            for (UserVideo userVideo1 : userVideo.get()) {
                if (userVideo1.getCourse() == course.get()) {
                    count += 1;
                }
            }

            int videoSize = course.get().getChapters().stream()
                    .mapToInt(chapter -> chapter.getVideos().size())
                    .sum();
            int progressTotal = videoSize > 0 ? (count * 100) / videoSize : 0;
            getCourseResponse.setProgress(progressTotal);

            List<Chapter> chapters = chapterRepository.findByCourseOrderByNumberAsc(courseGet);
            List<GetChapterResponse> getChapterResponses = chapters.stream().map((p)->{
                GetChapterResponse getChapterResponse = new GetChapterResponse();
                getChapterResponse.setJudulChapter(p.getChaptertitle());
                getChapterResponse.setTime(p.getChapterTime());

                List<Video> videos = videoRepository.findByChapterOrderByNumberVideo(p.getId());
                List<GetVideoResponse> getVideoResponses = videos.stream().map((x)->{
                    GetVideoResponse getVideoResponse = new GetVideoResponse();
                    getVideoResponse.setVideoCode(x.getVideoCode());
                    getVideoResponse.setJudulVideo(x.getVideoTitle());
                    if (x.getPremium()==true){
                        getVideoResponse.setLinkVideo(null);
                    }else {
                        getVideoResponse.setLinkVideo(x.getVideoLink());
                    }
                    getVideoResponse.setPremium(x.getPremium());
                    return getVideoResponse;
                }).collect(Collectors.toList());
                getChapterResponse.setGetVideoResponses(getVideoResponses);

                return getChapterResponse;
            }).collect(Collectors.toList());

            getCourseResponse.setGetChapterResponses(getChapterResponses);

            response.setData(getCourseResponse);
            response.setMessage("successfully get data");
            response.setErrors(false);
            return response;
        }

        Course courseGet = course.get();
        GetCourseResponse getCourseResponse = new GetCourseResponse();
        getCourseResponse.setKodeKelas(courseGet.getCourseCode());
        getCourseResponse.setNamaKelas(courseGet.getClassName());
        getCourseResponse.setImageUrl(courseGet.getPictureUrl());
        getCourseResponse.setTime(courseGet.getTime());
        getCourseResponse.setKategori(courseGet.getCategories().getCategoryName());
        getCourseResponse.setLevel(courseGet.getLevel());
        getCourseResponse.setHarga(courseGet.getPrice());
        getCourseResponse.setAuthor(courseGet.getAuthor());
        getCourseResponse.setRating(courseGet.getRating());
        getCourseResponse.setModul(courseGet.getModul());
        getCourseResponse.setDeskripsi(courseGet.getMateri());

        int count = 0;

        for (UserVideo userVideo1 : userVideo.get()) {
            if (userVideo1.getCourse() == course.get()) {
                count += 1;
            }
        }

        int videoSize = course.get().getChapters().stream()
                .mapToInt(chapter -> chapter.getVideos().size())
                .sum();
        int progressTotal = videoSize > 0 ? (count * 100) / videoSize : 0;
        getCourseResponse.setProgress(progressTotal);

        List<Chapter> chapters = chapterRepository.findByCourseOrderByNumberAsc(courseGet);
        List<GetChapterResponse> getChapterResponses = chapters.stream().map((p)->{
            GetChapterResponse getChapterResponse = new GetChapterResponse();
            getChapterResponse.setJudulChapter(p.getChaptertitle());
            getChapterResponse.setTime(p.getChapterTime());

            List<Video> videos = videoRepository.findByChapterOrderByNumberVideo(p.getId());
            List<GetVideoResponse> getVideoResponses = videos.stream().map((x)->{
                GetVideoResponse getVideoResponse = new GetVideoResponse();
                getVideoResponse.setVideoCode(x.getVideoCode());
                getVideoResponse.setJudulVideo(x.getVideoTitle());
                getVideoResponse.setLinkVideo(x.getVideoLink());
                getVideoResponse.setPremium(x.getPremium());
                return getVideoResponse;
            }).collect(Collectors.toList());
            getChapterResponse.setGetVideoResponses(getVideoResponses);

            return getChapterResponse;
        }).collect(Collectors.toList());

        getCourseResponse.setGetChapterResponses(getChapterResponses);

        response.setData(getCourseResponse);
        response.setMessage("successfully get data");
        response.setErrors(false);
        return response;
    }

    @Override
    public ResponseHandling<List<PaymentHistoryResponse>> getPaymentHistory(Integer page) {
        ResponseHandling<List<PaymentHistoryResponse>> response = new ResponseHandling<>();
        Optional<User> user = userRepository.findByEmail(getAuth());
        Optional<List<Order>> order;
        if (page == null) {
            order = orderRepository.findOrdersByUser(user.get());
        } else {
            Pageable pageable = PageRequest.of(page, 10);
            Optional<Page<Order>> coursePage = orderRepository.findOrdersByUser(user.get(), pageable);
            order = coursePage.map(pageContent -> pageContent.getContent())
                    .map(Collections::unmodifiableList);
        }
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
            paymentHistory.setImageUrl(p.getCourse().getPictureUrl());
            paymentHistory.setKategori(p.getCourse().getCategories().getCategoryName());
            paymentHistory.setLevel(p.getCourse().getLevel());
            paymentHistory.setRating(p.getCourse().getRating());
            paymentHistory.setModul(p.getCourse().getModul());
            paymentHistory.setAuthor(p.getCourse().getAuthor());
            paymentHistory.setTime(p.getCourse().getTime());
            paymentHistory.setCompletePaid(p.getCompletePaid());
            return paymentHistory;
        }).collect(Collectors.toList());
        response.setData(paymentHistoryResponse);
        response.setMessage("success get payment history");
        response.setErrors(false);
        return response;
    }

    @Override
    public ResponseHandling<List<CourseGetResponse>> getPremiumClass(Integer page) {
        ResponseHandling<List<CourseGetResponse>> response = new ResponseHandling<>();
        List<Course> courses = courseRepository.findPremiumCourses();
        if (page == null) {
            courses = courseRepository.findPremiumCourses();
        } else {
            Pageable pageable = PageRequest.of(page, 10);
            Page<Course> coursePage = courseRepository.findPremiumCourses(pageable);
            courses = coursePage.getContent();
        }

        List<CourseGetResponse> courseGetResponsee = courses.stream().map((p) -> {
            CourseGetResponse courseGetResponse = new CourseGetResponse();
            courseGetResponse.setKodeKelas(p.getCourseCode());
            courseGetResponse.setNamaKelas(p.getClassName());
            courseGetResponse.setImageUrl(p.getPictureUrl());
            courseGetResponse.setKategori(p.getCategories().getCategoryName());
            courseGetResponse.setLevel(p.getLevel());
            courseGetResponse.setHarga(p.getPrice());
            courseGetResponse.setAuthor(p.getAuthor());
            courseGetResponse.setRating(p.getRating());
            courseGetResponse.setTime(p.getTime());
            courseGetResponse.setModul(p.getModul());
            courseGetResponse.setTipeKelas(p.getClassType());

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

    @Override
    public ResponseHandling<List<CourseGetResponse>> getFreeClass(Integer page) {
        ResponseHandling<List<CourseGetResponse>> response = new ResponseHandling<>();
        List<Course> courses;
        if (page == null) {
            courses = courseRepository.findFreeCourses();
        } else {
            Pageable pageable = PageRequest.of(page, 10);
            Page<Course> coursePage = courseRepository.findFreeCourses(pageable);
            courses = coursePage.getContent();
        }
        List<CourseGetResponse> courseGetResponsee = courses.stream().map((p) -> {
            CourseGetResponse courseGetResponse = new CourseGetResponse();
            courseGetResponse.setKodeKelas(p.getCourseCode());
            courseGetResponse.setNamaKelas(p.getClassName());
            courseGetResponse.setImageUrl(p.getPictureUrl());
            courseGetResponse.setKategori(p.getCategories().getCategoryName());
            courseGetResponse.setLevel(p.getLevel());
            courseGetResponse.setHarga(p.getPrice());
            courseGetResponse.setAuthor(p.getAuthor());
            courseGetResponse.setRating(p.getRating());
            courseGetResponse.setTime(p.getTime());
            courseGetResponse.setModul(p.getModul());
            courseGetResponse.setTipeKelas(p.getClassType());

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

    @Override
    public void videoTrigger(String videoCode) {
        Optional<User> user = userRepository.findByEmail(getAuth());
        Optional<Video> video = videoRepository.findByVideoCode(videoCode);
        Optional<Course> course = courseRepository.findCourseByVideoCode(videoCode);
        Optional<Order> order = orderRepository.findOrdersByUserAndCourse(user.get(),course.get());
        Optional<UserVideo> userVideoCheck = userVideoRepository.findByUserAndVideo(user.get(), video.get());

        if (order.isPresent() && order.get().getCompletePaid()==true && !userVideoCheck.isPresent()){
            UserVideo userVideo = new UserVideo();
            userVideo.setIsWatched(true);
            userVideo.setUser(user.get());
            userVideo.setVideo(video.get());
            userVideo.setCourse(course.get());
            userVideoRepository.save(userVideo);
        }
    }

    @Override
    public ResponseHandling<List<UserWatchProgressResponse>> getProgressAndFinished(Integer page) {
        ResponseHandling<List<UserWatchProgressResponse>> response = new ResponseHandling<>();
        Optional<User> user = userRepository.findByEmail(getAuth());
        Optional<List<UserVideo>> userVideo = userVideoRepository.findByUser(user);
        Optional<List<Order>> order = orderRepository.findOrdersByUser(user.get());

        if (!order.isPresent() || order.get().size()==0){
            response.setMessage("user dont have data");
            response.setErrors(true);
            return response;
        }
        if (!userVideo.isPresent()) {
            response.setMessage("user never learnnnnn");
            response.setErrors(true);
            return response;
        }

        List<UserWatchProgressResponse> responses = order.get().stream().map((p)->{
            UserWatchProgressResponse userWatchProgressResponse = new UserWatchProgressResponse();
            userWatchProgressResponse.setKodeKelas(p.getCourse().getCourseCode());
            userWatchProgressResponse.setNamaKelas(p.getCourse().getClassName());
            userWatchProgressResponse.setImageUrl(p.getCourse().getPictureUrl());
            userWatchProgressResponse.setKategori(p.getCourse().getCategories().getCategoryName());
            userWatchProgressResponse.setLevel(p.getCourse().getLevel());
            userWatchProgressResponse.setAuthor(p.getCourse().getAuthor());
            userWatchProgressResponse.setRating(p.getCourse().getRating());
            userWatchProgressResponse.setModul(p.getCourse().getModul());
            userWatchProgressResponse.setTime(p.getCourse().getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String outputDate = dateFormat.format(p.getCourse().getPublish());
            userWatchProgressResponse.setPublish(outputDate);
            int count = 0;

            for (UserVideo userVideo1 : userVideo.get()) {
                if (userVideo1.getCourse() == p.getCourse()) {
                    count += 1;
                }
            }

            int videoSize = p.getCourse().getChapters().stream()
                    .mapToInt(chapter -> chapter.getVideos().size())
                    .sum();
            int progressTotal = videoSize > 0 ? (count * 100) / videoSize : 0;
            userWatchProgressResponse.setProgress(progressTotal);

            return userWatchProgressResponse;
        }).collect(Collectors.toList());

        response.setData(responses);
        response.setMessage("success get data");
        response.setErrors(false);
        return response;
    }

    @Override
    public ResponseHandling<List<UserWatchProgressResponse>> searchProgressAndFinished(String courseName, Integer page) {
        ResponseHandling<List<UserWatchProgressResponse>> response = new ResponseHandling<>();
        Optional<User> user = userRepository.findByEmail(getAuth());
        Optional<List<UserVideo>> userVideo = userVideoRepository.findByUser(user);
        List<Course> courses = courseRepository.findByClassNameOrTeacherJPQL(courseName);
        List<Order> order;

        if (page == null) {
            order = orderRepository.findByUserAndCourseIn(user.get(), courses);
        } else {
            Pageable pageable = PageRequest.of(page, 10);
            Page<Order> orderPage = orderRepository.findByUserAndCourseIn(user.get(), courses, pageable);
            order = orderPage.getContent();
        }

        if (order == null || order.isEmpty()) {
            response.setMessage("user dont have data");
            response.setErrors(true);
            return response;
        }

        List<UserWatchProgressResponse> responses = order.stream().map((p)->{
            UserWatchProgressResponse userWatchProgressResponse = new UserWatchProgressResponse();
            userWatchProgressResponse.setKodeKelas(p.getCourse().getCourseCode());
            userWatchProgressResponse.setNamaKelas(p.getCourse().getClassName());
            userWatchProgressResponse.setImageUrl(p.getCourse().getPictureUrl());
            userWatchProgressResponse.setKategori(p.getCourse().getCategories().getCategoryName());
            userWatchProgressResponse.setLevel(p.getCourse().getLevel());
            userWatchProgressResponse.setAuthor(p.getCourse().getAuthor());
            userWatchProgressResponse.setRating(p.getCourse().getRating());
            userWatchProgressResponse.setModul(p.getCourse().getModul());
            userWatchProgressResponse.setTime(p.getCourse().getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String outputDate = dateFormat.format(p.getCourse().getPublish());
            userWatchProgressResponse.setPublish(outputDate);
            int count = 0;

            for (UserVideo userVideo1 : userVideo.get()) {
                if (userVideo1.getCourse() == p.getCourse()) {
                    count += 1;
                }
            }

            int videoSize = p.getCourse().getChapters().stream()
                    .mapToInt(chapter -> chapter.getVideos().size())
                    .sum();
            int progressTotal = videoSize > 0 ? (count * 100) / videoSize : 0;
            userWatchProgressResponse.setProgress(progressTotal);

            return userWatchProgressResponse;
        }).collect(Collectors.toList());

        response.setData(responses);
        response.setMessage("success get data");
        response.setErrors(false);
        return response;
    }

    @Override
    public ResponseHandling<List<UserWatchProgressResponse>> searchProgress(String courseName, Integer page) {
        ResponseHandling<List<UserWatchProgressResponse>> response = new ResponseHandling<>();
        Optional<User> user = userRepository.findByEmail(getAuth());
        Optional<List<UserVideo>> userVideo = userVideoRepository.findByUser(user);
        List<Course> courses = courseRepository.findByClassNameOrTeacherJPQL(courseName);
        List<Order> order;

        if (page == null) {
            order = orderRepository.findByUserAndCourseIn(user.get(), courses);
        } else {
            Pageable pageable = PageRequest.of(page, 10);
            Page<Order> orderPage = orderRepository.findByUserAndCourseIn(user.get(), courses, pageable);
            order = orderPage.getContent();
        }

        if (order == null || order.isEmpty()) {
            response.setMessage("user dont have data");
            response.setErrors(true);
            return response;
        }

        List<UserWatchProgressResponse> responses = order.stream().map((p)->{
            UserWatchProgressResponse userWatchProgressResponse = new UserWatchProgressResponse();
            userWatchProgressResponse.setKodeKelas(p.getCourse().getCourseCode());
            userWatchProgressResponse.setNamaKelas(p.getCourse().getClassName());
            userWatchProgressResponse.setImageUrl(p.getCourse().getPictureUrl());
            userWatchProgressResponse.setKategori(p.getCourse().getCategories().getCategoryName());
            userWatchProgressResponse.setLevel(p.getCourse().getLevel());
            userWatchProgressResponse.setAuthor(p.getCourse().getAuthor());
            userWatchProgressResponse.setRating(p.getCourse().getRating());
            userWatchProgressResponse.setModul(p.getCourse().getModul());
            userWatchProgressResponse.setTime(p.getCourse().getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String outputDate = dateFormat.format(p.getCourse().getPublish());
            userWatchProgressResponse.setPublish(outputDate);
            int count = 0;

            for (UserVideo userVideo1 : userVideo.get()) {
                if (userVideo1.getCourse() == p.getCourse()) {
                    count += 1;
                }
            }

            int videoSize = p.getCourse().getChapters().stream()
                    .mapToInt(chapter -> chapter.getVideos().size())
                    .sum();
            int progressTotal = videoSize > 0 ? (count * 100) / videoSize : 0;
            userWatchProgressResponse.setProgress(progressTotal);

            if (progressTotal == 100){
                return null;
            }

            return userWatchProgressResponse;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        response.setData(responses);
        response.setMessage("success get data");
        response.setErrors(false);
        return response;
    }

    @Override
    public ResponseHandling<List<UserWatchProgressResponse>> searchFinished(String courseName, Integer page) {
        ResponseHandling<List<UserWatchProgressResponse>> response = new ResponseHandling<>();
        Optional<User> user = userRepository.findByEmail(getAuth());
        Optional<List<UserVideo>> userVideo = userVideoRepository.findByUser(user);
        List<Course> courses = courseRepository.findByClassNameOrTeacherJPQL(courseName);
        List<Order> order;

        if (page == null) {
            order = orderRepository.findByUserAndCourseIn(user.get(), courses);
        } else {
            Pageable pageable = PageRequest.of(page, 10);
            Page<Order> orderPage = orderRepository.findByUserAndCourseIn(user.get(), courses, pageable);
            order = orderPage.getContent();
        }

        if (order == null || order.isEmpty()) {
            response.setMessage("user dont have data");
            response.setErrors(true);
            return response;
        }
        List<UserWatchProgressResponse> responses = order.stream().map((p) -> {
            UserWatchProgressResponse userWatchProgressResponse = new UserWatchProgressResponse();
            userWatchProgressResponse.setKodeKelas(p.getCourse().getCourseCode());
            userWatchProgressResponse.setNamaKelas(p.getCourse().getClassName());
            userWatchProgressResponse.setImageUrl(p.getCourse().getPictureUrl());
            userWatchProgressResponse.setKategori(p.getCourse().getCategories().getCategoryName());
            userWatchProgressResponse.setLevel(p.getCourse().getLevel());
            userWatchProgressResponse.setAuthor(p.getCourse().getAuthor());
            userWatchProgressResponse.setRating(p.getCourse().getRating());
            userWatchProgressResponse.setModul(p.getCourse().getModul());
            userWatchProgressResponse.setTime(p.getCourse().getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String outputDate = dateFormat.format(p.getCourse().getPublish());
            userWatchProgressResponse.setPublish(outputDate);
            int count = 0;

            for (UserVideo userVideo1 : userVideo.get()) {
                if (userVideo1.getCourse() == p.getCourse()) {
                    count += 1;
                }
            }

            int videoSize = p.getCourse().getChapters().stream()
                    .mapToInt(chapter -> chapter.getVideos().size())
                    .sum();
            int progressTotal = videoSize > 0 ? (count * 100) / videoSize : 0;
            userWatchProgressResponse.setProgress(progressTotal);

            return userWatchProgressResponse;
        }).collect(Collectors.toList());

        List<UserWatchProgressResponse> filteredList = responses.stream()
                .filter(userWatchProgressResponse -> userWatchProgressResponse.getProgress() == 100)
                .collect(Collectors.toList());

        response.setData(filteredList);
        response.setMessage("success get data");
        response.setErrors(false);
        return response;
    }

    @Override
    public ResponseHandling<List<UserWatchProgressResponse>> filterProgress(Boolean isNewest, Boolean isPopular,
                                                                            ProgressType progressType, List<String> category, List<Level> level) {
        ResponseHandling<List<UserWatchProgressResponse>> response = new ResponseHandling<>();
        Optional<User> user = userRepository.findByEmail(getAuth());
        Optional<List<UserVideo>> userVideo = userVideoRepository.findByUser(user);
        List<Category> categories = category.stream()
                .map(categoryRepository::findByCategoryName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        List<Order> order = orderRepository.findFilteredOrdersByUser(user.get(), categories, level, isPopular, isNewest);

        if (order == null || order.isEmpty()){
            response.setMessage("user dont have data");
            response.setErrors(true);
            return response;
        }

        List<UserWatchProgressResponse> responses = order.stream().map((p)->{
            UserWatchProgressResponse userWatchProgressResponse = new UserWatchProgressResponse();
            userWatchProgressResponse.setKodeKelas(p.getCourse().getCourseCode());
            userWatchProgressResponse.setNamaKelas(p.getCourse().getClassName());
            userWatchProgressResponse.setImageUrl(p.getCourse().getPictureUrl());
            userWatchProgressResponse.setKategori(p.getCourse().getCategories().getCategoryName());
            userWatchProgressResponse.setLevel(p.getCourse().getLevel());
            userWatchProgressResponse.setAuthor(p.getCourse().getAuthor());
            userWatchProgressResponse.setRating(p.getCourse().getRating());
            userWatchProgressResponse.setModul(p.getCourse().getModul());
            userWatchProgressResponse.setTime(p.getCourse().getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String outputDate = dateFormat.format(p.getCourse().getPublish());
            userWatchProgressResponse.setPublish(outputDate);
            int count = 0;

            for (UserVideo userVideo1 : userVideo.get()) {
                if (userVideo1.getCourse() == p.getCourse()) {
                    count += 1;
                }
            }

            int videoSize = p.getCourse().getChapters().stream()
                    .mapToInt(chapter -> chapter.getVideos().size())
                    .sum();
            int progressTotal = videoSize > 0 ? (count * 100) / videoSize : 0;
            userWatchProgressResponse.setProgress(progressTotal);

            if (progressTotal == 100 && progressType== ProgressType.PROGRESS){
                return null;
            }

            return userWatchProgressResponse;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        if (progressType == ProgressType.FINISH){
            List<UserWatchProgressResponse> filteredList = responses.stream()
                    .filter(userWatchProgressResponse -> userWatchProgressResponse.getProgress() == 100)
                    .collect(Collectors.toList());
            responses = filteredList;
        }

        response.setData(responses);
        response.setMessage("success get data");
        response.setErrors(false);
        return response;
    }

    @Override
    public ResponseHandling<List<UserWatchProgressResponse>> getProgressResponse(Integer page) {
        ResponseHandling<List<UserWatchProgressResponse>> response = new ResponseHandling<>();
        Optional<User> user = userRepository.findByEmail(getAuth());
        Optional<List<UserVideo>> userVideo = userVideoRepository.findByUser(user);
        Optional<List<Order>> order;

        if (page == null) {
            order = orderRepository.findOrdersByUser(user.get());
        } else {
            Pageable pageable = PageRequest.of(page, 10);
            Optional<Page<Order>> coursePage = orderRepository.findOrdersByUser(user.get(), pageable);
            order = coursePage.map(pageContent -> pageContent.getContent())
                    .map(Collections::unmodifiableList);
        }

        if (!order.isPresent() || order.get().size()==0) {
            response.setMessage("user dont have data");
            response.setErrors(true);
            return response;
        }

        List<UserWatchProgressResponse> responses = order.get().stream().map((p)->{
            UserWatchProgressResponse userWatchProgressResponse = new UserWatchProgressResponse();
            userWatchProgressResponse.setKodeKelas(p.getCourse().getCourseCode());
            userWatchProgressResponse.setNamaKelas(p.getCourse().getClassName());
            userWatchProgressResponse.setImageUrl(p.getCourse().getPictureUrl());
            userWatchProgressResponse.setKategori(p.getCourse().getCategories().getCategoryName());
            userWatchProgressResponse.setLevel(p.getCourse().getLevel());
            userWatchProgressResponse.setAuthor(p.getCourse().getAuthor());
            userWatchProgressResponse.setRating(p.getCourse().getRating());
            userWatchProgressResponse.setModul(p.getCourse().getModul());
            userWatchProgressResponse.setTime(p.getCourse().getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String outputDate = dateFormat.format(p.getCourse().getPublish());
            userWatchProgressResponse.setPublish(outputDate);
            int count = 0;

            for (UserVideo userVideo1 : userVideo.get()) {
                if (userVideo1.getCourse() == p.getCourse()) {
                    count += 1;
                }
            }

            int videoSize = p.getCourse().getChapters().stream()
                    .mapToInt(chapter -> chapter.getVideos().size())
                    .sum();
            int progressTotal = videoSize > 0 ? (count * 100) / videoSize : 0;
            userWatchProgressResponse.setProgress(progressTotal);

            if (progressTotal == 100){
                return null;
            }

            return userWatchProgressResponse;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        response.setData(responses);
        response.setMessage("success get data");
        response.setErrors(false);
        return response;
    }

    @Override
    public ResponseHandling<List<UserWatchProgressResponse>> getFinishedClass(Integer page) {
        ResponseHandling<List<UserWatchProgressResponse>> response = new ResponseHandling<>();
        Optional<User> user = userRepository.findByEmail(getAuth());
        Optional<List<UserVideo>> userVideo = userVideoRepository.findByUser(user);
        Optional<List<Order>> order;

        if (page == null) {
            order = orderRepository.findOrdersByUser(user.get());
        } else {
            Pageable pageable = PageRequest.of(page, 10);
            Optional<Page<Order>> coursePage = orderRepository.findOrdersByUser(user.get(), pageable);
            order = coursePage.map(pageContent -> pageContent.getContent())
                    .map(Collections::unmodifiableList);
        }

        if (!order.isPresent() || order.get().size()==0){
            response.setMessage("user dont have data");
            response.setErrors(true);
            return response;
        }

        List<UserWatchProgressResponse> responses = order.get().stream().map((p)->{
            UserWatchProgressResponse userWatchProgressResponse = new UserWatchProgressResponse();
            userWatchProgressResponse.setKodeKelas(p.getCourse().getCourseCode());
            userWatchProgressResponse.setNamaKelas(p.getCourse().getClassName());
            userWatchProgressResponse.setImageUrl(p.getCourse().getPictureUrl());
            userWatchProgressResponse.setKategori(p.getCourse().getCategories().getCategoryName());
            userWatchProgressResponse.setLevel(p.getCourse().getLevel());
            userWatchProgressResponse.setAuthor(p.getCourse().getAuthor());
            userWatchProgressResponse.setRating(p.getCourse().getRating());
            userWatchProgressResponse.setModul(p.getCourse().getModul());
            userWatchProgressResponse.setTime(p.getCourse().getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String outputDate = dateFormat.format(p.getCourse().getPublish());
            userWatchProgressResponse.setPublish(outputDate);
            int count = 0;

            for (UserVideo userVideo1 : userVideo.get()) {
                if (userVideo1.getCourse() == p.getCourse()) {
                    count += 1;
                }
            }

            int videoSize = p.getCourse().getChapters().stream()
                    .mapToInt(chapter -> chapter.getVideos().size())
                    .sum();
            int progressTotal = videoSize > 0 ? (count * 100) / videoSize : 0;
            userWatchProgressResponse.setProgress(progressTotal);

            return userWatchProgressResponse;
        }).collect(Collectors.toList());

        List<UserWatchProgressResponse> filteredList = responses.stream()
                .filter(userWatchProgressResponse -> userWatchProgressResponse.getProgress() == 100)
                .collect(Collectors.toList());

        response.setData(filteredList);
        response.setMessage("success get data");
        response.setErrors(false);
        return response;
    }

    @Override
    public ResponseHandling<List<CourseGetResponse>> filter(Boolean isNewest, Boolean isPopular, ClassType classType,
                                                            List<String> category, List<Level> level){
        ResponseHandling<List<CourseGetResponse>> response = new ResponseHandling<>();

        CourseFilterRequest courseFilterRequest = new CourseFilterRequest();
        courseFilterRequest.setIsNewest(isNewest);
        courseFilterRequest.setIsPopular(isPopular);
        courseFilterRequest.setCategories(category);
        courseFilterRequest.setLevels(level);

        List<Category> categories = category.stream()
                .map(categoryRepository::findByCategoryName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        List<Course> courseList = new ArrayList<>();

        if (!courseFilterRequest.getIsNewest() && !courseFilterRequest.getIsPopular()
                && courseFilterRequest.getCategories().isEmpty() && courseFilterRequest.getLevels().isEmpty()) {
            courseList = courseRepository.findAll();
        }else if (classType == null){
            courseList = courseRepository.findFilteredCoursesWithoutclasstype(categories, courseFilterRequest.getLevels(),
                    courseFilterRequest.getIsPopular(), courseFilterRequest.getIsNewest());
        }else {
            courseList = courseRepository.findFilteredCourses(categories, courseFilterRequest.getLevels(),
                    classType, courseFilterRequest.getIsPopular(), courseFilterRequest.getIsNewest());
        }

        if (courseList.isEmpty() || courseList == null){
            response.setMessage("Data course null");
            response.setErrors(false);
            return response;
        }

        List<CourseGetResponse> courseGetResponses = courseList.stream().map((p)->{
            CourseGetResponse courseGetResponse = new CourseGetResponse();
            courseGetResponse.setKodeKelas(p.getCourseCode());
            courseGetResponse.setNamaKelas(p.getClassName());
            courseGetResponse.setImageUrl(p.getPictureUrl());
            courseGetResponse.setKategori(p.getCategories().getCategoryName());
            courseGetResponse.setLevel(p.getLevel());
            courseGetResponse.setHarga(p.getPrice());
            courseGetResponse.setAuthor(p.getAuthor());
            courseGetResponse.setRating(p.getRating());
            courseGetResponse.setTime(p.getTime());
            courseGetResponse.setModul(p.getModul());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String outputDate = dateFormat.format(p.getPublish());
            courseGetResponse.setPublish(outputDate);
            courseGetResponse.setTipeKelas(p.getClassType());
            return courseGetResponse;
        }) .collect(Collectors.toList());
        response.setData(courseGetResponses);
        response.setMessage("success get course");
        response.setErrors(false);
        return response;
    }

    @Override
    public ResponseHandling<List<PaymentStatusResponse>> dashboardFilter(Boolean isOldest, Boolean isAlreadyPaid, Boolean isNoPaid, List<CardType> paymentMethod, List<String> category, Integer page) {
        ResponseHandling<List<PaymentStatusResponse>> response = new ResponseHandling<>();

        List<Order> orders;
        if (isOldest == null || isOldest == false && isAlreadyPaid == null || isAlreadyPaid == false && isNoPaid == null ||
                isNoPaid == false && paymentMethod == null && category == null){
            if (page == null) {
                orders = orderRepository.findAll();
            } else {
                Pageable pageable = PageRequest.of(page, 10);
                Page<Order> orderpage = orderRepository.findAll(pageable);
                orders = orderpage.getContent();
            }
        }else if (page != null){

            List<Category> categories = category.stream()
                    .map(categoryRepository::findByCategoryName)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
            Pageable pageable = PageRequest.of(page, 10);
            Page<Order> orderpage = orderRepository.findDashboardFilterPage(isOldest, isAlreadyPaid, isNoPaid, paymentMethod, categories, pageable);
            orders = orderpage.getContent();
        } else {
            List<Category> categories = category.stream()
                    .map(categoryRepository::findByCategoryName)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
            orders = orderRepository.findDashboardFilter(isOldest, isAlreadyPaid, isNoPaid, paymentMethod, categories);
        }

        List<PaymentStatusResponse> paymentStatusResponses = orders.stream().map(p -> {
            PaymentStatusResponse paymentStatusResponse = new PaymentStatusResponse();
            paymentStatusResponse.setId(p.getUser().getNama());
            paymentStatusResponse.setKategori(p.getCourse().getCategories().getCategoryName());
            paymentStatusResponse.setKelas(p.getCourse().getClassName());
            paymentStatusResponse.setStatus(p.getCompletePaid() ? "SUDAH BAYAR" : "BELUM BAYAR");

            if (p.getPaymentMethod() != null) {
                if (CardType.BANK_TRANSFER.equals(p.getPaymentMethod())) {
                    paymentStatusResponse.setMetodePembayaran("Transfer bank");
                } else if (CardType.CREDIT_CARD.equals(p.getPaymentMethod())) {
                    paymentStatusResponse.setMetodePembayaran("Credit card");
                } else {
                    paymentStatusResponse.setMetodePembayaran("-");
                }
            } else {
                paymentStatusResponse.setMetodePembayaran("-");
            }

            SimpleDateFormat outputFormat = new SimpleDateFormat("d MMM, yyyy 'at' h:mm a", Locale.ENGLISH);
            Date payTime = p.getPayTime();

            if (payTime != null) {
                String outputDate = outputFormat.format(payTime);
                paymentStatusResponse.setTanggalBayar(outputDate.isEmpty() ? "-" : outputDate);
            } else {
                paymentStatusResponse.setTanggalBayar("-");
            }

            return paymentStatusResponse;
        }).collect(Collectors.toList());

        if (paymentStatusResponses.isEmpty()){
            response.setMessage("cant get data");
            response.setErrors(true);
            return response;
        }

        response.setData(paymentStatusResponses);
        response.setMessage("success get data");
        response.setErrors(false);

        return response;
    }

    @Override
    public ResponseHandling<DashboardResponse> getActivedashboard() {
        ResponseHandling<DashboardResponse> response = new ResponseHandling<>();
        List<User> user = userRepository.findAll();
        List<Course> courses = courseRepository.findAll();
        List<Course> premiumCourse = courseRepository.findPremiumCourses();
        DashboardResponse dashboardResponse = new DashboardResponse();
        dashboardResponse.setActiveUser(user.size());
        dashboardResponse.setActiveClass(courses.size());
        dashboardResponse.setPremiumClass(premiumCourse.size());

        response.setData(dashboardResponse);
        response.setMessage("success get data");
        response.setErrors(false);

        return response;
    }

    @Override
    public ResponseHandling<List<PaymentStatusResponse>> searchDashboard(String keyword, Integer page) {
        ResponseHandling<List<PaymentStatusResponse>> response = new ResponseHandling<>();

        List<Order> orders;

        if (page == null) {
            orders = orderRepository.findByOrderAndClassname(keyword);
        } else {
            Pageable pageable = PageRequest.of(page, 10);
            Page<Order> orderpage = orderRepository.findByOrderAndClassname(keyword, pageable);
            orders = orderpage.getContent();
        }
        List<PaymentStatusResponse> paymentStatusResponses = orders.parallelStream().map(p -> {
            PaymentStatusResponse paymentStatusResponse = new PaymentStatusResponse();
            paymentStatusResponse.setId(p.getUser().getNama());
            paymentStatusResponse.setKategori(p.getCourse().getCategories().getCategoryName());
            paymentStatusResponse.setKelas(p.getCourse().getClassName());
            paymentStatusResponse.setStatus(p.getCompletePaid() ? "SUDAH BAYAR" : "BELUM BAYAR");

            if (p.getPaymentMethod() != null) {
                if (CardType.BANK_TRANSFER.equals(p.getPaymentMethod())) {
                    paymentStatusResponse.setMetodePembayaran("Transfer bank");
                } else if (CardType.CREDIT_CARD.equals(p.getPaymentMethod())) {
                    paymentStatusResponse.setMetodePembayaran("Credit card");
                } else {
                    paymentStatusResponse.setMetodePembayaran("-");
                }
            } else {
                paymentStatusResponse.setMetodePembayaran("-");
            }

            SimpleDateFormat outputFormat = new SimpleDateFormat("d MMM, yyyy 'at' h:mm a", Locale.ENGLISH);
            Date payTime = p.getPayTime();

            if (payTime != null) {
                String outputDate = outputFormat.format(payTime);
                paymentStatusResponse.setTanggalBayar(outputDate.isEmpty() ? "-" : outputDate);
            } else {
                paymentStatusResponse.setTanggalBayar("-");
            }

            return paymentStatusResponse;
        }).collect(Collectors.toList());

        response.setData(paymentStatusResponses);
        response.setMessage("success get data");
        response.setErrors(false);

        return response;
    }

    @Override
    public ResponseHandling<List<ManageClassResponse>> searchManageClass(String keyword, Integer page) {
        ResponseHandling<List<ManageClassResponse>> response = new ResponseHandling<>();
        List<Course> course;

        if (page == null) {
            course = courseRepository.findByClassNameOrTeacherJPQL(keyword);
        } else {
            Pageable pageable = PageRequest.of(page, 10);
            Page<Course> coursePage = courseRepository.findByClassNameOrTeacherJPQLPage(keyword, pageable);
            course = coursePage.getContent();
        }

        List<ManageClassResponse> manageClassResponse = course.stream().map((p)->{
            ManageClassResponse manageClassResponse1 = new ManageClassResponse();
            manageClassResponse1.setKodeKelas(p.getCourseCode());
            manageClassResponse1.setKategori(p.getCategories().getCategoryName());
            manageClassResponse1.setNamaKelas(p.getClassName());
            if (ClassType.PREMIUM.equals(p.getClassType())){
                manageClassResponse1.setTipeKelas("PREMIUM");
            }else {
                manageClassResponse1.setTipeKelas("GRATIS");
            }
            if (Level.BEGINNER.equals(p.getLevel())){
                manageClassResponse1.setLevel("Beginner");
            }else if (Level.ADVANCED.equals(p.getLevel())){
                manageClassResponse1.setLevel("Advanced");
            }else {
                manageClassResponse1.setLevel("Intermediate");
            }
            manageClassResponse1.setHarga(p.getPrice());

            return manageClassResponse1;

        }).collect(Collectors.toList());

        response.setData(manageClassResponse);
        response.setMessage("get data success");
        response.setErrors(false);
        return response;
    }

    @Override
    public ResponseHandling<DeleteCourseResponse> deleteUserData(String coursecode) {
        ResponseHandling<DeleteCourseResponse> response = new ResponseHandling<>();
        Optional<Course> course = courseRepository.findByCourseCode(coursecode);
        if (!course.isPresent()){
            response.setMessage("Course not found with code " + coursecode);
            response.setErrors(true);
            return response;
        }

        course.get().getChapters().forEach(chapter -> {
            chapter.getVideos().forEach(video -> {
                Optional<UserVideo> userVideo = userVideoRepository.findByVideo(video);
                userVideo.ifPresent(v -> userVideoRepository.deleteById(v.getId()));
            });
        });

        course.get().getChapters().forEach(chapter -> {
            chapter.getVideos().forEach(video -> {
                Optional<Video> existingVideo = videoRepository.findByVideoCode(video.getVideoCode());
                existingVideo.ifPresent(v -> videoRepository.deleteById(v.getId()));
            });
        });

        course.get().getChapters().forEach(chapter -> {
            Optional<Chapter> existingChapter = chapterRepository.findById(chapter.getId());
            existingChapter.ifPresent(c -> chapterRepository.deleteById(c.getId()));
        });
        courseRepository.deleteById(course.get().getId());

        DeleteCourseResponse deleteCourseResponse = new DeleteCourseResponse();
        deleteCourseResponse.setCourseCode(coursecode);
        response.setData(deleteCourseResponse);
        response.setMessage("Success delete data");
        response.setErrors(false);
        return response;
    }

    @Override
    public ResponseHandling<List<ManageClassResponse>> getManageClass(Integer page) {
        ResponseHandling<List<ManageClassResponse>> response = new ResponseHandling<>();
        List<Course> courses;
        if (page == null) {
            courses = courseRepository.findAll();
        } else {
            Pageable pageable = PageRequest.of(page, 10);
            Page<Course> coursePage = courseRepository.findAll(pageable);
            courses = coursePage.getContent();
        }

        List<ManageClassResponse> manageClassResponses = courses.stream().map((p)->{
            ManageClassResponse manageClassResponse = new ManageClassResponse();
            manageClassResponse.setKodeKelas(p.getCourseCode());
            manageClassResponse.setKategori(p.getCategories().getCategoryName());
            manageClassResponse.setNamaKelas(p.getClassName());
            if (ClassType.PREMIUM.equals(p.getClassType())){
                manageClassResponse.setTipeKelas("PREMIUM");
            }else {
                manageClassResponse.setTipeKelas("GRATIS");
            }
            if (Level.BEGINNER.equals(p.getLevel())){
                manageClassResponse.setLevel("Beginner");
            }else if (Level.ADVANCED.equals(p.getLevel())){
                manageClassResponse.setLevel("Advanced");
            }else {
                manageClassResponse.setLevel("Intermediate");
            }
            manageClassResponse.setHarga(p.getPrice());

            return manageClassResponse;
        }).collect(Collectors.toList());

        response.setData(manageClassResponses);
        response.setMessage("get data success");
        response.setErrors(false);
        return response;
    }

    @Override
    public ResponseHandling<List<PaymentStatusResponse>> dashboard(Integer page) {
        ResponseHandling<List<PaymentStatusResponse>> response = new ResponseHandling<>();
        List<Order> orders;

        if (page == null) {
            orders = orderRepository.findAll();
        } else {
            Pageable pageable = PageRequest.of(page, 10);
            Page<Order> orderpage = orderRepository.findAll(pageable);
            orders = orderpage.getContent();
        }

        List<PaymentStatusResponse> paymentStatusResponses = orders.parallelStream().map(p -> {
            PaymentStatusResponse paymentStatusResponse = new PaymentStatusResponse();
            paymentStatusResponse.setId(p.getUser().getNama());
            paymentStatusResponse.setKategori(p.getCourse().getCategories().getCategoryName());
            paymentStatusResponse.setKelas(p.getCourse().getClassName());
            paymentStatusResponse.setStatus(p.getCompletePaid() ? "SUDAH BAYAR" : "BELUM BAYAR");

            if (p.getPaymentMethod() != null) {
                if (CardType.BANK_TRANSFER.equals(p.getPaymentMethod())) {
                    paymentStatusResponse.setMetodePembayaran("Transfer bank");
                } else if (CardType.CREDIT_CARD.equals(p.getPaymentMethod())) {
                    paymentStatusResponse.setMetodePembayaran("Credit card");
                } else {
                    paymentStatusResponse.setMetodePembayaran("-");
                }
            } else {
                paymentStatusResponse.setMetodePembayaran("-");
            }

            SimpleDateFormat outputFormat = new SimpleDateFormat("d MMM, yyyy 'at' h:mm a", Locale.ENGLISH);
            Date payTime = p.getPayTime();

            if (payTime != null) {
                String outputDate = outputFormat.format(payTime);
                paymentStatusResponse.setTanggalBayar(outputDate.isEmpty() ? "-" : outputDate);
            } else {
                paymentStatusResponse.setTanggalBayar("-");
            }

            return paymentStatusResponse;
        }).collect(Collectors.toList());

        response.setData(paymentStatusResponses);
        response.setMessage("success get data");
        response.setErrors(false);

        return response;
    }


    @Override
    public ResponseHandling<List<CourseGetResponse>> getPopularClass(String category) {
        ResponseHandling<List<CourseGetResponse>> response = new ResponseHandling<>();
        Optional<Category> categories = categoryRepository.findByCategoryName(category);
        if (!categories.isPresent()){
            List<Course> courses = courseRepository.findByRatingGreaterThanOrEqual();
            List<CourseGetResponse> courseGetResponses = courses.stream().map((p)->{
                CourseGetResponse courseGetResponse = new CourseGetResponse();
                courseGetResponse.setKodeKelas(p.getCourseCode());
                courseGetResponse.setNamaKelas(p.getClassName());
                courseGetResponse.setImageUrl(p.getPictureUrl());
                courseGetResponse.setKategori(p.getCategories().getCategoryName());
                courseGetResponse.setLevel(p.getLevel());
                courseGetResponse.setHarga(p.getPrice());
                courseGetResponse.setAuthor(p.getAuthor());
                courseGetResponse.setRating(p.getRating());
                courseGetResponse.setTime(p.getTime());
                courseGetResponse.setModul(p.getModul());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String outputDate = dateFormat.format(p.getPublish());
                courseGetResponse.setPublish(outputDate);
                courseGetResponse.setTipeKelas(p.getClassType());
                return courseGetResponse;
            }).collect(Collectors.toList());
            response.setData(courseGetResponses);
            response.setMessage("success get data");
            response.setErrors(false);
            return response;
        }

        //temukan course dari category
        List<Course> courseList = courseRepository.findByRatingAndCategory(categories.get());
        List<CourseGetResponse> courseGetResponses = courseList.stream().map((p)->{
            CourseGetResponse courseGetResponse = new CourseGetResponse();
            courseGetResponse.setKodeKelas(p.getCourseCode());
            courseGetResponse.setNamaKelas(p.getClassName());
            courseGetResponse.setImageUrl(p.getPictureUrl());
            courseGetResponse.setKategori(p.getCategories().getCategoryName());
            courseGetResponse.setLevel(p.getLevel());
            courseGetResponse.setHarga(p.getPrice());
            courseGetResponse.setAuthor(p.getAuthor());
            courseGetResponse.setRating(p.getRating());
            courseGetResponse.setTime(p.getTime());
            courseGetResponse.setModul(p.getModul());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String outputDate = dateFormat.format(p.getPublish());
            courseGetResponse.setPublish(outputDate);
            courseGetResponse.setTipeKelas(p.getClassType());
            return courseGetResponse;
        }).collect(Collectors.toList());

        response.setData(courseGetResponses);
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
