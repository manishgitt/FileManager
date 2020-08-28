package com.assingment.file.Manager.controller;

//import java.awt.PageAttributes.MediaType;
/*import java.io.File;
import java.io.ObjectInputStream.GetField;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;*/
import java.io.IOException;

//import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Logger;
//import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

//import com.assingment.file.Manager.FileManagerApplication;
import com.assingment.file.Manager.res.FileInfo;
import com.assingment.file.Manager.service.FileManageService;

//import ch.qos.logback.core.util.ContentTypeUtil;

@RestController
public class ManageFileController {
	
	private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger(ManageFileController.class);
	
	@Autowired
	private FileManageService fileManage;
	

	public ManageFileController(FileManageService fileManage) {
		this.fileManage = fileManage;
	}
	
	//CREATE FILE
	@PostMapping("/create")
	FileInfo createFile( @RequestParam("file") MultipartFile file)
	{
		logger.info("inside create file controller");
		
		//String fileName;
		
		String fileName=fileManage.createFileService(file);
		
		logger.info(fileName+" file uploaded successfully");
		
		
		logger.info("creating download link");
		String url=ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/download/")
				.path(fileName)
				.toUriString();
		
		logger.info("download link for the uploaded file :"+ url);
		
	   String contentType=file.getContentType();
	   
		FileInfo response =new FileInfo(fileName,contentType,url);
		
		return response;
	}
	
	//DOWNLOAD FILE
	@GetMapping("/download/{fileName}")
	ResponseEntity<org.springframework.core.io.Resource> downLoadFile(@PathVariable String fileName,HttpServletRequest request)
	{
		logger.info("sending request to downloadFileService to download file: "+fileName);	
		
		
		org.springframework.core.io.Resource resource=fileManage.downloadFileService(fileName);
		
		// making mimeType generic type
		String mimeType=org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
		try {
			request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
			
			logger.info("fetching file from downloadFileService  response");
		} catch (IOException e) {
			mimeType=org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
			logger.error("mediatype error");
		}
		
		logger.info("file downloaded successfully");
		return ResponseEntity.ok().contentType(org.springframework.http.MediaType.parseMediaType(mimeType))
				.header(HttpHeaders.CONTENT_DISPOSITION,"attachment;fileName="+resource.getFilename()).body(resource);
		
	}
	
	//DELETE file
	
	@DeleteMapping("/delete/{fileName}")
	public String deleteFile(@PathVariable String fileName)
	{
		logger.info("inside delete file controller redirected to deleteFileService");
		fileManage.deleteFileService(fileName);
		
		return fileName+" deleted successfully";
	          
	}
	
}
