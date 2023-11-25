package com.binar.backendonlinecourseapp.Service.Impl;

import com.binar.backendonlinecourseapp.DTO.Response.ResponseCreateCategory;
import com.binar.backendonlinecourseapp.DTO.Response.ResponseUpdateCategory;
import com.binar.backendonlinecourseapp.DTO.Response.ResponseHandling;
import com.binar.backendonlinecourseapp.Entity.Category;
import com.binar.backendonlinecourseapp.Repository.CategoryRepository;
import com.binar.backendonlinecourseapp.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
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

    @Transactional
    @Override
    public ResponseHandling<ResponseUpdateCategory> updateCategory(String categoryName) {
        ResponseHandling<ResponseUpdateCategory> response = new ResponseHandling<>();

        Category category = new Category();
        Long categoryId = category.getId();
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

        if(optionalCategory.isPresent()) {
            Category newCategory = optionalCategory.get();
            category.setCategoryName(categoryName);
            categoryRepository.save(newCategory);

            ResponseUpdateCategory updateCategory = new ResponseUpdateCategory();
            updateCategory.setCategoryName(categoryName);
            response.setData(updateCategory);
            response.setMessage("success update category");
            response.setErrors(false);
            return response;
        }
        else {
            response.setMessage("failed update data");
            response.setErrors(true);
            return response;
        }

    }
}
