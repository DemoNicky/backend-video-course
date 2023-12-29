package com.binar.backendonlinecourseapp.Repository;

import com.binar.backendonlinecourseapp.DTO.Response.GetChapterResponse;
import com.binar.backendonlinecourseapp.Entity.Chapter;
import com.binar.backendonlinecourseapp.Entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, String> {

    Optional<Chapter> findByChaptertitle(String title);

    @Query("SELECT c FROM Chapter c WHERE c.course = :course ORDER BY c.number ASC")
    List<Chapter> findByCourseOrderByNumberAsc(@Param("course") Course course);

}
