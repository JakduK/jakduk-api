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
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.jakduk.authentication.common.CommonPrincipal;
import com.jakduk.common.CommonConst;
import com.jakduk.dao.BoardFreeOnGallery;
import com.jakduk.dao.JakdukDAO;
import com.jakduk.model.db.Gallery;
import com.jakduk.model.embedded.CommonFeelingUser;
import com.jakduk.model.embedded.CommonWriter;
import com.jakduk.model.embedded.GalleryStatus;
import com.jakduk.model.simple.GalleryOnList;
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
	
	@Value("${kakao.javascript.key}")
	private String kakaoJavascriptKey;

	@Autowired
	private UserService userService;
	
	@Autowired
	private GalleryRepository galleryRepository;
	
	@Autowired
	private JakdukDAO jakdukDAO;
	
	@Autowired
	private CommonService commonService;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	public Integer uploadImage(Model model, MultipartFile file) {
		
		try {
			Gallery gallery = new Gallery();
			
			CommonPrincipal principal = userService.getCommonPrincipal();
			String userid = principal.getId();
			String username = principal.getUsername();
			String type = principal.getType();

			CommonWriter writer = new CommonWriter();
			writer.setUserId(userid);
			writer.setUsername(username);
			writer.setType(type);
			gallery.setWriter(writer);
			
			GalleryStatus status = new GalleryStatus();
			status.setStatus(CommonConst.GALLERY_STATUS_TEMP);
			gallery.setStatus(status);
			
			gallery.setFileName(file.getOriginalFilename());
			gallery.setFileSize(file.getSize());
			gallery.setSize(file.getSize());
			gallery.setContentType(file.getContentType());

			galleryRepository.save(gallery);
			
			// 폴더 생성.
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
			
			// 사진 경로.
			Path imageFilePath = imageDirPath.resolve(gallery.getId());
			Path thumbFilePath = thumbDirPath.resolve(gallery.getId());
			
			// 사진 포맷.
			String formatName = "jpg";
			
			String splitContentType[] = gallery.getContentType().split("/");
			if (splitContentType != null) {
				formatName = splitContentType[1];
			}

			// 사진 저장.
			if (Files.notExists(imageFilePath, LinkOption.NOFOLLOW_LINKS)) {
				if (CommonConst.GALLERY_MAXIUM_CAPACITY > file.getSize()) {
					Files.write(imageFilePath, file.getBytes());
				} else {
					BufferedImage bi = ImageIO.read(file.getInputStream());

					double ratio = CommonConst.GALLERY_MAXIUM_CAPACITY / (double) file.getSize();
					int ratioWidth = (int)(bi.getWidth() * ratio);
					int ratioHeight = (int)(bi.getHeight() * ratio);
					
					BufferedImage bufferIm = new BufferedImage(ratioWidth, ratioHeight, BufferedImage.TYPE_INT_RGB);
					Image tempImg = bi.getScaledInstance(ratioWidth, ratioHeight, Image.SCALE_AREA_AVERAGING);
					Graphics2D g2 = bufferIm.createGraphics();
					g2.drawImage(tempImg, 0, 0, ratioWidth, ratioHeight, null);
					
					ImageIO.write(bufferIm, formatName, imageFilePath.toFile());
					
					BasicFileAttributes attr = Files.readAttributes(imageFilePath, BasicFileAttributes.class);
					
					gallery.setSize(attr.size());
					galleryRepository.save(gallery);
				}
			}
			
			// 썸네일 만들기.
			if (Files.notExists(thumbFilePath, LinkOption.NOFOLLOW_LINKS)) {
				//BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
				BufferedImage bi = ImageIO.read(file.getInputStream());
				BufferedImage bufferIm = new BufferedImage(CommonConst.GALLERY_THUMBNAIL_SIZE_WIDTH, CommonConst.GALLERY_THUMBNAIL_SIZE_HEIGHT, BufferedImage.TYPE_INT_RGB);
				Image tempImg = bi.getScaledInstance(CommonConst.GALLERY_THUMBNAIL_SIZE_WIDTH, CommonConst.GALLERY_THUMBNAIL_SIZE_HEIGHT, Image.SCALE_AREA_AVERAGING);
				Graphics2D g2 = bufferIm.createGraphics();
				g2.drawImage(tempImg, 0, 0, CommonConst.GALLERY_THUMBNAIL_SIZE_WIDTH, CommonConst.GALLERY_THUMBNAIL_SIZE_HEIGHT, null);
				
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
	
	public Integer getImage(HttpServletResponse response, String id) {
		
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
	
	public Integer getThumbnail(HttpServletResponse response, String id) {
		
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
	
	/**
	 * 사진 삭제.
	 * @param model
	 * @param id
	 * @return
	 */
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
		
		// 사진을 삭제하기 전에, 이 사진과 연동된 글이 있는지 검사를 해야 한다. 최종적으로 연동된 글이 전부 없어진다면 사진은 삭제되어야 한다.
		// 추가 할것.
		
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

	public Model getList(Model model, String id) {

		List<ObjectId> ids = new ArrayList<ObjectId>();
		
		List<GalleryOnList> galleries;
		
		if (id != null) {
			galleries = jakdukDAO.getGalleryList(Direction.DESC, CommonConst.GALLERY_SIZE, new ObjectId(id));
		} else {
			galleries = jakdukDAO.getGalleryList(Direction.DESC, CommonConst.GALLERY_SIZE, null);
		}
		
		for (GalleryOnList gallery : galleries) {
			ids.add(new ObjectId(gallery.getId()));
		}
		
		HashMap<String, Integer> usersLikingCount = jakdukDAO.getGalleryUsersLikingCount(ids);
		HashMap<String, Integer> usersDislikingCount = jakdukDAO.getGalleryUsersDislikingCount(ids);

		model.addAttribute("galleries", galleries);
		model.addAttribute("usersLikingCount", usersLikingCount);
		model.addAttribute("usersDislikingCount", usersDislikingCount);

		return model;
	}
	
	public Integer getGallery(Model model, Locale locale, String id, Boolean isAddCookie) {
		
		Gallery gallery = galleryRepository.findOne(id);
		
		if (gallery == null) {
			return HttpServletResponse.SC_NOT_FOUND;
		}
		
		if (isAddCookie == true) {
			int views = gallery.getViews();
			gallery.setViews(++views);
			galleryRepository.save(gallery);
		}
		
		Map<String, Date> createDate = new HashMap<String, Date>();
		
		Gallery prevGall = jakdukDAO.getGalleryById(new ObjectId(id), Sort.Direction.ASC);
		Gallery nextGall = jakdukDAO.getGalleryById(new ObjectId(id), Sort.Direction.DESC);
		List<BoardFreeOnGallery> posts = jakdukDAO.getBoardFreeOnGallery(new ObjectId(id));
		
		ObjectId objId = new ObjectId(id);
		createDate.put(id, objId.getDate());
		
		for (BoardFreeOnGallery post : posts) {
			objId = new ObjectId(post.getId());
			createDate.put(post.getId(), objId.getDate());
		}

		model.addAttribute("gallery", gallery);
		model.addAttribute("prev", prevGall);
		model.addAttribute("next", nextGall);
		model.addAttribute("linkedPosts", posts);
		model.addAttribute("createDate", createDate);
		model.addAttribute("dateTimeFormat", commonService.getDateTimeFormat(locale));
		model.addAttribute("kakaoKey", kakaoJavascriptKey);

		return HttpServletResponse.SC_OK;
	}
	
	public Model setUserFeeling(Model model, String id, String feeling) {
		
		String errCode = CommonConst.BOARD_USERS_FEELINGS_STATUS_NONE;

		CommonPrincipal principal = userService.getCommonPrincipal();
		String accountId = principal.getId();
		String accountName = principal.getUsername();
		
		Gallery gallery = galleryRepository.findOne(id);
		CommonWriter writer = gallery.getWriter();

		List<CommonFeelingUser> usersLiking = gallery.getUsersLiking();
		List<CommonFeelingUser> usersDisliking = gallery.getUsersDisliking();

		if (usersLiking == null) {
			usersLiking = new ArrayList<CommonFeelingUser>(); 
		}
		
		if (usersDisliking == null) {
			usersDisliking = new ArrayList<CommonFeelingUser>(); 
		}
		
		if (accountId != null && accountName != null) {
			if (writer != null && accountId.equals(writer.getUserId())) {
				errCode = CommonConst.BOARD_USERS_FEELINGS_STATUS_WRITER;
			} 

			if (errCode.equals(CommonConst.BOARD_USERS_FEELINGS_STATUS_NONE)) {
				Stream<CommonFeelingUser> users = usersLiking.stream();
				Long itemCount = users.filter(item -> item.getUserId().equals(accountId)).count();
				if (itemCount > 0) {
					errCode = CommonConst.BOARD_USERS_FEELINGS_STATUS_ALREADY;
				}
			}
			
			if (errCode.equals(CommonConst.BOARD_USERS_FEELINGS_STATUS_NONE)) {
				Stream<CommonFeelingUser> users = usersDisliking.stream();
				Long itemCount = users.filter(item -> item.getUserId().equals(accountId)).count();
				if (itemCount > 0) {
					errCode = CommonConst.BOARD_USERS_FEELINGS_STATUS_ALREADY;
				}				
			}

			if (errCode.equals(CommonConst.BOARD_USERS_FEELINGS_STATUS_NONE)) {
				CommonFeelingUser feelingUser = new CommonFeelingUser();
				feelingUser.setUserId(accountId);
				feelingUser.setUsername(accountName);
				feelingUser.setId(new ObjectId().toString());

				switch (feeling) {
				case CommonConst.FEELING_TYPE_LIKE:
					usersLiking.add(feelingUser);
					gallery.setUsersLiking(usersLiking);
					errCode = CommonConst.BOARD_USERS_FEELINGS_STATUS_LIKE; 
					break;
				case CommonConst.FEELING_TYPE_DISLIKE:
					usersDisliking.add(feelingUser);
					gallery.setUsersDisliking(usersDisliking);
					errCode = CommonConst.BOARD_USERS_FEELINGS_STATUS_DISLIKE;
					break;
				default:
					break;
				}
				
				galleryRepository.save(gallery);
			}
		} else {
			errCode = CommonConst.BOARD_USERS_FEELINGS_STATUS_ANONYMOUS;
		}
		
		model.addAttribute("errorCode", errCode);
		model.addAttribute("numberOfLike", usersLiking.size());
		model.addAttribute("numberOfDislike", usersDisliking.size());
		
		return model;
	}	

}
