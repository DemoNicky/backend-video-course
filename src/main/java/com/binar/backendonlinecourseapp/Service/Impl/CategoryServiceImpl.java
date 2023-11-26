package com.binar.backendonlinecourseapp.Service.Impl;

import com.binar.backendonlinecourseapp.DTO.Response.ResponseCreateCategory;
import com.binar.backendonlinecourseapp.DTO.Response.ResponseHandling;
import com.binar.backendonlinecourseapp.Entity.Category;
import com.binar.backendonlinecourseapp.Repository.CategoryRepository;
import com.binar.backendonlinecourseapp.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
