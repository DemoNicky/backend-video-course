package com.binar.backendonlinecourseapp.Service;

import com.binar.backendonlinecourseapp.DTO.Response.ReseponseGetCategory;
import com.binar.backendonlinecourseapp.DTO.Response.ResponseCreateCategory;
import com.binar.backendonlinecourseapp.DTO.Response.ResponseDeleteCategory;
import com.binar.backendonlinecourseapp.DTO.Response.ResponseHandling;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CategoryService {

    ResponseHandling<ResponseCreateCategory> createCategory(String categoryName);

    ResponseHandling<List<ReseponseGetCategory>> getByCategory();

    String addPicture(MultipartFile multipartFile, Long id) throws IOException;

    ResponseHandling<ResponseDeleteCategory> deleteCategoryById(Long id);

}
