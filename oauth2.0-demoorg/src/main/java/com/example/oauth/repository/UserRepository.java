package com.example.oauth.repository;


import com.example.oauth.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByName(final String name);

//    @Query(value = "SELECT u.id, u.name, u.email, u.role, rs.scopes " +
//            "FROM users u " +
//            "LEFT JOIN (" +
//            "SELECT GROUP_CONCAT(scope) as scopes, role FROM role_scope_relationship GROUP BY role" +
//            ") rs ON u.role=rs.role " +
//            "WHERE u.email=?1 LIMIT 1", nativeQuery = true)
//    User getUserDetail1(String email);

    User findByEmail(final String email);

    User findById(final int id);
    Page<User> findAll(Pageable pageable);

}