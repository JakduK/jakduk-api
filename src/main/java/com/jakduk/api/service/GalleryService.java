package com.jakduk.api.service;

import com.jakduk.api.common.Constants;
import com.jakduk.api.common.rabbitmq.RabbitMQPublisher;
import com.jakduk.api.common.util.UrlGenerationUtils;
import com.jakduk.api.configuration.JakdukProperties;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.db.Article;
import com.jakduk.api.model.db.Gallery;
import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.model.embedded.GalleryStatus;
import com.jakduk.api.model.embedded.LinkedItem;
import com.jakduk.api.model.simple.ArticleSimple;
import com.jakduk.api.repository.article.ArticleRepository;
import com.jakduk.api.repository.gallery.GalleryRepository;
import com.jakduk.api.restcontroller.vo.board.GalleryOnBoard;
import com.jakduk.api.restcontroller.vo.gallery.GalleryDetail;
import com.jakduk.api.restcontroller.vo.gallery.GalleryOnList;
import com.jakduk.api.restcontroller.vo.gallery.GalleryResponse;
import com.jakduk.api.restcontroller.vo.gallery.SurroundingsGallery;
import com.jakduk.api.restcontroller.vo.home.HomeGallery;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

	@Resource private JakdukProperties.Storage storageProperties;

	@Autowired private UrlGenerationUtils urlGenerationUtils;
	@Autowired private GalleryRepository galleryRepository;
	@Autowired private ArticleRepository articleRepository;
	@Autowired private CommonGalleryService commonGalleryService;
	@Autowired private RabbitMQPublisher rabbitMQPublisher;

	public Gallery findOneById(String id) {
		return galleryRepository.findOneById(id).orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_GALLERY));
	}

	public List<Gallery> findByIdIn(List<String> galleryIds) {
		return galleryRepository.findByIdIn(galleryIds);
	}

	public List<HomeGallery>findSimpleById(ObjectId id, Integer limit) {
		return galleryRepository.findSimpleById(id, limit).stream()
				.map(HomeGallery::new)
				.peek(gallery -> {
					gallery.setImageUrl(urlGenerationUtils.generateGalleryUrl(Constants.IMAGE_SIZE_TYPE.LARGE, gallery.getId()));
					gallery.setThumbnailUrl(urlGenerationUtils.generateGalleryUrl(Constants.IMAGE_SIZE_TYPE.SMALL, gallery.getId()));
				})
				.collect(Collectors.toList());
	}

	/**
	 * 사진 목록
	 */
	public List<GalleryOnList> getGalleries(String id, Integer size) {

		ObjectId objectId = null;

		if (StringUtils.isNotBlank(id))
			objectId = new ObjectId(id);

		return galleryRepository.findGalleriesById(objectId, Constants.CRITERIA_OPERATOR.LT, size).stream()
				.map(gallery -> {
					GalleryOnList galleryOnList = new GalleryOnList();
					BeanUtils.copyProperties(gallery, galleryOnList);

					galleryOnList.setName(StringUtils.isNotBlank(gallery.getName()) ? gallery.getName() : gallery.getFileName());
					galleryOnList.setImageUrl(urlGenerationUtils.generateGalleryUrl(Constants.IMAGE_SIZE_TYPE.LARGE, gallery.getId()));
					galleryOnList.setThumbnailUrl(urlGenerationUtils.generateGalleryUrl(Constants.IMAGE_SIZE_TYPE.SMALL, gallery.getId()));

					return galleryOnList;
				})
				.collect(Collectors.toList());
	}

    public GalleryResponse getGalleryDetail(String id) {

		Gallery gallery = galleryRepository.findOneById(id)
				.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_GALLERY));

		GalleryDetail galleryDetail = new GalleryDetail();
		BeanUtils.copyProperties(gallery, galleryDetail);

		galleryDetail.setName(StringUtils.isNoneBlank(gallery.getName()) ? gallery.getName() : gallery.getFileName());
		galleryDetail.setImageUrl(urlGenerationUtils.generateGalleryUrl(Constants.IMAGE_SIZE_TYPE.LARGE, gallery.getId()));
		galleryDetail.setThumbnailUrl(urlGenerationUtils.generateGalleryUrl(Constants.IMAGE_SIZE_TYPE.SMALL, gallery.getId()));

		// 사진첩 보기의 앞, 뒤 사진을 가져온다.
		List<Gallery> surroundingsPrevGalleries = galleryRepository.findGalleriesById(new ObjectId(id), Constants.CRITERIA_OPERATOR.GT,
				Constants.NUMBER_OF_ITEMS_IN_SURROUNDINGS_GALLERY);
		List<Gallery> surroundingsNextGalleries = galleryRepository.findGalleriesById(new ObjectId(id), Constants.CRITERIA_OPERATOR.LT,
				Constants.NUMBER_OF_ITEMS_IN_SURROUNDINGS_GALLERY);

		final Integer HALF_NUMBER_OF_ITEMS_IN_SURROUNDINGS_GALLERY = Constants.NUMBER_OF_ITEMS_IN_SURROUNDINGS_GALLERY / 2;
		Integer prevGalleriesLimit = 0;
		Integer nextGalleriesLimit = 0;
		List<SurroundingsGallery> surroundingsGalleries = new ArrayList<>();

		// Gallery -> SurroundingsGallery
		Consumer<Gallery> extractSurroundingsGalleries = surroundingsPrevGallery -> {
			SurroundingsGallery surroundingsGallery = new SurroundingsGallery();
			BeanUtils.copyProperties(surroundingsPrevGallery, surroundingsGallery);

			surroundingsGallery.setImageUrl(urlGenerationUtils.generateGalleryUrl(Constants.IMAGE_SIZE_TYPE.LARGE, surroundingsPrevGallery.getId()));
			surroundingsGallery.setThumbnailUrl(urlGenerationUtils.generateGalleryUrl(Constants.IMAGE_SIZE_TYPE.SMALL, surroundingsPrevGallery.getId()));

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

			prevGalleriesLimit = Constants.NUMBER_OF_ITEMS_IN_SURROUNDINGS_GALLERY - surroundingsNextGalleries.size();
			nextGalleriesLimit = surroundingsNextGalleries.size();
		}
		// 앞 사진 목록이 5개 미만일때
		else if (surroundingsPrevGalleries.size() < HALF_NUMBER_OF_ITEMS_IN_SURROUNDINGS_GALLERY
				&& surroundingsNextGalleries.size() >= HALF_NUMBER_OF_ITEMS_IN_SURROUNDINGS_GALLERY) {

			prevGalleriesLimit = surroundingsPrevGalleries.size();
			nextGalleriesLimit = Constants.NUMBER_OF_ITEMS_IN_SURROUNDINGS_GALLERY - surroundingsPrevGalleries.size();
		}

		// 현재 보는 사진 포함 11장을 조합한다.
		surroundingsPrevGalleries.stream()
				.limit(prevGalleriesLimit)
				.forEach(extractSurroundingsGalleries);

		SurroundingsGallery surroundingsViewingGallery = new SurroundingsGallery();
		BeanUtils.copyProperties(gallery, surroundingsViewingGallery);

		surroundingsViewingGallery.setImageUrl(urlGenerationUtils.generateGalleryUrl(Constants.IMAGE_SIZE_TYPE.LARGE, surroundingsViewingGallery.getId()));
		surroundingsViewingGallery.setThumbnailUrl(urlGenerationUtils.generateGalleryUrl(Constants.IMAGE_SIZE_TYPE.SMALL, surroundingsViewingGallery.getId()));

		surroundingsGalleries.add(surroundingsViewingGallery);

		surroundingsNextGalleries.stream()
				.limit(nextGalleriesLimit)
				.forEach(extractSurroundingsGalleries);

		// 이 사진을 사용하는 게시물 목록
        // TODO 댓글도 보여줘야지?
        List<ArticleSimple> linkedPosts = null;

        List<LinkedItem> linkedItems = gallery.getLinkedItems();

        if (! ObjectUtils.isEmpty(linkedItems)) {
            List<String> ids = linkedItems.stream()
                    .filter(linkedItem -> linkedItem.getFrom().equals(Constants.GALLERY_FROM_TYPE.ARTICLE))
                    .map(LinkedItem::getId)
                    .collect(Collectors.toList());

            List<Article> posts = articleRepository.findByIdInAndLinkedGalleryIsTrue(ids);

            linkedPosts = posts.stream()
                    .map(post -> {
                        ArticleSimple articleSimple = new ArticleSimple();
                        BeanUtils.copyProperties(post, articleSimple);

                        return articleSimple;
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
		Optional<Gallery> oGallery = galleryRepository.findOneByHashAndStatusStatus(hash, Constants.GALLERY_STATUS_TYPE.ENABLE);

		if (oGallery.isPresent())
			return oGallery.get();

		Gallery gallery = Gallery.builder()
				.contentType(contentType)
				.writer(writer)
				.status(new GalleryStatus(Constants.GALLERY_STATUS_TYPE.TEMP))
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

			Path imageDirPath = Paths.get(storageProperties.getImagePath(), String.valueOf(timePoint.getYear()),
					String.valueOf(timePoint.getMonthValue()), String.valueOf(timePoint.getDayOfMonth()));

			Path thumbDirPath = Paths.get(storageProperties.getThumbnailPath(), String.valueOf(timePoint.getYear()),
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

					double scale = Constants.GALLERY_MAXIMUM_CAPACITY < size ?
							Constants.GALLERY_MAXIMUM_CAPACITY / (double) size : 1;

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
						.size(Constants.GALLERY_THUMBNAIL_SIZE_WIDTH, Constants.GALLERY_THUMBNAIL_SIZE_HEIGHT)
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
	public ByteArrayOutputStream getGalleryOutStream(String id, String contentType, Constants.IMAGE_TYPE imageType) {

		ObjectId objId = new ObjectId(id);
		Instant instant = Instant.ofEpochMilli(objId.getDate().getTime());
		LocalDateTime timePoint = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

		String imagePath = null;

		switch (imageType) {
			case FULL:
				imagePath = storageProperties.getImagePath();
				break;
			case THUMBNAIL:
				imagePath = storageProperties.getThumbnailPath();
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

		if (gallery.getStatus().getStatus().equals(Constants.GALLERY_STATUS_TYPE.TEMP)) {

			if (! userId.equals(gallery.getWriter().getUserId()))
				throw new ServiceException(ServiceError.FORBIDDEN);

			commonGalleryService.deleteGallery(id, gallery.getContentType());
		}
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
	public void processLinkedGalleries(String userId, List<Gallery> galleries, List<GalleryOnBoard> galleriesForInsertion,
									   List<String> galleryIdsForRemoval, Constants.GALLERY_FROM_TYPE fromType, String itemId) {

		LinkedItem linkedItem = LinkedItem.builder()
				.id(itemId)
				.from(fromType)
				.build();

		// Galleries 와 해당 Item을 연결 한다.
		galleries.forEach(gallery -> {
			List<LinkedItem> willBeLinkedItems = gallery.getLinkedItems();

			if (CollectionUtils.isEmpty(willBeLinkedItems))
				willBeLinkedItems = new ArrayList<>();

			// 중복 검사 해서 없으면 추가
			if (willBeLinkedItems.stream().noneMatch(item -> item.getId().equals(linkedItem.getId()))) {
				willBeLinkedItems.add(linkedItem);
				gallery.setLinkedItems(willBeLinkedItems);
			}

			// 사용자가 입력한 이름이 있다면 그걸 입력. 그림 글쓴이만 이름을 고칠 수 있다.
			if (userId.equals(gallery.getWriter().getUserId())) {
				galleriesForInsertion.stream()
						.filter(galleryOnBoard -> galleryOnBoard.getId().equals(gallery.getId()))
						.findFirst()
						.ifPresent(galleryOnBoard -> {
							gallery.setName(galleryOnBoard.getName());
						});
			}

			GalleryStatus status = gallery.getStatus();

			if (status.getStatus().equals(Constants.GALLERY_STATUS_TYPE.TEMP)) {
				status.setStatus(Constants.GALLERY_STATUS_TYPE.ENABLE);
				gallery.setStatus(status);
			}

			galleryRepository.save(gallery);

			// 엘라스틱서치 색인 요청
			rabbitMQPublisher.indexDocumentGallery(gallery.getId(), gallery.getWriter(), gallery.getName());

			if (! CollectionUtils.isEmpty(galleryIdsForRemoval) && galleryIdsForRemoval.contains(gallery.getId()))
				galleryIdsForRemoval.remove(gallery.getId());
		});

		// Galleries 와 해당 Item을 연결 해제한다. Gallery 가 지워질 수도 있음.
		if (! CollectionUtils.isEmpty(galleryIdsForRemoval)) {
			List<Gallery> galleriesForRemoval = galleryRepository.findByIdIn(galleryIdsForRemoval);

			galleriesForRemoval.forEach(gallery -> {
				List<LinkedItem> linkedItems = gallery.getLinkedItems();

				if (! CollectionUtils.isEmpty(linkedItems) &&
						gallery.getStatus().getStatus().equals(Constants.GALLERY_STATUS_TYPE.ENABLE)) {

					linkedItems.stream()
							.filter(item -> item.getId().equals(linkedItem.getId()) && item.getFrom().equals(linkedItem.getFrom()))
							.findFirst()
							.ifPresent(linkedItems::remove);

					// 모두 지움.
					if (linkedItems.size() < 1) {
						commonGalleryService.deleteGallery(gallery.getId(), gallery.getContentType());
						// 엘라스틱 서치 document 삭제.
						rabbitMQPublisher.deleteDocumentGallery(gallery.getId());
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

}
