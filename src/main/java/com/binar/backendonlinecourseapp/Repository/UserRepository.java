package com.binar.backendonlinecourseapp.Repository;

import com.binar.backendonlinecourseapp.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByTelp(String telp);

    @Query("SELECT u FROM User u WHERE u.email = :identifier OR u.telp = :identifier")
    Optional<User> findUserByEmailOrtelp(@Param("identifier") String identifier);

}
