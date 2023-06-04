package com.example.photoservice.repository;

import com.example.photoservice.entity.PhotoEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PhotoRepository extends R2dbcRepository<PhotoEntity, UUID> {
}
