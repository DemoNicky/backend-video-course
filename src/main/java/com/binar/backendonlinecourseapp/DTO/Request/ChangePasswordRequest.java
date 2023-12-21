package com.binar.backendonlinecourseapp.DTO.Request;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class ChangePasswordRequest {

    @NotBlank
    private String oldpassword;

    @NotBlank
    @NotEmpty
    private String newpassword;

}
