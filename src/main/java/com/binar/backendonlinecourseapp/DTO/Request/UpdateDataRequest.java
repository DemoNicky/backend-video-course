package com.binar.backendonlinecourseapp.DTO.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDataRequest {

    private String nama;

    private String email;

    private String telp;

    private String oldpassword;

    private String newpassword;

}
