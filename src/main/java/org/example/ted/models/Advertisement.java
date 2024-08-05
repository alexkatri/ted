package org.example.ted.models;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "advertisement", schema = "ted")
public class Advertisement {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

//    @Lob
    @Column(name = "text", nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "createdBy", nullable = false)
    private User createdBy;

    @ManyToMany
    @JoinTable(name = "advertisement_requests",
            joinColumns = @JoinColumn(name = "advertisement_id"),
            inverseJoinColumns = @JoinColumn(name = "request_user"))
    private Set<User> requestUsers = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Set<User> getRequestUsers() {
        return requestUsers;
    }

    public void setRequestUsers(Set<User> requestUsers) {
        this.requestUsers = requestUsers;
    }
}