package com.binar.backendonlinecourseapp.Repository;

import com.binar.backendonlinecourseapp.Entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, String> {

    Page<Course> findAll(Pageable pageable);

    List<Course> findAll();

    Optional<Course> findByCourseCode(String code);

    Optional<Course> findByClassName(String code);

    @Query("SELECT c FROM Course c WHERE c.className LIKE %:searchTerm% OR c.author LIKE %:searchTerm%")
    Page<Course> findByClassNameOrTeacherJPQL(@Param("searchTerm") String searchTerm, Pageable pageable);
}
