package com.binar.backendonlinecourseapp.DTO.Response;

import com.binar.backendonlinecourseapp.DTO.Request.InsertVideoRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChapterUpdateRequest {

    private String chapterCode;

    private String chaptertitle;

    private List<UpdateVideoRequest> updateVideoRequests;

}
