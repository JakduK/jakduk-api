package com.jakduk.api.service;

import com.jakduk.api.common.ApiConst;
import com.jakduk.api.common.util.ApiUtils;
import com.jakduk.api.common.util.UserUtils;
import com.jakduk.api.vo.board.GalleryOnBoard;
import com.jakduk.api.vo.gallery.GalleriesResponse;
import com.jakduk.api.vo.gallery.GalleryDetail;
import com.jakduk.api.vo.gallery.GalleryResponse;
import com.jakduk.api.vo.gallery.SurroundingsGallery;
import com.jakduk.core.common.CoreConst;
import com.jakduk.core.dao.JakdukDAO;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.model.db.BoardFree;
import com.jakduk.core.model.db.Gallery;
import com.jakduk.core.model.embedded.CommonFeelingUser;
import com.jakduk.core.model.embedded.CommonWriter;
import com.jakduk.core.model.embedded.GalleryStatus;
import com.jakduk.core.model.embedded.LinkedItem;
import com.jakduk.core.model.simple.BoardFreeSimple;
import com.jakduk.core.model.simple.GallerySimple;
import com.jakduk.core.repository.board.free.BoardFreeRepository;
import com.jakduk.core.repository.gallery.GalleryRepository;
import com.jakduk.core.service.CommonGalleryService;
import com.jakduk.core.service.CommonSearchService;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
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
import java.util.function.Consumer;
import java.util.stream.Collectors;
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
	private BoardFreeRepository boardFreeRepository;

	@Autowired
	private JakdukDAO jakdukDAO;

	@Autowired
	private CommonSearchService commonSearchService;

	@Autowired
	private CommonGalleryService commonGalleryService;

	@Resource
	private ApiUtils apiUtils;

	// 사진 목록.
	public List<GallerySimple> getGalleriesById(String id, Integer size) {
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

	public List<Gallery> findByIdIn(List<String> galleryIds) {
		return galleryRepository.findByIdIn(galleryIds);
	}

	/**
	 * 사진 목록
	 */
	public GalleriesResponse getGalleries(String id, Integer size) {

		ObjectId objectId = null;

		if (StringUtils.isNotBlank(id))
			objectId = new ObjectId(id);

		List<Gallery> galleries = galleryRepository.findGalleriesById(objectId, CoreConst.CRITERIA_OPERATOR.GT, size);

		List<GallerySimple>


		return GalleriesResponse.builder()
				.galleries(galleries)
				.build();
	}

    public GalleryResponse getGalleryDetail(String id, Boolean isAddCookie) {

		Gallery gallery = galleryRepository.findOneById(id).orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_GALLERY));

		if (isAddCookie)
			this.increaseViews(gallery);

		CommonWriter commonWriter = UserUtils.getCommonWriter();

		GalleryDetail galleryDetail = new GalleryDetail();
		BeanUtils.copyProperties(gallery, galleryDetail);

		Integer numberOfLike = ObjectUtils.isEmpty(gallery.getUsersLiking()) ? 0 : gallery.getUsersLiking().size();
		Integer numberOfDisLike = ObjectUtils.isEmpty(gallery.getUsersDisliking()) ? 0 : gallery.getUsersDisliking().size();

		galleryDetail.setNumberOfLike(numberOfLike);
		galleryDetail.setNumberOfDislike(numberOfDisLike);
		galleryDetail.setImageUrl(apiUtils.generateGalleryUrl(CoreConst.IMAGE_SIZE_TYPE.LARGE, gallery.getId()));
		galleryDetail.setThumbnailUrl(apiUtils.generateGalleryUrl(CoreConst.IMAGE_SIZE_TYPE.SMALL, gallery.getId()));

		if (Objects.nonNull(commonWriter))
			galleryDetail.setMyFeeling(ApiUtils.getMyFeeling(commonWriter, gallery.getUsersLiking(), gallery.getUsersDisliking()));

		// 사진첩 보기의 앞, 뒤 사진을 가져온다.
		List<GallerySimple> surroundingsPrevGalleries = galleryRepository.findGalleriesById(new ObjectId(id), CoreConst.CRITERIA_OPERATOR.GT,
				ApiConst.NUMBER_OF_ITEMS_IN_SURROUNDINGS_GALLERY);
		List<GallerySimple> surroundingsNextGalleries = galleryRepository.findGalleriesById(new ObjectId(id), CoreConst.CRITERIA_OPERATOR.LT,
				ApiConst.NUMBER_OF_ITEMS_IN_SURROUNDINGS_GALLERY);

		final Integer HALF_NUMBER_OF_ITEMS_IN_SURROUNDINGS_GALLERY = ApiConst.NUMBER_OF_ITEMS_IN_SURROUNDINGS_GALLERY / 2;
		Integer prevGalleriesLimit = 0;
		Integer nextGalleriesLimit = 0;
		List<SurroundingsGallery> surroundingsGalleries = new ArrayList<>();

		// GallerySimple -> SurroundingsGallery
		Consumer<GallerySimple> extractSurroundingsGalleries = surroundingsPrevGallery -> {
			SurroundingsGallery surroundingsGallery = new SurroundingsGallery();
			BeanUtils.copyProperties(surroundingsPrevGallery, surroundingsGallery);

			surroundingsGallery.setImageUrl(apiUtils.generateGalleryUrl(CoreConst.IMAGE_SIZE_TYPE.LARGE, surroundingsPrevGallery.getId()));
			surroundingsGallery.setThumbnailUrl(apiUtils.generateGalleryUrl(CoreConst.IMAGE_SIZE_TYPE.SMALL, surroundingsPrevGallery.getId()));

			surroundingsGalleries.add(surroundingsGallery);
		};

		// 앞 사진 목록과 뒷 사진 목록이 모두 5개 이상일때
		if (surroundingsPrevGalleries.size() >= HALF_NUMBER_OF_ITEMS_IN_SURROUNDINGS_GALLERY
				&& surroundingsNextGalleries.size() >= HALF_NUMBER_OF_ITEMS_IN_SURROUNDINGS_GALLERY) {

			prevGalleriesLimit = HALF_NUMBER_OF_ITEMS_IN_SURROUNDINGS_GALLERY;
			nextGalleriesLimit = HALF_NUMBER_OF_ITEMS_IN_SURROUNDINGS_GALLERY;
		}
		// 뒷 사진 목록이 5개 미만일때
		else if (surroundingsPrevGalleries.size() >= HALF_NUMBER_OF_ITEMS_IN_SURROUNDINGS_GALLERY
				&& surroundingsNextGalleries.size() < HALF_NUMBER_OF_ITEMS_IN_SURROUNDINGS_GALLERY) {

			prevGalleriesLimit = ApiConst.NUMBER_OF_ITEMS_IN_SURROUNDINGS_GALLERY - surroundingsNextGalleries.size();
			nextGalleriesLimit = surroundingsNextGalleries.size();
		}
		// 앞 사진 목록이 5개 미만일때
		else if (surroundingsPrevGalleries.size() < HALF_NUMBER_OF_ITEMS_IN_SURROUNDINGS_GALLERY
				&& surroundingsNextGalleries.size() >= HALF_NUMBER_OF_ITEMS_IN_SURROUNDINGS_GALLERY) {

			prevGalleriesLimit = surroundingsPrevGalleries.size();
			nextGalleriesLimit = ApiConst.NUMBER_OF_ITEMS_IN_SURROUNDINGS_GALLERY - surroundingsPrevGalleries.size();
		}

		// 현재 보는 사진 포함 11장을 조합한다.
		surroundingsPrevGalleries.stream()
				.limit(prevGalleriesLimit)
				.forEach(extractSurroundingsGalleries);

		SurroundingsGallery surroundingsViewingGallery = new SurroundingsGallery();
		BeanUtils.copyProperties(gallery, surroundingsViewingGallery);

		surroundingsViewingGallery.setImageUrl(apiUtils.generateGalleryUrl(CoreConst.IMAGE_SIZE_TYPE.LARGE, surroundingsViewingGallery.getId()));
		surroundingsViewingGallery.setThumbnailUrl(apiUtils.generateGalleryUrl(CoreConst.IMAGE_SIZE_TYPE.SMALL, surroundingsViewingGallery.getId()));

		surroundingsGalleries.add(surroundingsViewingGallery);

		surroundingsNextGalleries.stream()
				.limit(nextGalleriesLimit)
				.forEach(extractSurroundingsGalleries);

		// 이 사진을 사용하는 게시물 목록
        // TODO 댓글도 보여줘야지?
        List<BoardFreeSimple> linkedPosts = null;

        List<LinkedItem> linkedItems = gallery.getLinkedItems();

        if (! ObjectUtils.isEmpty(linkedItems)) {
            List<String> ids = linkedItems.stream()
                    .filter(linkedItem -> linkedItem.getFrom().equals(CoreConst.GALLERY_FROM_TYPE.BOARD_FREE))
                    .map(LinkedItem::getId)
                    .collect(Collectors.toList());

            List<BoardFree> posts = boardFreeRepository.findByIdInAndLinkedGalleryIsTrue(ids);

            linkedPosts = posts.stream()
                    .map(post -> {
                        BoardFreeSimple boardFreeSimple = new BoardFreeSimple();
                        BeanUtils.copyProperties(post, boardFreeSimple);

                        return boardFreeSimple;
                    })
                    .collect(Collectors.toList());
        }

		return GalleryResponse.builder()
				.gallery(galleryDetail)
				.surroundingsGalleries(surroundingsGalleries)
          		.linkedPosts(linkedPosts)
				.build();
	}

	/**
	 * 사진 올리기
     */
	public Gallery uploadImage(CommonWriter writer, String fileName, long size, String contentType, byte[] bytes) {

		// hash를 뽑아 DB에 같은게 있는지 찾아보고, 있으면 찾은걸 응답.
		String hash = DigestUtils.md5DigestAsHex(bytes);
		Optional<Gallery> oGallery = galleryRepository.findOneByHashAndStatusStatus(hash, CoreConst.GALLERY_STATUS_TYPE.ENABLE);

		if (oGallery.isPresent())
			return oGallery.get();

		Gallery gallery = Gallery.builder()
				.contentType(contentType)
				.writer(writer)
				.status(
						GalleryStatus.builder()
								.status(CoreConst.GALLERY_STATUS_TYPE.TEMP)
								.build())
				.fileName(fileName)
				.size(size)
				.fileSize(size)
				.hash(DigestUtils.md5DigestAsHex(bytes))
				.build();

		galleryRepository.save(gallery);

		// 사진 포맷.
		String splitContentType[] = StringUtils.split(contentType, "/");
		String formatName = splitContentType[1];

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

		log.debug("gallery=\n{}", gallery);

		return gallery;

	}

	// 이미지 가져오기.
	public ByteArrayOutputStream getGalleryOutStream(String id, String contentType, CoreConst.IMAGE_TYPE imageType) {

		ObjectId objId = new ObjectId(id);
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

		String formatName = StringUtils.split(contentType, "/")[1];

		Path filePath = Paths.get(imagePath, String.valueOf(timePoint.getYear()), String.valueOf(timePoint.getMonthValue()),
				String.valueOf(timePoint.getDayOfMonth()), id + "." + formatName);

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
	 * 사진 삭제. (TEMP 일 경우에만 바로 지워진다.)
	 */
	public void deleteGallery(String id, String userId) {

		Gallery gallery = galleryRepository.findOneById(id)
                .orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_GALLERY));

		if (Objects.isNull(gallery.getWriter()))
			throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

		if (gallery.getStatus().getStatus().equals(CoreConst.GALLERY_STATUS_TYPE.TEMP)) {

			if (! userId.equals(gallery.getWriter().getUserId()))
				throw new ServiceException(ServiceError.FORBIDDEN);

			commonGalleryService.deleteGallery(id, gallery.getContentType(), false);
		}
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

	/**
	 * 글/댓글과 엮인 사진들을 업데이트/삭제 한다.
	 * 색인도 포함
	 *
	 * @param galleries 아이템과 엮일 사진들
	 * @param galleriesForInsertion 사용자가 입력한 사진들
	 * @param galleryIdsForRemoval 지워지길 원하는 사진들
	 * @param fromType 아이템 타입
	 * @param itemId 아이템 ID
	 */
	public void processLinkedGalleries(List<Gallery> galleries, List<GalleryOnBoard> galleriesForInsertion, List<String> galleryIdsForRemoval,
								CoreConst.GALLERY_FROM_TYPE fromType, String itemId) {

		LinkedItem linkedItem = LinkedItem.builder()
				.id(itemId)
				.from(fromType)
				.build();

		// Galleries 와 해당 Item을 연결 한다.
		galleries.forEach(gallery -> {
			List<LinkedItem> linkedItems = gallery.getLinkedItems();

			if (Objects.isNull(linkedItems))
				linkedItems = new ArrayList<>();

			// 중복 검사
			Boolean isItemPresent = linkedItems.stream()
					.anyMatch(item -> item.getId().equals(linkedItem.getId()));

			if (! isItemPresent) {
				linkedItems.add(linkedItem);
				gallery.setLinkedItems(linkedItems);
			}

			// 사용자가 입력한 이름이 있다면 그걸 입력
			Optional<GalleryOnBoard> oGalleryOnBoard = galleriesForInsertion.stream()
					.filter(galleryOnBoard -> galleryOnBoard.getId().equals(gallery.getId()))
					.findFirst();

			if (oGalleryOnBoard.isPresent() && StringUtils.isBlank(gallery.getName()))
				gallery.setName(oGalleryOnBoard.get().getName());

			GalleryStatus status = gallery.getStatus();

			if (! status.getStatus().equals(CoreConst.GALLERY_STATUS_TYPE.ENABLE)) {
				status.setStatus(CoreConst.GALLERY_STATUS_TYPE.ENABLE);
				gallery.setStatus(status);
			}

			galleryRepository.save(gallery);

			// 엘라스틱서치 색인 요청
			commonSearchService.indexDocumentGallery(gallery.getId(), gallery.getWriter(), gallery.getName());

			if (! ObjectUtils.isEmpty(galleryIdsForRemoval)) {
				if (galleryIdsForRemoval.contains(gallery.getId()))
					galleryIdsForRemoval.remove(gallery.getId());
			}
		});

		// Galleries 와 해당 Item을 연결 해제한다. Gallery 가 지워질 수도 있음.
		if (! ObjectUtils.isEmpty(galleryIdsForRemoval)) {
			List<Gallery> galleriesForRemoval = galleryRepository.findByIdIn(galleryIdsForRemoval);

			galleriesForRemoval.forEach(gallery -> {
				if (! ObjectUtils.isEmpty(gallery.getLinkedItems()) && gallery.getStatus().getStatus().equals(CoreConst.GALLERY_STATUS_TYPE.ENABLE)) {

					List<LinkedItem> linkedItems = gallery.getLinkedItems();

					Optional<LinkedItem> oLinkedItem = linkedItems.stream()
							.filter(item -> item.getId().equals(linkedItem.getId()) && item.getFrom().equals(linkedItem.getFrom()))
							.findFirst();

					oLinkedItem.ifPresent(linkedItems::remove);

					// 모두 지움.
					if (linkedItems.size() < 1) {
						commonGalleryService.deleteGallery(gallery.getId(), gallery.getContentType(), true);
					}
					// 업데이트 처리
					else {
						gallery.setLinkedItems(linkedItems);
						galleryRepository.save(gallery);
					}
				}
			});
		}
	}

	/**
	 * 읽음수 1 증가
	 */
	private void increaseViews(Gallery gallery) {
		int views = gallery.getViews();
		gallery.setViews(++views);

		galleryRepository.save(gallery);
	}

}
