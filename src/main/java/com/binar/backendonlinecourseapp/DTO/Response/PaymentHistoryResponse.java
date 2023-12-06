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

    private String kategori;

    private Level level;

    private String author;

    private Boolean completePaid;

}
