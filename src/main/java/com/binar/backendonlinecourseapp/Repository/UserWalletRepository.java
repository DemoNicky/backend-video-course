package com.binar.backendonlinecourseapp.Repository;

import com.binar.backendonlinecourseapp.Entity.UserWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserWalletRepository extends JpaRepository<UserWallet, String> {

    Optional<UserWallet> findByCardNumber(String cardNumber);

}
