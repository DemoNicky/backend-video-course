package com.binar.backendonlinecourseapp.Controller;

import com.binar.backendonlinecourseapp.DTO.Response.ReseponseGetCategory;
import com.binar.backendonlinecourseapp.DTO.Response.ResponseCreateCategory;
import com.binar.backendonlinecourseapp.DTO.Response.ResponseHandling;
import com.binar.backendonlinecourseapp.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ResponseHandling<ResponseCreateCategory>>createCategory(@RequestParam String categoryName){
        ResponseHandling<ResponseCreateCategory> response = categoryService.createCategory(categoryName);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

//    @PostMapping
//    public ResponseEntity<String>addPicture(@RequestHeader MultipartFile multipartFile){
//
//    }

    @GetMapping(
            path = "/{category}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<ReseponseGetCategory>>getByCategory(@PathVariable("category")String categoryName){
        ResponseHandling<ReseponseGetCategory> response = categoryService.getByCategory(categoryName);
        if (response.getData() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
