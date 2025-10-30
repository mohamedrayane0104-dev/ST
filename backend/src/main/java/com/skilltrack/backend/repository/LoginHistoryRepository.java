package com.skilltrack.backend.repository;

import com.skilltrack.backend.model.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> { }
