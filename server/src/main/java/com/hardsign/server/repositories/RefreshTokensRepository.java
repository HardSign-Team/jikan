package com.hardsign.server.repositories;

import com.hardsign.server.models.auth.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokensRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findRefreshTokenEntityByUserLoginAndActive(String userLogin, boolean active);
    Optional<RefreshTokenEntity> findRefreshTokenEntityByUserLoginAndActiveAndRefreshToken(String userLogin, boolean active, String refreshToken);
}
