package com.jakduk.api.gallery;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.util.CollectionUtils;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.db.Gallery;
import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.model.embedded.GalleryStatus;
import com.jakduk.api.model.embedded.LinkedItem;
import com.jakduk.api.model.embedded.UserPictureInfo;
import com.jakduk.api.repository.gallery.GalleryRepository;

/**
 * Created by pyohwanjang on 2017. 4. 4..
 */

@DataMongoTest
public class GalleryRepositoryTest {

	@Autowired
	private GalleryRepository repository;

	private Gallery articleGallery01;
	private Gallery articleGallery02;
	private Gallery articleCommentGallery;
	private Gallery tempGallery;
	private String articleId = "5b9869fe32e37f32969c2ff2";
	@BeforeEach
	public void before() {
		articleGallery01 = Gallery.builder()
			.status(new GalleryStatus(Constants.GALLERY_STATUS_TYPE.ENABLE))
			.writer(CommonWriter.builder()
				.userId("571ccf50ccbfc325b20711c5")
				.username("test07")
				.providerId(Constants.ACCOUNT_TYPE.JAKDUK)
				.picture(UserPictureInfo.builder()
					.id("597a0d53807d710f57420aa5")
					.smallPictureUrl("https://dev-api.jakduk.com/user/picture/small/597a0d53807d710f57420aa5")
					.largePictureUrl("https://dev-api.jakduk.com/user/picture/597a0d53807d710f57420aa5")
					.build())
				.build())
			.contentType("image/jpeg")
			.name("S__3751942.jpg")
			.fileName("S__3751942.jpg")
			.size(129312L)
			.fileSize(216970L)
			.hash("1548b760af481c78d97602f2edb9015e")
			.linkedItems(new ArrayList<LinkedItem>() {{
				add(LinkedItem.builder()
					.id(articleId)
					.from(Constants.GALLERY_FROM_TYPE.ARTICLE)
					.build());
			}})
			.build();

		articleGallery02 = Gallery.builder()
			.status(new GalleryStatus(Constants.GALLERY_STATUS_TYPE.ENABLE))
			.writer(CommonWriter.builder()
				.userId("571ccf50ccbfc325b20711c5")
				.username("test07")
				.providerId(Constants.ACCOUNT_TYPE.JAKDUK)
				.picture(UserPictureInfo.builder()
					.id("597a0d53807d710f57420aa5")
					.smallPictureUrl("https://dev-api.jakduk.com/user/picture/small/597a0d53807d710f57420aa5")
					.largePictureUrl("https://dev-api.jakduk.com/user/picture/597a0d53807d710f57420aa5")
					.build())
				.build())
			.contentType("image/jpeg")
			.name("S__3751939.jpg")
			.fileName("S__3751939.jpg")
			.size(195950L)
			.fileSize(323237L)
			.hash("6f2c65f80bef87452da17a261dea843c")
			.linkedItems(new ArrayList<LinkedItem>() {{
				add(LinkedItem.builder()
					.id(articleId)
					.from(Constants.GALLERY_FROM_TYPE.ARTICLE)
					.build());
			}})
			.build();

		articleCommentGallery = Gallery.builder()
			.status(new GalleryStatus(Constants.GALLERY_STATUS_TYPE.ENABLE))
			.writer(CommonWriter.builder()
				.userId("566d68d5e4b0dfaaa5b98685")
				.username("test05")
				.providerId(Constants.ACCOUNT_TYPE.JAKDUK)
				.build())
			.contentType("image/jpeg")
			.name("공차는사진")
			.fileName("작두서버목록")
			.size(430L)
			.fileSize(430L)
			.hash("3eb21a59a4af0f2f5a968242605ecba4")
			.linkedItems(new ArrayList<LinkedItem>() {{
				add(LinkedItem.builder()
					.id("58ee422be846b60526cd3382")
					.from(Constants.GALLERY_FROM_TYPE.ARTICLE_COMMENT)
					.build());
			}})
			.build();

		tempGallery = Gallery.builder()
			.status(new GalleryStatus(Constants.GALLERY_STATUS_TYPE.TEMP))
			.writer(CommonWriter.builder()
				.userId("54ae868b3d96f79363a45e7d")
				.username("test03")
				.providerId(Constants.ACCOUNT_TYPE.JAKDUK)
				.build())
			.contentType("image/jpeg")
			.fileName("54f3bb3de4b0085b552f1a10.jpg")
			.size(292652L)
			.fileSize(292652L)
			.build();

		repository.saveAll(Arrays.asList(articleGallery01, articleGallery02, articleCommentGallery, tempGallery));
	}

	@Test
	public void findGalleriesById() {
		List<Gallery> galleries = repository.findGalleriesById(new ObjectId(articleGallery02.getId()), Constants.CRITERIA_OPERATOR.GT, 3);
		assertEquals(1, galleries.size());
		assertTrue(CollectionUtils.contains(galleries.iterator(), articleCommentGallery));
		assertFalse(CollectionUtils.contains(galleries.iterator(), articleGallery02));
	}

	@Test
	public void findByItemIdAndFromType() {
		List<Gallery> galleries = repository.findByItemIdAndFromType(new ObjectId(articleId), Constants.GALLERY_FROM_TYPE.ARTICLE, 3);
		assertEquals(2, galleries.size());
		assertTrue(CollectionUtils.contains(galleries.iterator(), articleGallery01));
		assertTrue(CollectionUtils.contains(galleries.iterator(), articleGallery02));
	}

}
