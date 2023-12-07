package com.binar.backendonlinecourseapp.DTO.Response;

import com.binar.backendonlinecourseapp.Entity.Level;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentHistoryResponse {

    private String orderCode;

    private String kodeKelas;

    private String namaKelas;

    private String imageUrl;

    private String kategori;

    private Level level;

    private String author;

    private Integer time;

    private Boolean completePaid;

}
