package com.binar.backendonlinecourseapp.DTO.Response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CourseUpdateResponse {

    private String namaKelas;

    private String kategori;

    private String kodeKelas;

    private BigDecimal harga;

    private String materi;

}
