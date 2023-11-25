package com.binar.backendonlinecourseapp.Service;

import com.binar.backendonlinecourseapp.DTO.Request.UpdateDataRequest;
import com.binar.backendonlinecourseapp.DTO.Response.ResponseCreateCategory;
import com.binar.backendonlinecourseapp.DTO.Response.ResponseDeleteCategory;
import com.binar.backendonlinecourseapp.DTO.Response.ResponseHandling;
import com.binar.backendonlinecourseapp.DTO.Response.UpdateDataResponse;

public interface CategoryService {

    ResponseHandling<ResponseCreateCategory> createCategory(String categoryName);

    ResponseHandling<ResponseDeleteCategory> deleteCategoryById(Long id);

}
