package com.binar.backendonlinecourseapp.DTO.Response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PaymentStatusResponse {

    private String id;

    private String kategori;

    private String kelas;

    private String status;

    private String metodePembayaran;

    private String tanggalBayar;

}
