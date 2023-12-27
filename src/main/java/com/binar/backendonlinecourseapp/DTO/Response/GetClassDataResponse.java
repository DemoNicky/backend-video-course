package com.binar.backendonlinecourseapp.DTO.Response;

import com.binar.backendonlinecourseapp.DTO.Request.ChapterInsertRequest;
import com.binar.backendonlinecourseapp.Entity.Enum.ClassType;
import com.binar.backendonlinecourseapp.Entity.Enum.Level;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class GetClassDataResponse {

    private String namaKelas;

    private String kategori;

    private ClassType tipeKelas;

    private Level level;

    private BigDecimal harga;

    private String materi;

    private List<ChapterResponse> chapterResponses;

}
