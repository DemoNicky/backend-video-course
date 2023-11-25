package com.binar.backendonlinecourseapp.DTO.Response;

import com.binar.backendonlinecourseapp.Entity.ClassType;
import com.binar.backendonlinecourseapp.Entity.Level;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CourseGetResponse {

    private String kodeKelas;

    private String namaKelas;

    private String kategori;

    private Level level;

    private BigDecimal harga;

    private String teacher;

    private ClassType tipeKelas;

}
