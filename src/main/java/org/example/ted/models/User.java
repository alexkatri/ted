package org.example.ted.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(name = "email", nullable = false, length = 60)
    private String email;

    @Column(name = "name", length = 45)
    private String name;

    @Column(name = "surname", length = 45)
    private String surname;

    @Column(name = "password", nullable = false, length = 1024)
    private String password;

    @Column(name = "phone_number", length = 45)
    private String phoneNumber;

    @OneToMany(mappedBy = "createdBy")
    private Set<Advertisement> advertisements = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "request_users")
    private Set<Advertisement> advertisement_requests = new LinkedHashSet<>();

    @OneToMany(mappedBy = "createdBy")
    private Set<Article> articles = new LinkedHashSet<>();

    @OneToMany(mappedBy = "createdBy")
    private Set<Comment> comments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user1")
    private Set<Connection> connection1 = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user2")
    private Set<Connection> connection2 = new LinkedHashSet<>();

    @OneToMany(mappedBy = "createdBy")
    private Set<Like> likes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Notification> notification1 = new LinkedHashSet<>();

    @OneToMany(mappedBy = "friend")
    private Set<Notification> notification2 = new LinkedHashSet<>();

    @OneToOne(mappedBy = "createdBy")
    private Profile profile;

    @OneToMany(mappedBy = "createdBy")
    private Set<Request> request1 = new LinkedHashSet<>();

    @OneToMany(mappedBy = "toUser")
    private Set<Request> request2 = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "users")
    private Set<Role> roles = new LinkedHashSet<>();

    @OneToOne(mappedBy = "user")
    private Image image;

    public User() {}
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, String name, String surname, String phoneNumber) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }
}