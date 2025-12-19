package com.ihy.app.auth.repository;

import com.ihy.app.auth.entity.InvalidateToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidateTokenRepository extends JpaRepository<InvalidateToken, String> {

}
