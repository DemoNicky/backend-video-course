package com.binar.backendonlinecourseapp.DTO.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResendResponse {

    private String email;

    private String token;
}
