package com.skilltrack.backend.repository;

import com.skilltrack.backend.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> { }
