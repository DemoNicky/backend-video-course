package com.binar.backendonlinecourseapp.DTO.Response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateVideoRequest {

    private String videoCode;

    private String judulVideo;

    private String linkVideo;

    private Boolean isPremium;

}
