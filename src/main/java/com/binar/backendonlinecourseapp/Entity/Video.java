package com.binar.backendonlinecourseapp.Entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Data
@Table(name = "tb_video")
public class Video {

    @Id
    @GenericGenerator(strategy = "uuid2", name = "uuid")
    @GeneratedValue(generator = "uuid")
    private String id;

    @Column(name = "video_code", length = 10, nullable = false)
    private String videoCode;

    @Column(name = "video_title", length = 40, nullable = false)
    private String videoTitle;

    @Column(name = "video_link", nullable = false)
    private String videoLink;

    @Column(name = "description", length = 500)
    private String description;

    private Boolean premium;

    @Column(name = "chapter", length = 20, nullable = false)
    private String Chapter;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

}
