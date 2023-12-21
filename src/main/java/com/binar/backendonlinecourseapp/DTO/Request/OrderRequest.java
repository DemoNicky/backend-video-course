package com.binar.backendonlinecourseapp.DTO.Request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class OrderRequest {

    @NotBlank(message = "Course code tidak boleh kosong")
    private String courseCode;

}
