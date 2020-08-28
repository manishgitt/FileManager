package com.assingment.file.Manager;



import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class FileManagerApplication {

	private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger(FileManagerApplication.class);
	public static void main(String[] args) {
		
		
		SpringApplication.run(FileManagerApplication.class, args);
		logger.info("FileManagerApplication  started successfully");
	}

}
