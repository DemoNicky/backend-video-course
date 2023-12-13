package com.binar.backendonlinecourseapp.Entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Data
@Table(name = "tb_user_video")
public class UserVideo {

    @Id
    @GenericGenerator(strategy = "uuid2", name = "uuid")
    @GeneratedValue(generator = "uuid")
    private String id;

    private Boolean isWatched;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "video_id", referencedColumnName = "id")
    private Video video;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;

}
