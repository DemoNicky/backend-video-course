package com.binar.backendonlinecourseapp.Entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "tb_role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name", length = 20)
    private String roleName;
}
