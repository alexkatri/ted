package org.example.ted.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "email", nullable = false, length = 60)
    private String email;

    @Column(name = "password", nullable = false, length = 1024)
    private String password;

    @Column(name = "name", length = 45)
    private String name;

    @Column(name = "surname", length = 45)
    private String surname;

    @Column(name = "phone_number", length = 45)
    private String phoneNumber;

    @ManyToMany(mappedBy = "request_users")
    @JsonIgnoreProperties("request_users")
    private Set<Advertisement> advertisement_requests = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties("user")
    private Set<Connection> connect = new LinkedHashSet<>();

    @OneToMany(mappedBy = "connection_user")
    @JsonIgnoreProperties("connection_user")
    private Set<Connection> connected = new LinkedHashSet<>();

    @OneToMany(mappedBy = "receiver")
    @JsonIgnoreProperties("receiver")
    private Set<Notification> received_notification = new LinkedHashSet<>();

    @OneToMany(mappedBy = "sender")
    @JsonIgnoreProperties("sender")
    private Set<Notification> sent_notification = new LinkedHashSet<>();

    @OneToOne(mappedBy = "createdBy")
    @JsonIgnoreProperties("createdBy")
    private Profile profile;

    @OneToMany(mappedBy = "sender")
    @JsonIgnoreProperties("sender")
    private Set<Request> sent_requests = new LinkedHashSet<>();

    @OneToMany(mappedBy = "receiver")
    @JsonIgnoreProperties("receiver")
    private Set<Request> received_requests = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    @JsonIgnoreProperties("users")
    private Set<Role> roles = new LinkedHashSet<>();

    @OneToOne(mappedBy = "user")
    @JsonIgnoreProperties("user")
    private Image image;

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnoreProperties("createdBy")
    private Set<Advertisement> advertisements = new LinkedHashSet<>();

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnoreProperties("createdBy")
    private Set<Article> articles = new LinkedHashSet<>();

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnoreProperties("createdBy")
    private Set<Comment> comments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnoreProperties("createdBy")
    private Set<Like> likes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties("user")
    private Set<UserAdvertisementRating> userAdvertisementRatings = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "user_skills",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "skills_id"))
    @JsonIgnoreProperties("users")
    private Set<Skill> skills = new LinkedHashSet<>();

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