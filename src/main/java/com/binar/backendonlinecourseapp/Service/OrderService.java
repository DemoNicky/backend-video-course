package com.binar.backendonlinecourseapp.Service;

import com.binar.backendonlinecourseapp.DTO.Request.OrderRequest;
import com.binar.backendonlinecourseapp.DTO.Response.OrderResponse;
import com.binar.backendonlinecourseapp.DTO.Response.PaymentResponse;
import com.binar.backendonlinecourseapp.DTO.Response.ResponseHandling;
import com.binar.backendonlinecourseapp.Entity.Enum.CardType;

import java.text.ParseException;

public interface OrderService {

    ResponseHandling<OrderResponse> createOrder(OrderRequest orderRequest) throws ParseException;

    ResponseHandling<PaymentResponse> payCourse(String ordercode, String cardNumber, CardType cardType);
}
