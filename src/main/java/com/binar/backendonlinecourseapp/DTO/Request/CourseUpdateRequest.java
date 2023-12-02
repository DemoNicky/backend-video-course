package com.binar.backendonlinecourseapp.DTO.Request;

import com.binar.backendonlinecourseapp.Entity.ClassType;
import com.binar.backendonlinecourseapp.Entity.Level;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CourseUpdateRequest {

    private String kodeKelas;

    private ClassType tipeKelas;

    private Level level;

    private BigDecimal harga;

    private String materi;

    private String judulVideo;

    private String linkVideo;

    private Boolean isPremium;

    private String chapter;

}
