package org.example.ted.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "profile")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "createdBy", nullable = false)
    private User createdBy;

    @Column(name = "experience", columnDefinition = "text")
    private String experience;

    @Column(name = "education", columnDefinition = "text")
    private String education;

    @Column(name = "skills", columnDefinition = "text")
    private String skills;

    @Column(name = "cv", columnDefinition = "text")
    private String cv;

}