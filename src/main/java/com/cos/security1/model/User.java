package com.cos.security1.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Getter @Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String password;
    private String username;
    private String email;
    private String role;

    private String provider;
    private String providerId;

    @CreationTimestamp
    private Timestamp createDate;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", provider='" + provider + '\'' +
                ", providerId='" + providerId + '\'' +
                ", createDate=" + createDate +
                '}';
    }

    @Builder
    public User(String password, String username, String email, String role,
                String provider, String providerId, Timestamp createDate) {
        this.id = id;
        this.password = password;
        this.username = username;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.createDate = createDate;
    }
}
