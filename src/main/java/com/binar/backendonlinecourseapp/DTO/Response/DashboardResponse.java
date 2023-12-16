package com.binar.backendonlinecourseapp.DTO.Response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DashboardResponse {

    private Integer activeUser;

    private Integer activeClass;

    private Integer premiumClass;

    private List<PaymentStatusResponse> paymentStatusResponses;

}
