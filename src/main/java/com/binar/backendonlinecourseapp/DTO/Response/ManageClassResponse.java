package com.binar.backendonlinecourseapp.DTO.Response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ManageClassResponse {

    private String kodeKelas;

    private String kategori;

    private String namaKelas;

    private String tipeKelas;

    private String level;

    private BigDecimal harga;

}
