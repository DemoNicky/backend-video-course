package com.binar.backendonlinecourseapp.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "tb_course")
public class Course {

    @Id
    @GenericGenerator(strategy = "uuid2", name = "uuid")
    @GeneratedValue(generator = "uuid")
    private String id;

    @Column(name = "course_code", length = 10, nullable = false, unique = true)
    private String courseCode;

    @Column(name = "class_name", length = 40, nullable = false, unique = true)
    private String className;

    @Enumerated(EnumType.STRING)
    private Level level;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "teacher", nullable = false, length = 40)
    private String author;

    @Column(name = "materi", nullable = false, length = 500)
    private String materi;

    @Max(value = 5, message = "Rating cannot be greater than 5.0")
    private Double rating;

    private Integer modul;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date publish;

    @Enumerated(EnumType.STRING)
    private ClassType classType;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Order> orders;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false, referencedColumnName = "id")
    private Category categories;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
    private List<Video> videos;

}
