package com.binar.backendonlinecourseapp.DTO.Request;

import com.binar.backendonlinecourseapp.Entity.Enum.ClassType;
import com.binar.backendonlinecourseapp.Entity.Enum.Level;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

    @NotNull(message = "Tipe kelas tidak boleh kosong")
    private ClassType tipeKelas;

    @NotNull(message = "Level tidak boleh kosong")
    private Level level;

    private BigDecimal harga;

    private String materi;

    @Valid
    @Size(min = 1, message = "Setidaknya satu chapter harus diisi")
    private List<ChapterInsertRequest> chapterInsertRequests;

}
