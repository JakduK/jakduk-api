package com.jakduk.service;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.jakduk.authentication.common.CommonPrincipal;
import com.jakduk.common.CommonConst;
import com.jakduk.dao.BoardFreeOnGallery;
import com.jakduk.dao.JakdukDAO;
import com.jakduk.model.db.Gallery;
import com.jakduk.model.embedded.BoardWriter;
import com.jakduk.model.embedded.GalleryStatus;
import com.jakduk.repository.GalleryRepository;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 18.
 * @desc     :
 */

@Service
public class GalleryService {
	
	@Value("${storage.image.path}")
	private String storageImagePath;
	
	@Value("${storage.thumbnail.path}")
	private String storageThumbnailPath;

	@Autowired
	private UserService userService;
	
	@Autowired
	private GalleryRepository galleryRepository;
	
	@Autowired
	private JakdukDAO jakdukDAO;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	public Integer uploadImage(Model model, MultipartFile file) {
		
		try {
			Gallery gallery = new Gallery();
			
			CommonPrincipal principal = userService.getCommonPrincipal();
			String userid = principal.getId();
			String username = principal.getUsername();
			String type = principal.getType();

			BoardWriter writer = new BoardWriter();
			writer.setUserId(userid);
			writer.setUsername(username);
			writer.setType(type);
			gallery.setWriter(writer);
			
			GalleryStatus status = new GalleryStatus();
			status.setUse(CommonConst.GALLERY_USE_STATUS_UNUSE);
			gallery.setStatus(status);
			
			gallery.setFileName(file.getOriginalFilename());
			gallery.setSize(file.getSize());
			gallery.setContentType(file.getContentType());

			galleryRepository.save(gallery);
			
			ObjectId objId = new ObjectId(gallery.getId());
			Instant instant = Instant.ofEpochMilli(objId.getDate().getTime());
			LocalDateTime timePoint = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
			
			Path imageDirPath = Paths.get(storageImagePath, String.valueOf(timePoint.getYear()), 
					String.valueOf(timePoint.getMonthValue()), String.valueOf(timePoint.getDayOfMonth()));
			
			Path thumbDirPath = Paths.get(storageThumbnailPath, String.valueOf(timePoint.getYear()), 
					String.valueOf(timePoint.getMonthValue()), String.valueOf(timePoint.getDayOfMonth()));
			
			if (Files.notExists(imageDirPath, LinkOption.NOFOLLOW_LINKS)) {
					Files.createDirectories(imageDirPath);
			}
			
			if (Files.notExists(thumbDirPath, LinkOption.NOFOLLOW_LINKS)) {
				Files.createDirectories(thumbDirPath);
			}
			
			Path imageFilePath = imageDirPath.resolve(gallery.getId());
			Path thumbFilePath = thumbDirPath.resolve(gallery.getId());

			if (Files.notExists(imageFilePath, LinkOption.NOFOLLOW_LINKS)) {
				//BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
				Files.write(imageFilePath, file.getBytes());
			}
			
			if (Files.notExists(thumbFilePath, LinkOption.NOFOLLOW_LINKS)) {
				//BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
				BufferedImage bi = ImageIO.read(file.getInputStream());
				BufferedImage bufferIm = new BufferedImage(CommonConst.GALLERY_THUMBNAIL_SIZE_WIDTH, CommonConst.GALLERY_THUMBNAIL_SIZE_HEIGHT, BufferedImage.TYPE_INT_RGB);
				Image tempImg = bi.getScaledInstance(CommonConst.GALLERY_THUMBNAIL_SIZE_WIDTH, CommonConst.GALLERY_THUMBNAIL_SIZE_HEIGHT, Image.SCALE_AREA_AVERAGING);
				Graphics2D g2 = bufferIm.createGraphics();
				g2.drawImage(tempImg, 0, 0, CommonConst.GALLERY_THUMBNAIL_SIZE_WIDTH, CommonConst.GALLERY_THUMBNAIL_SIZE_HEIGHT, null);
				
				String formatName = "jpg";
				
				String splitContentType[] = gallery.getContentType().split("/");
				if (splitContentType != null) {
					formatName = splitContentType[1];
				}
				
				ImageIO.write(bufferIm, formatName, thumbFilePath.toFile());
			}
			
			if (logger.isDebugEnabled()) {
				logger.debug("gallery info=" + gallery);
			}
			
			model.addAttribute("image", gallery);
			
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return HttpServletResponse.SC_NOT_IMPLEMENTED;
		} catch (IOException e) {
			e.printStackTrace();
			return HttpServletResponse.SC_NOT_IMPLEMENTED;
		}
		
		return HttpServletResponse.SC_OK;
	}
	
