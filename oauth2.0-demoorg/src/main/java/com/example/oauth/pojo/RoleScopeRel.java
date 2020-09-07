package com.example.oauth.pojo;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "role_scope_relationship")
public class RoleScopeRel implements Serializable {

    public RoleScopeRel() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "role", unique = false)
    private String role;

    @Column(name = "scope", unique = false)
    private String scope;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
