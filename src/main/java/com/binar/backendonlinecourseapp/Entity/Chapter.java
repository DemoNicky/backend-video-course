package com.binar.backendonlinecourseapp.Entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "tb_chapter")
public class Chapter {

    @Id
    @GenericGenerator(strategy = "uuid2", name = "uuid")
    @GeneratedValue(generator = "uuid")
    private String id;

    @Column(unique = true)
    private Integer number;

    @Column(length = 30, nullable = false)
    private String chaptertitle;

    private Integer chapterTime;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "chapter")
    private List<Video> videos;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

}
