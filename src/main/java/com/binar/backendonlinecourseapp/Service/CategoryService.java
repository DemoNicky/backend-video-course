package com.binar.backendonlinecourseapp.Service;

import com.binar.backendonlinecourseapp.DTO.Response.ResponseUpdateCategory;
import com.binar.backendonlinecourseapp.DTO.Response.ResponseCreateCategory;
import com.binar.backendonlinecourseapp.DTO.Response.ResponseHandling;

public interface CategoryService {

    ResponseHandling<ResponseCreateCategory> createCategory(String categoryName);
    ResponseHandling<ResponseUpdateCategory> updateCategory(String categoryName);
}
