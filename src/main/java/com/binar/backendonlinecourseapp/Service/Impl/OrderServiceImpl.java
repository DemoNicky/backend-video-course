package com.binar.backendonlinecourseapp.Service.Impl;

import com.binar.backendonlinecourseapp.DTO.Request.OrderRequest;
import com.binar.backendonlinecourseapp.DTO.Response.OrderResponse;
import com.binar.backendonlinecourseapp.DTO.Response.ResponseHandling;
import com.binar.backendonlinecourseapp.Entity.Course;
import com.binar.backendonlinecourseapp.Entity.Order;
import com.binar.backendonlinecourseapp.Entity.User;
import com.binar.backendonlinecourseapp.Repository.CourseRepository;
import com.binar.backendonlinecourseapp.Repository.OrderRepository;
import com.binar.backendonlinecourseapp.Repository.UserRepository;
import com.binar.backendonlinecourseapp.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public ResponseHandling<OrderResponse> createOrder(OrderRequest orderRequest) throws ParseException {
        ResponseHandling<OrderResponse> response = new ResponseHandling<>();
        Optional<User> user = userRepository.findByEmail(getAuth());
        Optional<Course> course = courseRepository.findByCourseCode(orderRequest.getCourseCode());
        if (!course.isPresent()){
            response.setMessage("cant create order because order code is invalid man!!!");
            response.setErrors(true);
            return response;
        }else if (orderRepository.findOrdersByUserAndCourse(user.get(), course.get()).isPresent()){
            response.setMessage("cant create order because you already order it bro!!!");
            response.setErrors(true);
            return response;
        }
        Order order = new Order();
        order.setOrderCode(getUUIDCode());

        order.setOrderDate(new Date());

        order.setExpired(Date.from(Instant.now().plusSeconds(24 * 60 * 60)));

        order.setCompletePaid(false);
        order.setCourse(course.get());
        order.setUser(user.get());
        orderRepository.save(order);

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setCouresCode(course.get().getCourseCode());
        orderResponse.setOrderCode(order.getOrderCode());

        response.setData(orderResponse);
        response.setMessage("success create order");
        response.setErrors(false);

        return response;
    }

    private String getAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return email;
    }

    private String getUUIDCode() {
        UUID uuid = UUID.randomUUID();
        String kode = uuid.toString().substring(0, 8);
        return kode;
    }


}
