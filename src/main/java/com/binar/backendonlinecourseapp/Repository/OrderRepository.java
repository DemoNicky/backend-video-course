package com.binar.backendonlinecourseapp.Repository;

import com.binar.backendonlinecourseapp.Entity.Course;
import com.binar.backendonlinecourseapp.Entity.Order;
import com.binar.backendonlinecourseapp.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    @Query("SELECT o FROM Order o WHERE o.user = :user AND o.course = :course")
    Optional<Order> findOrdersByUserAndCourse(@Param("user") User user, @Param("course") Course course);

    Optional<Order> findOrderByOrderCode(String orderCode);

    @Query("SELECT o FROM Order o WHERE o.user = :user")
    Optional<List<Order>> findOrdersByUser(@Param("user") User user);

    Page<Order> findAll(Pageable pageable);

//    Optional<List<Course>> findCourseByUser(User user);
}
