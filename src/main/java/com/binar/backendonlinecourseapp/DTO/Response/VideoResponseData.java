package com.binar.backendonlinecourseapp.DTO.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoResponseData {

    private String videoCode;

    private String judulVideo;

    private String linkVideo;

    private Boolean isPremium;

}
