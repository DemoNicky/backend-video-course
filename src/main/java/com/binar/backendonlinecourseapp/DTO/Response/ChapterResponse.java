package com.binar.backendonlinecourseapp.DTO.Response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChapterResponse {

    private String chapterCode;

    private String chaptertitle;

    private List<VideoResponseData> videoResponseData;

}
