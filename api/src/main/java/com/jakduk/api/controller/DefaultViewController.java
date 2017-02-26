package com.jakduk.api.controller;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.common.util.FileUtils;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.model.db.Gallery;
import com.jakduk.core.model.db.UserImage;
import com.jakduk.core.service.GalleryService;
import com.jakduk.core.service.UserImageService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 18.
 * @desc     :
 */

@Controller
@RequestMapping
public class DefaultViewController {

	@Autowired
	private GalleryService galleryService;

	@Autowired
	private UserImageService userImageService;

	@Value("${core.storage.user.picture.large.path}")
	private String storageUserPictureLargePath;

	@RequestMapping(value = "/rss", method = RequestMethod.GET, produces = "application/*")
	public String getRss() {
		return "documentRssFeedView";
	}

	// 사진 가져오기.
	@RequestMapping(value = "/gallery/{id}", method = RequestMethod.GET)
	public void getGallery(@PathVariable String id,
						   HttpServletResponse response) {

		Gallery gallery = galleryService.findOneById(id);

		ByteArrayOutputStream byteStream = galleryService.getGalleryOutStream(gallery, CoreConst.IMAGE_TYPE.FULL);
		response.setContentType(gallery.getContentType());

		try {
			byteStream.writeTo(response.getOutputStream());
		} catch (IOException e) {
			throw new ServiceException(ServiceError.NOT_FOUND_GALLERY, e);
		}
	}

	// 사진 썸네일 가져오기.
	@RequestMapping(value = "/gallery/thumbnail/{id}", method = RequestMethod.GET)
	public void getGalleyThumbnail(@PathVariable String id,
								   HttpServletResponse response) {

		Gallery gallery = galleryService.findOneById(id);

		ByteArrayOutputStream byteStream = galleryService.getGalleryOutStream(gallery, CoreConst.IMAGE_TYPE.THUMBNAIL);
		response.setContentType(gallery.getContentType());

		try {
			byteStream.writeTo(response.getOutputStream());
		} catch (IOException e) {
			throw new ServiceException(ServiceError.NOT_FOUND_GALLERY, e);
		}
	}

	// 회원 프로필 사진 가져오기.
	@RequestMapping(value = "/user/picture/{id}", method = RequestMethod.GET)
	public void getUserImage(@PathVariable String id,
							 HttpServletResponse response) {

		UserImage userImage = userImageService.findOneById(id);

		ObjectId objectId = new ObjectId(userImage.getId());
		LocalDate localDate = objectId.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		try {
			ByteArrayOutputStream byteStream = FileUtils.readImageFile(storageUserPictureLargePath, localDate, userImage.getId(), userImage.getContentType());
			response.setContentType(userImage.getContentType());

			byteStream.writeTo(response.getOutputStream());
		} catch (IOException e) {
			throw new ServiceException(ServiceError.IO_EXCEPTION, e);
		}
	}

}
