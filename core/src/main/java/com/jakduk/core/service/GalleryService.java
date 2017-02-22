package com.jakduk.core.service;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.dao.JakdukDAO;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.model.db.Gallery;
import com.jakduk.core.model.embedded.CommonFeelingUser;
import com.jakduk.core.model.embedded.CommonWriter;
import com.jakduk.core.model.embedded.GalleryStatus;
import com.jakduk.core.model.simple.BoardFreeSimple;
import com.jakduk.core.model.simple.GalleryOnList;
import com.jakduk.core.repository.gallery.GalleryRepository;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 18.
 * @desc     :
 */

@Slf4j
@Service
public class GalleryService {

	@Value("${core.storage.image.path}")
	private String storageImagePath;

	@Value("${core.storage.thumbnail.path}")
	private String storageThumbnailPath;

	@Autowired
	private GalleryRepository galleryRepository;

	@Autowired
	private JakdukDAO jakdukDAO;

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
		return galleryRepository.findOneById(id).orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_GALLERY));
	}

    public List<Gallery> findByIds(List<String> ids) {
        return galleryRepository.findByIdIn(ids);
    }

	/**
	 * 사진 올리기.
	 * @return Gallery 객체
     */
	public Gallery uploadImage(CommonWriter writer, String originalFileName, long size, String contentType, byte[] bytes) {

		Gallery gallery = new Gallery();

		gallery.setWriter(writer);

		GalleryStatus status = GalleryStatus.builder()
				.status(CoreConst.GALLERY_STATUS_TYPE.TEMP)
				.build();

		gallery.setStatus(status);
		gallery.setFileName(originalFileName);
		gallery.setFileSize(size);
		gallery.setSize(size);

		// 사진 포맷.
		String formatName = "jpg";
		String splitContentType[] = StringUtils.split(contentType, "/");

		if (! splitContentType[1].equals("octet-stream")) {
			formatName = splitContentType[1];
			gallery.setContentType(contentType);
		} else {
			gallery.setContentType("image/jpeg");
		}

		galleryRepository.save(gallery);

		try {
			// 폴더 생성.
			ObjectId objId = new ObjectId(gallery.getId());
			Instant instant = Instant.ofEpochMilli(objId.getDate().getTime());
			LocalDateTime timePoint = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

			Path imageDirPath = Paths.get(storageImagePath, String.valueOf(timePoint.getYear()),
					String.valueOf(timePoint.getMonthValue()), String.valueOf(timePoint.getDayOfMonth()));

			Path thumbDirPath = Paths.get(storageThumbnailPath, String.valueOf(timePoint.getYear()),
					String.valueOf(timePoint.getMonthValue()), String.valueOf(timePoint.getDayOfMonth()));

			if (Files.notExists(imageDirPath, LinkOption.NOFOLLOW_LINKS))
				Files.createDirectories(imageDirPath);

			if (Files.notExists(thumbDirPath, LinkOption.NOFOLLOW_LINKS))
				Files.createDirectories(thumbDirPath);

			// 사진 경로.
			Path imageFilePath = imageDirPath.resolve(gallery.getId() + "." + formatName);
			Path thumbFilePath = thumbDirPath.resolve(gallery.getId() + "." + formatName);

			// 사진 저장.
			if (Files.notExists(imageFilePath, LinkOption.NOFOLLOW_LINKS)) {
				if ("gif".equals(formatName)) {
					Files.write(imageFilePath, bytes);
				} else {

					double scale = CoreConst.GALLERY_MAXIMUM_CAPACITY < size ?
							CoreConst.GALLERY_MAXIMUM_CAPACITY / (double) size : 1;

                    InputStream originalInputStream = new ByteArrayInputStream(bytes);

					Thumbnails.of(originalInputStream)
							.scale(scale)
							.toFile(imageFilePath.toFile());

					BasicFileAttributes attr = Files.readAttributes(imageFilePath, BasicFileAttributes.class);

					gallery.setSize(attr.size());

					galleryRepository.save(gallery);
				}
			}

			// 썸네일 만들기.
			if (Files.notExists(thumbFilePath, LinkOption.NOFOLLOW_LINKS)) {
				InputStream thumbInputStream = new ByteArrayInputStream(bytes);

				Thumbnails.of(thumbInputStream)
						.size(CoreConst.GALLERY_THUMBNAIL_SIZE_WIDTH, CoreConst.GALLERY_THUMBNAIL_SIZE_HEIGHT)
                        .crop(Positions.TOP_CENTER)
						.toFile(thumbFilePath.toFile());
			}

		} catch (IOException e) {
			throw new ServiceException(ServiceError.GALLERY_IO_ERROR, e);
		}

		log.debug("gallery=" + gallery);

		return gallery;

	}

	// 이미지 가져오기.
	public ByteArrayOutputStream getGalleryOutStream(Gallery gallery, CoreConst.IMAGE_TYPE imageType) {

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

		String formatName = StringUtils.split(gallery.getContentType(), "/")[1];

		Path filePath = Paths.get(imagePath, String.valueOf(timePoint.getYear()), String.valueOf(timePoint.getMonthValue()),
				String.valueOf(timePoint.getDayOfMonth()), gallery.getId() + "." + formatName);

		if (Files.exists(filePath, LinkOption.NOFOLLOW_LINKS)) {
			try {
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(filePath.toString()));
				ByteArrayOutputStream byteStream = new ByteArrayOutputStream(512);

				int imageByte;

				while ((imageByte = in.read()) != -1){
					byteStream.write(imageByte);
				}

				in.close();
				return byteStream;

			} catch (IOException e) {
				throw new ServiceException(ServiceError.GALLERY_IO_ERROR, e);
			}
		} else {
			throw new ServiceException(ServiceError.NOT_FOUND_GALLERY);
		}
	}

	/**
	 * 사진 삭제.
	 */
	public void removeImage(String userId, String id) {
		Gallery gallery = galleryRepository.findOneById(id)
                .orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_GALLERY));

		if (ObjectUtils.isEmpty(gallery.getWriter()))
			throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

		if (! userId.equals(gallery.getWriter().getUserId()))
            throw new ServiceException(ServiceError.FORBIDDEN);

		ObjectId objId = new ObjectId(gallery.getId());
		Instant instant = Instant.ofEpochMilli(objId.getDate().getTime());
		LocalDateTime timePoint = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        String formatName = StringUtils.split(gallery.getContentType(), "/")[1];

		Path imageFilePath = Paths.get(storageImagePath, String.valueOf(timePoint.getYear()), String.valueOf(timePoint.getMonthValue()),
                String.valueOf(timePoint.getDayOfMonth()), gallery.getId() + "." + formatName);

		Path thumbThumbnailPath = Paths.get(storageThumbnailPath, String.valueOf(timePoint.getYear()), String.valueOf(timePoint.getMonthValue()),
                String.valueOf(timePoint.getDayOfMonth()), gallery.getId() + "." + formatName);

        // TODO 사진을 삭제하기 전에, 이 사진과 연동된 글이 있는지 검사를 해야 한다. 최종적으로 연동된 글이 전부 없어진다면 사진은 삭제되어야 한다.
		if (Files.exists(imageFilePath, LinkOption.NOFOLLOW_LINKS) && Files.exists(thumbThumbnailPath, LinkOption.NOFOLLOW_LINKS)) {
			try {
				Files.delete(imageFilePath);
				Files.delete(thumbThumbnailPath);
				galleryRepository.delete(gallery);

			} catch (IOException e) {
				throw new ServiceException(ServiceError.GALLERY_IO_ERROR);
			}
		} else {
            throw new ServiceException(ServiceError.NOT_FOUND_GALLERY_FILE);
        }

		// 엘라스틱 서치 document 삭제.
		searchService.deleteDocumentGallery(gallery.getId());
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
		List<BoardFreeSimple> posts = jakdukDAO.getBoardFreeOnGallery(new ObjectId(id));

		ObjectId objId = new ObjectId(id);
		createDate.put(id, objId.getDate());

		for (BoardFreeSimple post : posts) {
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

	public Map<String, Object> setUserFeeling(CommonWriter writer, String id, CoreConst.FEELING_TYPE feeling) {

		String errCode = CoreConst.BOARD_USERS_FEELINGS_STATUS_NONE;

		String accountId = writer.getUserId();
		String accountName = writer.getUsername();

		Gallery gallery = galleryRepository.findOne(id);
		CommonWriter galleryWriter = gallery.getWriter();

		List<CommonFeelingUser> usersLiking = gallery.getUsersLiking();
		List<CommonFeelingUser> usersDisliking = gallery.getUsersDisliking();

		if (usersLiking == null) {
			usersLiking = new ArrayList<>();
		}

		if (usersDisliking == null) {
			usersDisliking = new ArrayList<>();
		}

		if (accountId != null && accountName != null) {
			if (galleryWriter != null && accountId.equals(galleryWriter.getUserId())) {
				errCode = CoreConst.BOARD_USERS_FEELINGS_STATUS_WRITER;
			}

			if (errCode.equals(CoreConst.BOARD_USERS_FEELINGS_STATUS_NONE)) {
				Stream<CommonFeelingUser> users = usersLiking.stream();
				Long itemCount = users.filter(item -> item.getUserId().equals(accountId)).count();
				if (itemCount > 0) {
					errCode = CoreConst.BOARD_USERS_FEELINGS_STATUS_ALREADY;
				}
			}

			if (errCode.equals(CoreConst.BOARD_USERS_FEELINGS_STATUS_NONE)) {
				Stream<CommonFeelingUser> users = usersDisliking.stream();
				Long itemCount = users.filter(item -> item.getUserId().equals(accountId)).count();
				if (itemCount > 0) {
					errCode = CoreConst.BOARD_USERS_FEELINGS_STATUS_ALREADY;
				}
			}

			if (errCode.equals(CoreConst.BOARD_USERS_FEELINGS_STATUS_NONE)) {
				CommonFeelingUser feelingUser = new CommonFeelingUser(new ObjectId().toString(), accountId, accountName);

				if (CoreConst.FEELING_TYPE.LIKE.equals(feeling)) {
					usersLiking.add(feelingUser);
					gallery.setUsersLiking(usersLiking);
				} else {
					usersDisliking.add(feelingUser);
					gallery.setUsersDisliking(usersDisliking);
				}

				galleryRepository.save(gallery);
			} else {
				throw new ServiceException(ServiceError.FEELING_SELECT_ALREADY_LIKE);
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
