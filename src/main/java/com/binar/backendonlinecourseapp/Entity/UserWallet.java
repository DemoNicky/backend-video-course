package com.binar.backendonlinecourseapp.Entity;

import com.binar.backendonlinecourseapp.Entity.Enum.CardType;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "user_wallet")
public class UserWallet {

    @Id
    @GenericGenerator(strategy = "uuid2", name = "uuid")
    @GeneratedValue(generator = "uuid")
    private String id;

    @Column(length = 16, unique = true, nullable = false)
    private String cardNumber;

    private BigDecimal userBalance;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CardType cardType;

}
