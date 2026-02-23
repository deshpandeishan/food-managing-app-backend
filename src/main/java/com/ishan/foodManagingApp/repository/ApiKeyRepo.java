package com.ishan.foodManagingApp.repository;

import com.ishan.foodManagingApp.model.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiKeyRepo extends JpaRepository<ApiKey, Integer> {
    Optional<ApiKey> findByApiKeyAndIsActive(String apiKey, boolean isActive);
}