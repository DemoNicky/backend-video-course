package com.binar.backendonlinecourseapp.Service.Impl;

import com.binar.backendonlinecourseapp.DTO.Request.OrderRequest;
import com.binar.backendonlinecourseapp.DTO.Response.GetVideoResponse;
import com.binar.backendonlinecourseapp.DTO.Response.OrderResponse;
import com.binar.backendonlinecourseapp.DTO.Response.PaymentResponse;
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
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Transactional
    @Override
    public ResponseHandling<PaymentResponse> payCourse(String ordercode) {
        ResponseHandling<PaymentResponse> response = new ResponseHandling<>();

        if (!orderRepository.findOrderByOrderCode(ordercode).isPresent()){
            response.setMessage("payment is failed because code not found");
            response.setErrors(true);
            return response;
        }

        Optional<Order> order = orderRepository.findOrderByOrderCode(ordercode);
        Optional<Course> course = courseRepository.findById(order.get().getCourse().getId());
        if (order.get().getExpired().before(new Date())){
            orderRepository.deleteById(order.get().getId());
            response.setMessage("payment is failed because payment is expired");
            response.setErrors(true);
            return response;
        }
        Order order1 = order.get();
        order1.setPayTime(new Date());
        order1.setCompletePaid(true);
        orderRepository.save(order1);
        Course courseGet = course.get();
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setKodeKelas(courseGet.getCourseCode());
        paymentResponse.setNamaKelas(courseGet.getClassName());
        paymentResponse.setKategori(courseGet.getCategories().getCategoryName());
        paymentResponse.setLevel(courseGet.getLevel());
        paymentResponse.setHarga(courseGet.getPrice());
        paymentResponse.setAuthor(courseGet.getAuthor());
        paymentResponse.setDeskripsi(courseGet.getMateri());
        List<GetVideoResponse> getVideoResponses = courseGet.getVideos().stream().map((p)->{
            GetVideoResponse getVideoResponse = new GetVideoResponse();
            getVideoResponse.setVideoCode(p.getVideoCode());
            getVideoResponse.setJudulVideo(p.getVideoTitle());
            getVideoResponse.setLinkVideo(p.getVideoLink());
            getVideoResponse.setPremium(p.getPremium());
            getVideoResponse.setChapter(p.getChapter());
            return getVideoResponse;
        }).collect(Collectors.toList());
        paymentResponse.setGetVideoResponses(getVideoResponses);
        response.setData(paymentResponse);
        response.setMessage("payment success");
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
