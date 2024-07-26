package com.hblabs.uploadFile.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.hblabs.uploadFile.constant.Constants;
import com.hblabs.uploadFile.model.Product;
import com.hblabs.uploadFile.repository.ProductRepo;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepo fileRepository;

    @Override
    public Product saveAttachment(MultipartFile file) throws Exception {
        
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new Exception(Constants.Exceptions.INVALID_FILENAME);
        }
        fileName = StringUtils.cleanPath(fileName);
        
        try {
            if(fileName.contains("..")) {
                throw new Exception(Constants.Exceptions.INVALID_PATH + fileName);
            }
            if (file.getBytes().length > (1024 * 1024)) {
                throw new Exception(Constants.Exceptions.EXCEED_LIMIT);
            }
            Product attachment = new Product(fileName, file.getContentType(), file.getBytes());
            return fileRepository.save(attachment);
        } catch (MaxUploadSizeExceededException e) {
            throw new MaxUploadSizeExceededException(file.getSize());
        } catch (Exception e) {
            throw new Exception(Constants.Exceptions.NOT_SAVED + fileName);
        }
    }

    @Override
    public void saveFiles(MultipartFile[] files) {
        Arrays.asList(files).forEach(file -> {
            try {
                saveAttachment(file);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Override
    public List<Product> getAllFiles(){
        return fileRepository.findAll();
    }
}
