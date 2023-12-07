package com.binar.backendonlinecourseapp.Repository;

import com.binar.backendonlinecourseapp.Entity.UserVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVideoRepository extends JpaRepository<UserVideo, String> {
}
