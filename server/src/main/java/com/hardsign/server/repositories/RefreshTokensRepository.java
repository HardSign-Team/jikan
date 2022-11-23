package com.hardsign.server.repositories;

import com.hardsign.server.models.auth.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokensRepository extends JpaRepository<RefreshTokenEntity, Long> {
}
