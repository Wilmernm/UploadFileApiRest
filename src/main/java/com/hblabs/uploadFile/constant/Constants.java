package com.hblabs.uploadFile.constant;

import java.util.logging.Logger;

import com.hblabs.uploadFile.controller.ProductController;

public class Constants {
    
    public static class Endpoints {
        //Aqui iniciamos las constantes relacionadas con la base de datos H2
        public static final String PRINCIPAL_ROOT = "/api";
        public static final String SINGLE_CELL_FILE = "/singlecell";
        public static final String MULTI_CELL_FILE = "/multiplecell";
        public static final String GET_ALL_FILES = "/all";

        //Aqui terminamos con las constantes de la base de datos e iniciamos con las constantes relacionadas con guardado en el sistema
        public static final String SINGLE_FILE_SYSTEM = "/singlefile";
        public static final String MULTIPLE_FILE_SYSTEM = "/multiplefile";
        public static final String DOWNLOAD_FILE = "/download";
    }

    public static class ReqParameters {
        public static final String FILE = "file";
        public static final String FILES = "files";
    }

    public static class Parameters {
        public static final String PRODUCT = "product";
        public static final String NULL = "null";
    }

    //Constantes relacionadas con el manejo de excepciones y advertencias del productServiceImplement
    public static class Exceptions {
        public static final String NOT_SAVED = "NOT_SAVED: Could not save File: ";
        public static final String EXCEED_LIMIT = "EXCEED_LIMIT: File size exceeds maximum limit";
        public static final String INVALID_PATH = "INVALID_PATH: Filename contains invalid path sequence";
        public static final String INVALID_ATTACHMENT = "INVALID_ATTACHMENT: The condition of uploading a single file is not met";
        public static final String INVALID_QUERY = "INVALID_QUERY: You have not correctly loaded the QUERY key-value";
        public static final String INVALID_FILENAME = "INVALID_FILENAME: File cannot have a null value";
    }

    public static class Test {
        public static final Logger logger = Logger.getLogger(ProductController.class.getName());

    }

    //Ruta de carpetas, donde se guardan los archivos subidos a traves del endpoint-post.
    public static class Folder {
        public static final String ARCHIVE = "/Users/wilmer/Desktop/download_system/archive/";
        public static final String ARCHIVES = "/Users/wilmer/Desktop/download_system/archives/";
    }
}
