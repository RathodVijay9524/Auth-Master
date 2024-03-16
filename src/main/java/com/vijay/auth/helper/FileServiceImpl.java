package com.vijay.auth.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.vijay.auth.exception.BadApiRequestException;

import io.jsonwebtoken.io.IOException;
import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Log4j2
public class FileServiceImpl implements FileService   {

	@Override
	public String uploadFile(MultipartFile file, String path) throws IOException {
		  //abc.png
        String originalFilename = file.getOriginalFilename();
        log.info("Filename : {}", originalFilename);
        String filename = UUID.randomUUID().toString();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileNameWithExtension = filename + extension;
        String fullPathWithFileName = path  + fileNameWithExtension;

        log.info("full image path: {} ", fullPathWithFileName);
        if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")) {

            //file save
            log.info("file extension is {} ", extension);
            File folder = new File(path);
            if (!folder.exists()) {
                //create the folder
                folder.mkdirs();

            }

            //upload
            try {
				Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
			} catch (java.io.IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return fileNameWithExtension;

        } else {
            throw new BadApiRequestException("File with this " + extension + " not allowed !!");
        }


    }

	@Override
	public InputStream getResource(String path, String name) throws FileNotFoundException {
		String fullPath = path + File.separator + name;
        InputStream inputStream = new FileInputStream(fullPath);
        return inputStream;
    
	}

   
}