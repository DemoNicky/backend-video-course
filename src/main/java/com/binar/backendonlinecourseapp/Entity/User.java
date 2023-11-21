package com.binar.backendonlinecourseapp.Entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Table(name = "tb_user")
public class User {

    @Id
    @GenericGenerator(strategy = "uuid2", name = "uuid")
    @GeneratedValue(generator = "uuid")
    private String id;

    @Column(length = 100, nullable = false)
    private String nama;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 20, unique = true)
    private String telp;

    @Column(nullable = false, length = 60)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "USER_ROLE", joinColumns = {
            @JoinColumn(name = "USER_ID")
    },
            inverseJoinColumns = {
            @JoinColumn(name = "ROLE_ID")
            }
    )
    private Set<Role> roles;
}
