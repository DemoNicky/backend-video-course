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

    @Column(name = "video_code", length = 10, nullable = false, unique = true)
    private String videoCode;

    @Column(name = "video_title", length = 40, nullable = false)
    private String videoTitle;

    @Column(name = "video_link", nullable = false)
    private String videoLink;

    private Boolean premium;

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

}
