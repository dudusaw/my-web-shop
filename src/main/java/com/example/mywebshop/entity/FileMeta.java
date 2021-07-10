package com.example.mywebshop.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class FileMeta {

    @Id
    @GeneratedValue
    private Long id;

    private String path;

    private String originalFilename;

    public FileMeta(String path, String originalFilename) {
        this.path = path;
        this.originalFilename = originalFilename;
    }
}
