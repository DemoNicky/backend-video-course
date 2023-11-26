package com.binar.backendonlinecourseapp.DTO.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetVideoResponse {

    private String videoCode;

    private String judulVideo;

    private String linkVideo;

    private boolean isPremium;

    private String chapter;

}
