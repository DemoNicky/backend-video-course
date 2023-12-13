package com.binar.backendonlinecourseapp.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "tb_category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name", length = 30, nullable = false, unique = true)
    private String categoryName;

    private String pictureUrl;

    @OneToMany(mappedBy = "categories", cascade = CascadeType.ALL)
    private List<Course> courses;

}
