package com.binar.backendonlinecourseapp.Service.Impl;

import com.binar.backendonlinecourseapp.DTO.Response.CourseGetResponse;
import com.binar.backendonlinecourseapp.DTO.Response.ReseponseGetCategory;
import com.binar.backendonlinecourseapp.DTO.Response.ResponseCreateCategory;
import com.binar.backendonlinecourseapp.DTO.Response.ResponseHandling;
import com.binar.backendonlinecourseapp.Entity.Category;
import com.binar.backendonlinecourseapp.Repository.CategoryRepository;
import com.binar.backendonlinecourseapp.Service.CategoryService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    private final Cloudinary cloudinary;


    @Override
    public ResponseHandling<ResponseCreateCategory> createCategory(String categoryName) {
        ResponseHandling<ResponseCreateCategory> response = new ResponseHandling<>();

        Category category = new Category();
        category.setCategoryName(categoryName);
        categoryRepository.save(category);
        ResponseCreateCategory createCategory = new ResponseCreateCategory();
        createCategory.setCategoryName(categoryName);
        response.setData(createCategory);
        response.setMessage("success build category");
        response.setErrors(false);
        return response;
    }

    @Override
    public ResponseHandling<List<ReseponseGetCategory>> getByCategory() {
        ResponseHandling<List<ReseponseGetCategory>> response = new ResponseHandling<>();
        List<Category> category = categoryRepository.findAll();
        if (category == null || category.isEmpty()){
            response.setMessage("no category found");
            response.setErrors(true);
            return response;
        }

        List<ReseponseGetCategory> reseponseGetCategories = category.stream().map((p)->{
            ReseponseGetCategory reseponseGetCategory = new ReseponseGetCategory();
            reseponseGetCategory.setCategoryName(p.getCategoryName());
            reseponseGetCategory.setImageUrl(p.getPictureUrl());
            List<CourseGetResponse> courseGetResponses = p.getCourses().stream().map((x)->{
                CourseGetResponse courseGetResponse = new CourseGetResponse();
                courseGetResponse.setKodeKelas(x.getCourseCode());
                courseGetResponse.setNamaKelas(x.getClassName());
                courseGetResponse.setKategori(x.getCategories().getCategoryName());
                courseGetResponse.setLevel(x.getLevel());
                courseGetResponse.setHarga(x.getPrice());
                courseGetResponse.setAuthor(x.getAuthor());
                courseGetResponse.setTipeKelas(x.getClassType());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String outputDate = dateFormat.format(x.getPublish());
                courseGetResponse.setPublish(outputDate);
                return courseGetResponse;
            }).collect(Collectors.toList());
            reseponseGetCategory.setCourseGetResponses(courseGetResponses);
            return reseponseGetCategory;
        }).collect(Collectors.toList());

        response.setData(reseponseGetCategories);
        response.setMessage("Success found category");
        response.setErrors(false);
        return response;
    }

    @Override
    public String addPicture(MultipartFile multipartFile, Long id) throws IOException {
        Optional<Category> category = categoryRepository.findById(id);
        try {
            Map<?, ?> result = cloudinary.uploader().upload(multipartFile.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = result.get("url").toString();
            category.get().setPictureUrl(imageUrl);
            categoryRepository.save(category.get());
            return "success save iamge";
        }catch (IOException e) {
            e.printStackTrace();
        }

        return "failed save image";
    }

}
