package com.binar.backendonlinecourseapp.Entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Table(name = "tb_course")
public class Course {

    @Id
    @GenericGenerator(strategy = "uuid2", name = "uuid")
    @GeneratedValue(generator = "uuid")
    private String id;

    @Column(name = "course_code", length = 10, nullable = false)
    private String courseCode;

    @Column(name = "class_name", length = 40, nullable = false)
    private String className;

    @Enumerated(EnumType.STRING)
    private Level level;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "teacher", nullable = false, length = 40)
    private String teacher;

    private Boolean premium;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Order> orders;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false, referencedColumnName = "id")
    private Category categories;

    @OneToMany
    private List<Video> videos;

}
