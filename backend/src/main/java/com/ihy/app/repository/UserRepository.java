package com.ihy.app.repository;

import com.ihy.app.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users,String> {

    boolean existsByEmail(String email);


    @Query("SELECT u FROM Users u WHERE u.isActive = 1")
    List<Users> findActiveUsers();

}
