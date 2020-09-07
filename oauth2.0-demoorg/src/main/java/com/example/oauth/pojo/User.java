package com.example.oauth.pojo;

import com.example.oauth.pojo.UserDetail;
import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "users")


public class User implements Serializable {

    public User() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", unique = false)
    private String name;

    @Column(name = "password", unique = false)
    private String password;

    @Column(name = "email", unique = false)
    private String email;

    @Column(name = "role", unique = false, nullable = true)
    private String role = "user";

    @Transient
    private String scopes = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getScopes() {
        return scopes;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes;
    }

}
