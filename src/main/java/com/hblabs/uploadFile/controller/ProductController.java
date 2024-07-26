package com.hblabs.uploadFile.controller;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.hblabs.uploadFile.constant.Constants;
import com.hblabs.uploadFile.model.Product;
import com.hblabs.uploadFile.service.ProductService;
import com.hblabs.uploadFile.service.ResponseClass;

@RestController
@RequestMapping(Constants.Endpoints.PRINCIPAL_ROOT)
public class ProductController {
    @Autowired
    private ProductService fileService;

    protected String jsonResponse = "{\"succes\": \"true\"}";
    protected Product attachment = null;
    protected String downloadURL = "";

    //for uploading the SINGLE file to the database
    //Aun cuando se sube celda a celda, se sube un solo archivo y de preferencia el ultimo cargado.
    @PostMapping(Constants.Endpoints.SINGLE_CELL_FILE)
    public ResponseClass uploadFile(@RequestParam(Constants.ReqParameters.FILE) MultipartFile file) throws Exception {

        try {
            if (!file.isEmpty()) {
                attachment = fileService.saveAttachment(file);
                downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(Constants.Endpoints.DOWNLOAD_FILE)
                    .path(attachment.getId())
                    .toUriString();

                return new ResponseClass(attachment.getFileName(), 
                    downloadURL,
                    file.getContentType(),
                    file.getSize());
            } else {
                jsonResponse = Constants.Exceptions.INVALID_ATTACHMENT;
                throw new MultipartException(jsonResponse);
            }
        } catch (Exception e) {
            String message = e.getLocalizedMessage();
            jsonResponse = String.format("{\"Error\": \"%s\"}", message);
            throw new MultipartException(jsonResponse);
        }
    }

    // for uploading the MULTIPLE files to the database
    @PostMapping(Constants.Endpoints.MULTI_CELL_FILE)
    public List<ResponseClass> uploadingMultipleFiles(@RequestParam(Constants.ReqParameters.FILES) MultipartFile[] files) throws Exception {
        List<ResponseClass> responseList = new ArrayList<>();
        
        try {
            for(MultipartFile file: files) {
                Product attachment = fileService.saveAttachment(file);
                String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(Constants.Endpoints.DOWNLOAD_FILE)
                    .path(attachment.getId())
                    .toUriString();
                ResponseClass response = new ResponseClass(attachment.getFileName(),
                    downloadUrl,
                    file.getContentType(),
                    file.getSize());
                responseList.add(response);
            }
            return responseList;
            
        } catch (Exception e) {
            String message = e.getLocalizedMessage();
            jsonResponse = String.format("{\"Error\": \"%s\"}", message);
            throw new Exception(jsonResponse);
        }
    }
    
    // for retrieving all the files uploaded
    @GetMapping(Constants.Endpoints.GET_ALL_FILES)
    public ResponseEntity<List<ResponseClass>> getAllFiles() throws Exception{
        try {
            List<Product> products = fileService.getAllFiles();
            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
            }
            List<ResponseClass> responseClasses = products.stream().map(product -> {
                String downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(Constants.Endpoints.DOWNLOAD_FILE)
                    .path(product.getId())
                    .toUriString();
                return new ResponseClass(product.getFileName(),
                        downloadURL,
                        product.getFileType(),
                        product.getData().length);
            }).collect(Collectors.toList());
            
            return ResponseEntity.ok().body(responseClasses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.GONE).body(Collections.singletonList(new ResponseClass(null, null, null, 0L)));
        }
    }

    //for uploading the SINGLE file to the File System
    @PostMapping(Constants.Endpoints.SINGLE_FILE_SYSTEM)
    public ResponseEntity<ResponseClass> handleFileUpload(@RequestParam(Constants.ReqParameters.FILE) MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (!file.isEmpty()) {
            try {
                if (fileName == null) {
                    throw new Exception();
                }
                //Recordar cambiar el path o ruta de carpetas donde se guardaran los archivos en la memoria local
                file.transferTo(new File(Constants.Folder.ARCHIVE + fileName));
                String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(Constants.Endpoints.DOWNLOAD_FILE)
                    .path(fileName)
                    .toUriString();
                ResponseClass response = new ResponseClass(fileName,
                    downloadUrl,
                    file.getContentType(),
                    file.getSize());
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }  
    }
    
    //for uploading the MULTIPLE file to the File system
    @PostMapping(Constants.Endpoints.MULTIPLE_FILE_SYSTEM)
    public ResponseEntity<List<ResponseClass>> handleMultipleFilesUpload(@RequestParam(Constants.ReqParameters.FILES) MultipartFile[] files) {
        List<ResponseClass> responseList = new ArrayList<>();

        if (files.length >= 2) {
            for (MultipartFile file : files) {
                String fileName = file.getOriginalFilename();
                try {
                    if (fileName == null) {
                        throw new Exception();
                    }
                    //Recordar cambiar el path o ruta de carpetas donde se guardaran los archivos en la memoria local
                    file.transferTo(new File(Constants.Folder.ARCHIVES + fileName));
                    String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path(Constants.Endpoints.DOWNLOAD_FILE)
                        .path(fileName)
                        .toUriString();
                    ResponseClass response = new ResponseClass(fileName,
                        downloadUrl,
                        file.getContentType(),
                        file.getSize());
                    responseList.add(response);
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }
        } else {
            jsonResponse = String.format("{\"Error\": " + Constants.Exceptions.INVALID_QUERY);
            throw new MultipartException(jsonResponse);
        }
        return ResponseEntity.ok(responseList);
    }
}

