package com.binar.backendonlinecourseapp.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseHandling<T> {
    private T data;

    private String message;

    private Boolean errors;

}
