package com.binar.backendonlinecourseapp.DTO.Response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetUserResponse {

    private String nama;

    private boolean active;

}
