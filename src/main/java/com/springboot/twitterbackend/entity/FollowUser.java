package com.springboot.twitterbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "follow_user")
public class FollowUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followedBy", nullable = false)
    private User followedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followedTo", nullable = false)
    private User followedTo;

    @Column(name = "created_at")
    private Date created_at = new Date();

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getFollowedBy() {
        return followedBy;
    }

    public void setFollowedBy(User followedBy) {
        this.followedBy = followedBy;
    }

    public User getFollowedTo() {
        return followedTo;
    }

    public void setFollowedTo(User followedTo) {
        this.followedTo = followedTo;
    }

}