package com.opinionated.ws.service.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);
	private static final String BASE_DIRECTORY = "images/";
	private static final String WEB_SERVER_CHROME = "http://127.0.0.1:8887/";
	
	public String storeFile(MultipartFile file) {
		String realPath = getClass().getClassLoader().getResource(BASE_DIRECTORY).getPath();
		try {
			String filePath = realPath + "/" + file.getOriginalFilename();
			file.transferTo(new File(filePath));
			String storedPath = BASE_DIRECTORY + file.getOriginalFilename();
			return storedPath;
		} catch (FileAlreadyExistsException fileAlreadyExistsException) {
			LOGGER.error("File exists");
		} catch (IOException exception) {
			LOGGER.error("Unknown error " + exception.getMessage());
		}
		return null;
	}
	
	public String getFileRealPath(String fileName) {
		String realPath = getClass().getClassLoader().getResource(BASE_DIRECTORY).getPath();
		String filePath = realPath + fileName;
		if (new File(filePath).exists()) {
			return WEB_SERVER_CHROME + fileName;
		}
		return null;
	}

}