	public Integer getImage(HttpServletResponse response, Model model, String id) {
		
		try{
			Gallery gallery = galleryRepository.findOne(id);
			
			if (gallery == null) {
				return HttpServletResponse.SC_NOT_FOUND;
			}
			
			ObjectId objId = new ObjectId(gallery.getId());
			Instant instant = Instant.ofEpochMilli(objId.getDate().getTime());
			LocalDateTime timePoint = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
			
			Path filePath = Paths.get(storageImagePath, String.valueOf(timePoint.getYear()), 
					String.valueOf(timePoint.getMonthValue()), String.valueOf(timePoint.getDayOfMonth()), gallery.getId());
			
			if (Files.exists(filePath, LinkOption.NOFOLLOW_LINKS)) {
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(filePath.toString()));
				ByteArrayOutputStream byteStream = new ByteArrayOutputStream(512);
				
				int imageByte;
				
				while ((imageByte = in.read()) != -1){
					byteStream.write(imageByte);
				}
				
				in.close();
				response.setContentType(gallery.getContentType());
				byteStream.writeTo(response.getOutputStream());
			}
			

		} catch(IOException ioe){
			ioe.printStackTrace();
			return HttpServletResponse.SC_NOT_IMPLEMENTED;
		}
		return HttpServletResponse.SC_OK;
	}
	
	public Integer getThumbnail(HttpServletResponse response, Model model, String id) {
		
		try{
			Gallery gallery = galleryRepository.findOne(id);
			
			if (gallery == null) {
				return HttpServletResponse.SC_NOT_FOUND;
			}
			
			ObjectId objId = new ObjectId(gallery.getId());
			Instant instant = Instant.ofEpochMilli(objId.getDate().getTime());
			LocalDateTime timePoint = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
			
			Path filePath = Paths.get(storageThumbnailPath, String.valueOf(timePoint.getYear()), 
					String.valueOf(timePoint.getMonthValue()), String.valueOf(timePoint.getDayOfMonth()), gallery.getId());
			
			if (Files.exists(filePath, LinkOption.NOFOLLOW_LINKS)) {
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(filePath.toString()));
				ByteArrayOutputStream byteStream = new ByteArrayOutputStream(512);
				
				int imageByte;
				
				while ((imageByte = in.read()) != -1){
					byteStream.write(imageByte);
				}
				
				in.close();
				response.setContentType(gallery.getContentType());
				byteStream.writeTo(response.getOutputStream());
			}
			

		} catch(IOException ioe){
			ioe.printStackTrace();
			return HttpServletResponse.SC_NOT_IMPLEMENTED;
		}
		
		return HttpServletResponse.SC_OK;
	}
	
	public Integer removeImage(Model model, String id) {
		
		CommonPrincipal principal = userService.getCommonPrincipal();
		String accountId = principal.getId();

		if (accountId == null) {
			return HttpServletResponse.SC_UNAUTHORIZED;
		}
		
		Gallery gallery = galleryRepository.findOne(id);
		
		if (gallery == null) {
			return HttpServletResponse.SC_NOT_FOUND;
		}
		
		if (gallery.getWriter() == null) {
			return HttpServletResponse.SC_UNAUTHORIZED;
		}
		
		if (!accountId.equals(gallery.getWriter().getUserId())) {
			return HttpServletResponse.SC_UNAUTHORIZED;
		}
		
		ObjectId objId = new ObjectId(gallery.getId());
		Instant instant = Instant.ofEpochMilli(objId.getDate().getTime());
		LocalDateTime timePoint = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		
		Path imageFilePath = Paths.get(storageImagePath, String.valueOf(timePoint.getYear()), 
				String.valueOf(timePoint.getMonthValue()), String.valueOf(timePoint.getDayOfMonth()), gallery.getId());
		
		Path thumbThumbnailPath = Paths.get(storageThumbnailPath, String.valueOf(timePoint.getYear()), 
				String.valueOf(timePoint.getMonthValue()), String.valueOf(timePoint.getDayOfMonth()), gallery.getId());
		
		if (Files.exists(imageFilePath, LinkOption.NOFOLLOW_LINKS) && Files.exists(thumbThumbnailPath, LinkOption.NOFOLLOW_LINKS)) {
			try {
				Files.delete(imageFilePath);
				Files.delete(thumbThumbnailPath);
				galleryRepository.delete(gallery);
				
			} catch (IOException e) {
				e.printStackTrace();
				return HttpServletResponse.SC_NOT_IMPLEMENTED;
			}
		}

		return HttpServletResponse.SC_OK;
	}	

	public Model getList(Model model) {

		List<ObjectId> ids = new ArrayList<ObjectId>();
		
		Sort sort = new Sort(Sort.Direction.DESC, Arrays.asList("_id"));
		Pageable pageable = new PageRequest(0, CommonConst.BOARD_SIZE_LINE_NUMBER, sort);
		
		List<Gallery> galleries = galleryRepository.findAll(pageable).getContent();
		
		for (Gallery gallery : galleries) {
			ids.add(new ObjectId(gallery.getBoardItem().getId()));
		}
		
		if (!ids.isEmpty()) {
			HashMap<String, BoardFreeOnGallery> posts = jakdukDAO.getBoardFreeOnGallery(ids);
			model.addAttribute("posts", posts);			
		}

		model.addAttribute("galleries", galleries);

		return model;
	}

}
