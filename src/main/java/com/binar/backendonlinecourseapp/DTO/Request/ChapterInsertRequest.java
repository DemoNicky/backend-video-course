package com.binar.backendonlinecourseapp.DTO.Request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class ChapterInsertRequest {

    @NotBlank(message = "Judul chapter tidak boleh kosong")
    private String chaptertitle;

    @Valid
    @Size(min = 1, message = "Setidaknya satu chapter harus diisi")
    private List<InsertVideoRequest> insertVideoRequests;

}
