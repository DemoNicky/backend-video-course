package com.binar.backendonlinecourseapp.Controller;

import com.binar.backendonlinecourseapp.DTO.Response.ResponseCreateCategory;
import com.binar.backendonlinecourseapp.DTO.Response.ResponseDeleteCategory;
import com.binar.backendonlinecourseapp.DTO.Response.ResponseHandling;
import com.binar.backendonlinecourseapp.DTO.Response.UpdateDataResponse;
import com.binar.backendonlinecourseapp.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping(
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<ResponseCreateCategory>>createCategory(@RequestParam String categoryName){
        ResponseHandling<ResponseCreateCategory> response = categoryService.createCategory(categoryName);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @DeleteMapping (
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<ResponseDeleteCategory>> deleteCategoryById(@RequestParam Long Id){
        ResponseHandling<ResponseDeleteCategory> response = categoryService.deleteCategoryById(Id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



}
