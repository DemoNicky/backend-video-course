package com.binar.backendonlinecourseapp.DTO.Response;

import com.binar.backendonlinecourseapp.Entity.ClassType;
import com.binar.backendonlinecourseapp.Entity.Level;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class GetPremiumClassResponse {

    private String kodeKelas;

    private String namaKelas;

    private String kategori;

    private Level level;

    private BigDecimal harga;

    private String author;

    private ClassType tipeKelas;

    private Double rating;

    private Integer modul;

    private String publish;

}
