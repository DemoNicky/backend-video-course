package com.binar.backendonlinecourseapp.DTO.Request;

import com.binar.backendonlinecourseapp.Entity.Enum.ClassType;
import com.binar.backendonlinecourseapp.Entity.Enum.Level;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CourseCreateRequest {

    @NotBlank(message = "Nama kelas tidak boleh kosong")
    private String namaKelas;

    @NotBlank(message = "Kategori tidak boleh kosong")
    private String kategori;

    @NotBlank(message = "Kode kelas tidak boleh kosong")
    private String kodeKelas;

    private ClassType tipeKelas;

    private Level level;

    private BigDecimal harga;

    private String materi;

    private List<ChapterInsertRequest> chapterInsertRequests;

}
