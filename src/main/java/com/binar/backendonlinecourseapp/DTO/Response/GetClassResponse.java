package com.binar.backendonlinecourseapp.DTO.Response;

import com.binar.backendonlinecourseapp.Entity.Enum.ClassType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetClassResponse {
    private String className;
    private boolean isActive;
    private ClassType classType;
}