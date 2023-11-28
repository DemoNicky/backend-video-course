package com.binar.backendonlinecourseapp.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "tb_order")
public class Order {

    @Id
    @GenericGenerator(strategy = "uuid2", name = "uuid")
    @GeneratedValue(generator = "uuid")
    private String id;

    @Column(name = "order_code", length = 10, nullable = false)
    private String orderCode;

    @Column(name = "order_time")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date orderDate;

    @Column(name = "pay_time")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date payTime;

    @Column(name = "expired")
    @JsonFormat(pattern = "dd-MM-yyyy hh:mm")
    private Date expired;

    private Boolean completePaid;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
