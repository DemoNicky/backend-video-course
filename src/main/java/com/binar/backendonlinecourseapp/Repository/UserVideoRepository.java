package com.binar.backendonlinecourseapp.Repository;

import com.binar.backendonlinecourseapp.Entity.User;
import com.binar.backendonlinecourseapp.Entity.UserVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserVideoRepository extends JpaRepository<UserVideo, String> {

    Optional<List<UserVideo>> findByUser(Optional<User> user);

}
