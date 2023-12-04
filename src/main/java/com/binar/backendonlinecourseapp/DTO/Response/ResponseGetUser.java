package com.binar.backendonlinecourseapp.DTO.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseGetUser {

    private String urlPicture;

    private String nama;

    private String email;

    private String telp;

    private String negara;

    private String kota;

}
