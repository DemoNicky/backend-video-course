package com.binar.backendonlinecourseapp.DTO.Request;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ChapterInsertRequest {

    private String chapterNumber;

    private String chaptertitle;

    private List<InsertVideoRequest> insertVideoRequests;

}
