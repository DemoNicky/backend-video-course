package com.binar.backendonlinecourseapp.Repository;

import com.binar.backendonlinecourseapp.Entity.Course;
import com.binar.backendonlinecourseapp.Entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, String> {

    Optional<Video> findByVideoCode(String code);

}
