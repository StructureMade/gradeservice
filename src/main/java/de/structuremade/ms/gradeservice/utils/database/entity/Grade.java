package de.structuremade.ms.gradeservice.utils.database.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
public class Grade {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private String id;

    @Column
    private int grade;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "student")
    private User student;

    @ManyToOne(targetEntity = LessonRoles.class)
    @JoinColumn(name = "lesson")
    private LessonRoles lesson;
}
