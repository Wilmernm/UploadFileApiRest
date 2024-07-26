package com.hblabs.uploadFile.repository;

import com.hblabs.uploadFile.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product, Long>{
    
}
