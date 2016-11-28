package com.jakduk.api.controller;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.model.db.Gallery;
import com.jakduk.core.service.GalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 18.
 * @desc     :
 */

@RequestMapping("/gallery")
@Controller
public class GalleryController {

	@Autowired
	private GalleryService galleryService;

	// 사진 가져오기.
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public void getGallery(@PathVariable String id,	HttpServletResponse response) {

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
	@RequestMapping(value = "/thumbnail/{id}", method = RequestMethod.GET)
	@ResponseBody
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
}
