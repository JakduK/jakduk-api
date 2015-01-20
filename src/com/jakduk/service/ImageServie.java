package com.jakduk.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.jakduk.authentication.common.CommonPrincipal;
import com.jakduk.model.db.Image;
import com.jakduk.model.embedded.BoardWriter;
import com.jakduk.repository.ImageRepository;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 18.
 * @desc     :
 */

@Service
public class ImageServie {
	
	@Value("${file.server.real.path}")
	private String fileServerRealPath;

	@Autowired
	private UserService userService;
	
	@Autowired
	private ImageRepository imageRepository;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	public Integer uploadImage(Model model, MultipartFile file) {
		
		try {
			Image image = new Image();
			
			CommonPrincipal principal = userService.getCommonPrincipal();
			String userid = principal.getId();
			String username = principal.getUsername();
			String type = principal.getType();

			BoardWriter writer = new BoardWriter();
			writer.setUserId(userid);
			writer.setUsername(username);
			writer.setType(type);
			image.setWriter(writer);
			image.setName(file.getOriginalFilename());
			image.setSize(file.getSize());
			image.setContentType(file.getContentType());
			imageRepository.save(image);
			
			logger.debug("image=" + image);
			
			ObjectId objId = new ObjectId(image.getId());
			Instant instant = Instant.ofEpochMilli(objId.getDate().getTime());
			LocalDateTime timePoint = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
			
			Path directoryPath = Paths.get(fileServerRealPath, String.valueOf(timePoint.getYear()), 
					String.valueOf(timePoint.getMonthValue()), String.valueOf(timePoint.getDayOfMonth()));
			
			if (Files.notExists(directoryPath, LinkOption.NOFOLLOW_LINKS)) {
					Files.createDirectories(directoryPath);
			}
			
			Path filePath = directoryPath.resolve(image.getId());

			if (Files.notExists(filePath, LinkOption.NOFOLLOW_LINKS)) {
				//BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
				Files.write(filePath, file.getBytes());
			}
			
			model.addAttribute("image", image);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return HttpServletResponse.SC_OK;
	}
	
	public Integer getImage(HttpServletResponse response, Model model, String id) {
		
		try{
			Image image = imageRepository.findOne(id);
			
			ObjectId objId = new ObjectId(image.getId());
			Instant instant = Instant.ofEpochMilli(objId.getDate().getTime());
			LocalDateTime timePoint = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
			
			Path filePath = Paths.get(fileServerRealPath, String.valueOf(timePoint.getYear()), 
					String.valueOf(timePoint.getMonthValue()), String.valueOf(timePoint.getDayOfMonth()), image.getId());
			
			if (Files.exists(filePath, LinkOption.NOFOLLOW_LINKS)) {
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(filePath.toString()));
				ByteArrayOutputStream byteStream = new ByteArrayOutputStream(512);
				int imageByte;
				while((imageByte = in.read()) != -1){
					byteStream.write(imageByte);
				}
				in.close();
				response.setContentType("image/*");
				byteStream.writeTo(response.getOutputStream());
			}
			

		} catch(IOException ioe){
		}

		
		
		return HttpServletResponse.SC_OK;
	}
}
