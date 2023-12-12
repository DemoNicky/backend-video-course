package com.binar.backendonlinecourseapp.DTO.Response;

import com.binar.backendonlinecourseapp.Entity.Enum.ClassType;
import com.binar.backendonlinecourseapp.Entity.Enum.Level;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CourseGetResponse {

    private String kodeKelas;

    private String namaKelas;

    private String imageUrl;

    private String kategori;

    private Level level;

    private BigDecimal harga;

    private String author;

    private ClassType tipeKelas;

    private Double rating;

    private Integer modul;

    private Integer time;

    private String publish;

}
