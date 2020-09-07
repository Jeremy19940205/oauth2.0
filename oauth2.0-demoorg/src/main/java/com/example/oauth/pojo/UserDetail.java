package com.example.oauth.pojo;

import com.sun.istack.Nullable;

import java.io.Serializable;

public class UserDetail implements Serializable {

    private int id = -1;
    private String name = "";
    private String email= "";
    private String role = "";
    private String scopes = "";

    public UserDetail() {}

    public UserDetail(int id,
                      String name,
                      String email,
                      String role,
                      String scopes) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.scopes = scopes;
    }

    public UserDetail(int id) {
        this.id = id;
    }

    public UserDetail(int id,
                      @Nullable String email) {
        this.id = id;
        this.email = email;
    }

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
