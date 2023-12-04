package com.binar.backendonlinecourseapp.Entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "tb_admin")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String className;
    private boolean isActive;
    private ClassType classType;
}