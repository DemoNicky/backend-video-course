package com.binar.backendonlinecourseapp.DTO.Request;

import com.binar.backendonlinecourseapp.Entity.Enum.ClassType;
import com.binar.backendonlinecourseapp.Entity.Enum.Level;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CourseCreateRequest {

    private String namaKelas;

    private String kategori;

    private String kodeKelas;

    private ClassType tipeKelas;

    private Level level;

    private BigDecimal harga;

    private String materi;

    private List<ChapterInsertRequest> chapterInsertRequests;

}
