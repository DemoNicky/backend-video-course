package com.binar.backendonlinecourseapp.DTO.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertVideoRequest {

    private String judulVideo;

    private String linkVideo;

    private Boolean isPremium;

    private String chapter;

}
