package com.binar.backendonlinecourseapp.Repository;

import com.binar.backendonlinecourseapp.Entity.Token;
import com.binar.backendonlinecourseapp.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);

    Optional<Token> findByUserEmail(String email);

    Optional<Token> findByUser(User user);
}
