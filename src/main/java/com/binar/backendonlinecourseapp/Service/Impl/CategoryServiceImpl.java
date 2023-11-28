package com.binar.backendonlinecourseapp.Service.Impl;

import com.binar.backendonlinecourseapp.DTO.Response.CourseGetResponse;
import com.binar.backendonlinecourseapp.DTO.Response.ReseponseGetCategory;
import com.binar.backendonlinecourseapp.DTO.Response.ResponseCreateCategory;
import com.binar.backendonlinecourseapp.DTO.Response.ResponseHandling;
import com.binar.backendonlinecourseapp.Entity.Category;
import com.binar.backendonlinecourseapp.Repository.CategoryRepository;
import com.binar.backendonlinecourseapp.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;


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
    public ResponseHandling<ReseponseGetCategory> getByCategory(String categoryName) {
        ResponseHandling<ReseponseGetCategory> response = new ResponseHandling<>();
        Optional<Category> category = categoryRepository.findByCategoryName(categoryName);
        if (!category.isPresent()){
            response.setMessage("category Not Found");
            response.setErrors(true);
            return response;
        }
        Category categoryGet = category.get();
        ReseponseGetCategory reseponseGetCategory = new ReseponseGetCategory();
        reseponseGetCategory.setCategoryName(categoryGet.getCategoryName());
        List<CourseGetResponse> courseGetResponses = categoryGet.getCourses().stream().map((p)->{
            CourseGetResponse courseGetResponse = new CourseGetResponse();
            courseGetResponse.setKodeKelas(p.getCourseCode());
            courseGetResponse.setNamaKelas(p.getClassName());
            courseGetResponse.setKategori(p.getCategories().getCategoryName());
            courseGetResponse.setLevel(p.getLevel());
            courseGetResponse.setHarga(p.getPrice());
            courseGetResponse.setAuthor(p.getAuthor());
            courseGetResponse.setTipeKelas(p.getClassType());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String outputDate = dateFormat.format(p.getPublish());

            courseGetResponse.setPublish(outputDate);
            return courseGetResponse;
        }).collect(Collectors.toList());
        reseponseGetCategory.setCourseGetResponses(courseGetResponses);
        response.setData(reseponseGetCategory);
        response.setMessage("success get data");
        response.setErrors(false);
        return response;
    }

}
