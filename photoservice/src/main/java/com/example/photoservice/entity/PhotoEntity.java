package com.example.photoservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "t_photo")
public class PhotoEntity {

    private UUID photoId;
    private Integer checksum;
    private String name;

}
