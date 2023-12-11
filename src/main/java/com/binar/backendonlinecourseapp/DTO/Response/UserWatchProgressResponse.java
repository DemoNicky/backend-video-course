package com.binar.backendonlinecourseapp.DTO.Response;

import com.binar.backendonlinecourseapp.Entity.Enum.Level;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserWatchProgressResponse {

    private String kodeKelas;

    private String namaKelas;

    private String imageUrl;

    private String kategori;

    private Level level;

    private String author;

    private Double rating;

    private Integer modul;

    private Integer time;

    private String publish;

    private Integer progress;

}
