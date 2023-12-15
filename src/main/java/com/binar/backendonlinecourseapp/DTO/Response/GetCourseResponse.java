package com.binar.backendonlinecourseapp.DTO.Response;

import com.binar.backendonlinecourseapp.Entity.Enum.Level;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class GetCourseResponse {

    private String kodeKelas;

    private String namaKelas;

    private String kategori;

    private Level level;

    private BigDecimal harga;

    private String author;

    private Double rating;

    private Integer modul;

    private Integer time;

    private String deskripsi;

    private Integer progress;

    private List<GetChapterResponse> getChapterResponses;

}
