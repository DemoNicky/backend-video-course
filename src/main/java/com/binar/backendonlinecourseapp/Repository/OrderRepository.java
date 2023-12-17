package com.binar.backendonlinecourseapp.Repository;

import com.binar.backendonlinecourseapp.Entity.Category;
import com.binar.backendonlinecourseapp.Entity.Course;
import com.binar.backendonlinecourseapp.Entity.Enum.CardType;
import com.binar.backendonlinecourseapp.Entity.Enum.ClassType;
import com.binar.backendonlinecourseapp.Entity.Enum.Level;
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

    @Query("SELECT o FROM Order o WHERE o.user = :user")
    Optional<Page<Order>> findOrdersByUser(@Param("user") User user, Pageable pageable);

    List<Order> findByUserAndCourseIn(User user, List<Course> courses);

    Page<Order> findByUserAndCourseIn(User user, List<Course> courses, Pageable pageable);

    Page<Order> findAll(Pageable pageable);

//    @Query("SELECT o FROM Order o " +
//            "JOIN o.course c " +
//            "WHERE (COALESCE(:categoryList, NULL) IS NULL OR c.categories IN :categoryList) " +
//            "AND (COALESCE(:levelList, NULL) IS NULL OR c.level IN :levelList) " +
//            "ORDER BY " +
//            "CASE WHEN :orderByRating = true THEN c.rating END DESC, " +
//            "CASE WHEN :orderByPublish = true THEN c.publish END DESC")
//    List<Order> findFilteredOrders(
//            @Param("categoryList") List<Category> categoryList,
//            @Param("levelList") List<Level> levelList,
//            @Param("orderByRating") boolean orderByRating,
//            @Param("orderByPublish") boolean orderByPublish);

    @Query("SELECT o FROM Order o " +
            "WHERE o.user = :user " +
            "AND (COALESCE(:categoryList, NULL) IS NULL OR o.course.categories IN :categoryList) " +
            "AND (COALESCE(:levelList, NULL) IS NULL OR o.course.level IN :levelList) " +
            "ORDER BY " +
            "CASE WHEN :orderByRating = true THEN o.course.rating END DESC, " +
            "CASE WHEN :orderByPublish = true THEN o.course.publish END DESC")
    List<Order> findFilteredOrdersByUser(
            @Param("user") User user,
            @Param("categoryList") List<Category> categoryList,
            @Param("levelList") List<Level> levelList,
            @Param("orderByRating") boolean orderByRating,
            @Param("orderByPublish") boolean orderByPublish);

    @Query("SELECT o FROM Order o " +
            "LEFT JOIN o.course.categories cat " +
            "WHERE (COALESCE(:categoryList, NULL) IS NULL OR cat IN :categoryList) " +
            "AND (COALESCE(:paymentMethod, NULL) IS NULL OR o.paymentMethod IN :paymentMethod) " +
            "ORDER BY " +
            "CASE WHEN :isAlreadyPaid = true THEN " +
            "          CASE WHEN o.completePaid = true THEN 0 ELSE 1 END " +
            "     WHEN :isNoPaid = true THEN " +
            "          CASE WHEN o.completePaid = false THEN 0 ELSE 1 END " +
            "     ELSE 1 END DESC, " +
            "CASE WHEN :isOldest = true THEN o.orderDate END DESC")
    List<Order> findDashboardFilter(
            @Param("isOldest") Boolean isOldest,
            @Param("isAlreadyPaid") Boolean isAlreadyPaid,
            @Param("isNoPaid") Boolean isNoPaid,
            @Param("paymentMethod") List<CardType> paymentMethod,
            @Param("categoryList") List<Category> categoryList);

    @Query("SELECT o FROM Order o " +
            "LEFT JOIN o.course.categories cat " +
            "WHERE (COALESCE(:categoryList, NULL) IS NULL OR cat IN :categoryList) " +
            "AND (COALESCE(:paymentMethod, NULL) IS NULL OR o.paymentMethod IN :paymentMethod) " +
            "ORDER BY " +
            "CASE WHEN :isNoPaid = true AND o.completePaid = false THEN 0 " +
            "     WHEN :isAlreadyPaid = true AND o.completePaid = true THEN 0 " +
            "     ELSE 1 END DESC, " +
            "CASE WHEN :isOldest = true THEN o.orderDate END DESC")
    Page<Order> findDashboardFilterPage(
            @Param("isOldest")Boolean isOldest,
            @Param("isAlreadyPaid")Boolean isAlreadyPaid,
            @Param("isNoPaid")Boolean isNoPaid,
            @Param("paymentMethod")List<CardType> paymentMethod,
            @Param("category")List<Category> categoryList,
            Pageable pageable);

}
