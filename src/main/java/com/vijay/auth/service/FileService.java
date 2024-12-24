package com.vijay.auth.service;

import java.io.FileNotFoundException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import io.jsonwebtoken.io.IOException;

public interface FileService {


    String uploadFile(MultipartFile file, String path) throws IOException;

    InputStream getResource(String path,String name) throws FileNotFoundException;

}