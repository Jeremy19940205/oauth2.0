package com.example.oauth.repository;



import com.example.oauth.pojo.RoleScopeRel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ScopeRepository extends JpaRepository<RoleScopeRel, Long> {

    @Query(value = "SELECT " +
            "string_agg(scope, ',') as scopes " +
            "FROM role_scope_relationship WHERE role=?1 GROUP BY role", nativeQuery = true)
    String getScopes(String role);

}