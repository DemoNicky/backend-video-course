package com.binar.backendonlinecourseapp.Repository;

import com.binar.backendonlinecourseapp.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByTelp(String telp);

    List<User> findByActiveTrue();

}
