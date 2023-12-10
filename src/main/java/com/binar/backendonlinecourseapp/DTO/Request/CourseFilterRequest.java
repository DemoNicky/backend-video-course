package com.binar.backendonlinecourseapp.DTO.Request;

import com.binar.backendonlinecourseapp.Entity.Level;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class CourseFilterRequest {

    @NotNull(message = "isNewest cannot be null")
    private Boolean isNewest;

    @NotNull(message = "isPopular cannot be null")
    private Boolean isPopular;

    private List<CategoriesFilterRequest> categories;

    private List<Level> levels;
}
