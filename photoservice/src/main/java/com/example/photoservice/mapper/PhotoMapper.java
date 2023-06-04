package com.example.photoservice.mapper;

import com.example.photoservice.dto.PhotoRequest;
import com.example.photoservice.dto.PhotoResponse;
import com.example.photoservice.entity.PhotoEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PhotoMapper {
    PhotoEntity map(PhotoRequest request);
    @InheritInverseConfiguration
    PhotoResponse map(PhotoEntity entity);
}
