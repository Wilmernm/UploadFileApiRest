package com.hblabs.uploadFile.model;

import org.hibernate.annotations.UuidGenerator;

import com.hblabs.uploadFile.constant.Constants;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.*;

@Builder
@Entity
@Data@AllArgsConstructor
@NoArgsConstructor
@Table(name = Constants.Parameters.PRODUCT)
public class Product {

    @Id
    @UuidGenerator
    private String id;

    private String fileName;
    private String fileType;
    
    @Lob
    private byte[] data;

    public Product(String fileName, String fileType, byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
        }
    }

