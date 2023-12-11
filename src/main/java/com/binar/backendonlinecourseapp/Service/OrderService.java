package com.binar.backendonlinecourseapp.Service;

import com.binar.backendonlinecourseapp.DTO.Request.OrderRequest;
import com.binar.backendonlinecourseapp.DTO.Response.*;

import java.text.ParseException;
import java.util.List;

public interface OrderService {

    ResponseHandling<OrderResponse> createOrder(OrderRequest orderRequest) throws ParseException;

    ResponseHandling<PaymentResponse> payCourse(String ordercode);

    ResponseHandling<List<GetUserResponse>> getActiveUser();
}
