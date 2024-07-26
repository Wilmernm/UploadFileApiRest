package com.hblabs.uploadFile.service;

import com.hblabs.uploadFile.model.Product;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    Product saveAttachment(MultipartFile file) throws Exception;
    void saveFiles(MultipartFile[] files) throws Exception;
    List<Product> getAllFiles();
}
