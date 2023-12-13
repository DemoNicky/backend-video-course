package com.binar.backendonlinecourseapp.DTO.Response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderResponse {

    private String couresCode;

    private String orderCode;

    private BigDecimal harga;

    private BigDecimal ppn;

    private BigDecimal totalBayar;

    private String expiredDate;

}
