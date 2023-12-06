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
import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ResponseHandling<ResponseCreateCategory>>createCategory(@RequestParam String categoryName){
        ResponseHandling<ResponseCreateCategory> response = categoryService.createCategory(categoryName);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(
            path = "/addpicture/{id}"
    )
    public ResponseEntity<String>addPicture(@RequestHeader MultipartFile multipartFile, @PathVariable("id")Long id) throws IOException {
        String response = categoryService.addPicture(multipartFile, id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(
            path = "/get",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<ReseponseGetCategory>>>getByCategory(){
        ResponseHandling<List<ReseponseGetCategory>> response = categoryService.getByCategory();
        if (response.getData() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
