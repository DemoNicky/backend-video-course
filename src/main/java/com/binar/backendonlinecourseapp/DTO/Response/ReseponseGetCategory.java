package com.binar.backendonlinecourseapp.DTO.Response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReseponseGetCategory {

    private String categoryName;

    private List<CourseGetResponse> courseGetResponses;

}
