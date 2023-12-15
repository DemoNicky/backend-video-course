package com.binar.backendonlinecourseapp.DTO.Response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetChapterResponse {

    private String noChapter;

    private String judulChapter;

    private Integer time;

    private List<GetVideoResponse> getVideoResponses;

}
