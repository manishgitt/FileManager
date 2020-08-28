package com.assingment.file.Manager.service;

import java.io.IOException;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.logging.log4j.Logger;
/*import javax.annotation.Resource;
import javax.management.RuntimeErrorException;

import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;*/
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

//import com.assingment.file.Manager.controller.ManageFileController;


@Service
public class FileManageService {

	private Path fileStoragePath;
	public  String fileStorageLocation;
	private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger(FileManageService.class);
	
	public FileManageService(@Value("${file.storage.location:temp}") String  fileStorageLocation) {
		this.fileStorageLocation=fileStorageLocation;
		fileStoragePath = Paths.get(fileStorageLocation).toAbsolutePath().normalize();
		
		
		try {
			Files.createDirectories(fileStoragePath);
		} catch (IOException e) {
			throw new RuntimeException("Problem in creating Directory");
		}
	}

	public String createFileService(MultipartFile file) {
		String fileName =org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
		
		Path filePath =Paths.get(fileStoragePath+"\\"+fileName);
		logger.info("copying file at location: "+filePath);
		
		try {
			Files.copy(file.getInputStream(),filePath,StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			logger.error("creating file failed!!");
			throw new RuntimeException("File Creation Failed",e);
		}
		return fileName;
	}

	public org.springframework.core.io.Resource downloadFileService(String fileName) {
		Path path=Paths.get(fileStorageLocation).toAbsolutePath().resolve(fileName);
		org.springframework.core.io.Resource resource;
		
			logger.info("received request from controller to download: "+fileName);
		try {
			resource=new UrlResource(path.toUri());
			
		} catch (MalformedURLException e) {
			logger.error(fileName+" unreacheable");
			throw new RuntimeException("Problem in Reading the file",e);
		}
		
		if(resource.exists()&& resource.isReadable())
		{
			
			return resource;
		}
		else {
			logger.error("file doesnot exist");
			throw new RuntimeException("file doesn't exist or not readable");
		}
		
		
	}
	
	public String deleteFileService(String filename) {
		
		logger.info("deleteFileService called");
		Path filePath =Paths.get(fileStoragePath+"\\"+filename);
		
		try {
			Files.delete(filePath);
			
			logger.info(filename+" deleted");
		} catch (IOException e) {
			throw new RuntimeException(" File Unavailable to delete",e);
		}
		
		return null;
	}

}
