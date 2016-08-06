package com.jakduk.core.service;

import java.awt.*;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Stream;
import javax.imageio.ImageIO;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakduk.core.common.CommonConst;
import com.jakduk.core.dao.JakdukDAO;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.exception.UnauthorizedAccessException;
import com.jakduk.core.exception.UserFeelingException;
import com.jakduk.core.model.db.Gallery;
import com.jakduk.core.model.embedded.GalleryStatus;
import com.jakduk.core.model.simple.BoardFreeOnGallery;
import com.jakduk.core.model.simple.GalleryOnList;
import com.jakduk.core.repository.GalleryRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.jakduk.core.authentication.common.CommonPrincipal;
import com.jakduk.core.model.embedded.CommonFeelingUser;
import com.jakduk.core.model.embedded.CommonWriter;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 18.
 * @desc     :
 */

@Service
@Slf4j
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

	@Autowired
	private SearchService searchService;

	// 사진 목록.
	public List<GalleryOnList> getGalleriesById(String id, Integer size) {
		if (Objects.nonNull(id))
			return jakdukDAO.findGalleriesById(Direction.DESC, size, new ObjectId(id));
		else
			return jakdukDAO.findGalleriesById(Direction.DESC, size, null);
	}

	// 사진의 좋아요 개수 가져오기.
	public Map<String, Integer> getGalleryUsersLikingCount(List<ObjectId> ids) {
		return jakdukDAO.findGalleryUsersLikingCount(ids);
	}

	// 사진의 싫어요 개수 가져오기.
	public Map<String, Integer> getGalleryUsersDislikingCount(List<ObjectId> ids) {
		return jakdukDAO.findGalleryUsersDislikingCount(ids);
	}

	public Gallery findOneById(String id) {
		return galleryRepository.findOne(id);
	}

    public List<Gallery> findByIds(List<String> ids) {
        return galleryRepository.findByIdIn(ids);
    }

	public void getList(Model model, Locale locale) {
		try {
			model.addAttribute("dateTimeFormat", new ObjectMapper().writeValueAsString(commonService.getDateTimeFormat(locale)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 사진 올리기.
	 * @param file 멀티파트 객체
	 * @return Gallery 객체
     */
	public Gallery uploadImage(MultipartFile file) {

		try {
			Gallery gallery = new Gallery();

			CommonPrincipal principal = userService.getCommonPrincipal();
			String userid = principal.getId();
			String username = principal.getUsername();
			CommonConst.ACCOUNT_TYPE accountType = principal.getProviderId();

			CommonWriter writer = new CommonWriter(userid, username, accountType);

			gallery.setWriter(writer);

			GalleryStatus status = new GalleryStatus();
			status.setStatus(CommonConst.GALLERY_STATUS_TYPE.TEMP);
			gallery.setStatus(status);

			gallery.setFileName(file.getOriginalFilename());
			gallery.setFileSize(file.getSize());
			gallery.setSize(file.getSize());

			// 사진 포맷.
			String formatName = "jpg";
			String splitContentType[] = file.getContentType().split("/");

			if (!splitContentType[1].equals("octet-stream")) {
				formatName = splitContentType[1];
				gallery.setContentType(file.getContentType());
			} else {
				gallery.setContentType("image/jpeg");
			}

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

			if (log.isDebugEnabled()) {
				log.debug("gallery info=" + gallery);
			}

			return gallery;

		} catch (IOException e) {
			throw new RuntimeException(commonService.getResourceBundleMessage("messages.gallery", "gallery.exception.io"));
		}
	}

	// 이미지 가져오기.
	public ByteArrayOutputStream getImage(Locale locale, Gallery gallery, CommonConst.IMAGE_TYPE imageType) throws IOException {
		ObjectId objId = new ObjectId(gallery.getId());
		Instant instant = Instant.ofEpochMilli(objId.getDate().getTime());
		LocalDateTime timePoint = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

		String imagePath = null;

		switch (imageType) {
			case FULL:
				imagePath = storageImagePath;
				break;
			case THUMBNAIL:
				imagePath = storageThumbnailPath;
				break;
		}

		Path filePath = Paths.get(imagePath, String.valueOf(timePoint.getYear()),
				String.valueOf(timePoint.getMonthValue()), String.valueOf(timePoint.getDayOfMonth()), gallery.getId());

		if (Files.exists(filePath, LinkOption.NOFOLLOW_LINKS)) {
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(filePath.toString()));
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream(512);

			int imageByte;

			while ((imageByte = in.read()) != -1){
				byteStream.write(imageByte);
			}

			in.close();
			return byteStream;
		} else {
			throw new NoSuchElementException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.no.such.element"));
		}
	}

	/**
	 * 사진 삭제.
	 * @param id
	 * @return
	 */
	public void removeImage(String id) {

		CommonPrincipal principal = userService.getCommonPrincipal();
		String accountId = principal.getId();

		Gallery gallery = galleryRepository.findOne(id);

		if (Objects.isNull(gallery)) {
			throw new ServiceException(ServiceError.NOT_FOUND);
		}

		if (Objects.isNull(gallery.getWriter())) {
			throw new UnauthorizedAccessException(commonService.getResourceBundleMessage("messages.common", "common.exception.access.denied"));
		}

		if (!accountId.equals(gallery.getWriter().getUserId())) {
			throw new UnauthorizedAccessException(commonService.getResourceBundleMessage("messages.common", "common.exception.access.denied"));
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
				throw new RuntimeException(commonService.getResourceBundleMessage("messages.gallery", "gallery.exception.io"));
			}
		}

		// 엘라스틱 서치 document 삭제.
		searchService.deleteDocumentBoard(gallery.getId());
	}

	public Map<String, Object> getGallery(String id, Boolean isAddCookie) {

		Gallery gallery = galleryRepository.findOne(id);

		if (gallery == null) {
			return null;
		}

		if (isAddCookie) {
			int views = gallery.getViews();
			gallery.setViews(++views);
			galleryRepository.save(gallery);
		}

		Map<String, Date> createDate = new HashMap<>();

		Gallery prevGall = jakdukDAO.getGalleryById(new ObjectId(id), Sort.Direction.ASC);
		Gallery nextGall = jakdukDAO.getGalleryById(new ObjectId(id), Sort.Direction.DESC);
		List<BoardFreeOnGallery> posts = jakdukDAO.getBoardFreeOnGallery(new ObjectId(id));

		ObjectId objId = new ObjectId(id);
		createDate.put(id, objId.getDate());

		for (BoardFreeOnGallery post : posts) {
			objId = new ObjectId(post.getId());
			createDate.put(post.getId(), objId.getDate());
		}

		Map<String, Object> data = new HashMap<>();
		data.put("gallery", gallery);
		data.put("prev", prevGall);
		data.put("next", nextGall);
		data.put("linkedPosts", posts);
		data.put("createDate", createDate);

		return data;
	}

	public Map<String, Object> setUserFeeling(String id, CommonConst.FEELING_TYPE feeling) {

		String errCode = CommonConst.BOARD_USERS_FEELINGS_STATUS_NONE;

		CommonPrincipal principal = userService.getCommonPrincipal();
		String accountId = principal.getId();
		String accountName = principal.getUsername();

		Gallery gallery = galleryRepository.findOne(id);
		CommonWriter writer = gallery.getWriter();

		List<CommonFeelingUser> usersLiking = gallery.getUsersLiking();
		List<CommonFeelingUser> usersDisliking = gallery.getUsersDisliking();

		if (usersLiking == null) {
			usersLiking = new ArrayList<>();
		}

		if (usersDisliking == null) {
			usersDisliking = new ArrayList<>();
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
				CommonFeelingUser feelingUser = new CommonFeelingUser(new ObjectId().toString(), accountId, accountName);

				if (CommonConst.FEELING_TYPE.LIKE.equals(feeling)) {
					usersLiking.add(feelingUser);
					gallery.setUsersLiking(usersLiking);
				} else {
					usersDisliking.add(feelingUser);
					gallery.setUsersDisliking(usersDisliking);
				}

				galleryRepository.save(gallery);
			} else {
				throw new UserFeelingException(
					CommonConst.USER_FEELING_ERROR_CODE.ALREADY.toString(),
					commonService.getResourceBundleMessage("messages.common", "common.exception.select.already.like")
				);
			}
		} else {
			throw new ServiceException(ServiceError.FORBIDDEN);
		}

		Map<String, Object> data = new HashMap<>();
		data.put("feeling", feeling);
		data.put("numberOfLike", usersLiking.size());
		data.put("numberOfDislike", usersDisliking.size());
		return data;
	}

}
