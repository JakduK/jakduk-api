package com.jakduk.api.controller;

import com.jakduk.core.common.CommonConst;
import com.jakduk.core.exception.SuccessButNoContentException;
import com.jakduk.core.model.db.Gallery;
import com.jakduk.core.service.CommonService;
import com.jakduk.core.service.GalleryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 18.
 * @desc     :
 */

@Slf4j
@Controller
@RequestMapping("/gallery")
public class GalleryController {

	@Autowired
	private GalleryService galleryService;

	@Autowired
	private CommonService commonService;

	// 사진 가져오기.
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public void getGallery(@PathVariable String id,
						Locale locale,
						HttpServletResponse response) {

		Gallery gallery = galleryService.findOneById(id);

		if (Objects.isNull(gallery))
			throw new SuccessButNoContentException(commonService.getResourceBundleMessage("messages.exception", "exception.no.such.element"));

		try {
			ByteArrayOutputStream byteStream = galleryService.getImage(locale, gallery, CommonConst.IMAGE_TYPE.FULL);

			response.setContentType(gallery.getContentType());
			byteStream.writeTo(response.getOutputStream());

		} catch (IOException exception) {
			throw new RuntimeException(commonService.getResourceBundleMessage("messages.exception", "exception.io"));
		}
	}

	// 사진 썸네일 가져오기.
	@ResponseBody
	@RequestMapping(value = "/thumbnail/{id}", method = RequestMethod.GET)
	public void getGalleyThumbnail(@PathVariable String id,
						  Locale locale,
						  HttpServletResponse response) {

		Gallery gallery = galleryService.findOneById(id);

		if (Objects.isNull(gallery))
			throw new SuccessButNoContentException(commonService.getResourceBundleMessage("messages.exception", "exception.no.such.element"));

		try {
			ByteArrayOutputStream byteStream = galleryService.getImage(locale, gallery, CommonConst.IMAGE_TYPE.THUMBNAIL);

			response.setContentType(gallery.getContentType());
			byteStream.writeTo(response.getOutputStream());

		} catch (IOException exception) {
			throw new RuntimeException(commonService.getResourceBundleMessage("messages.exception", "exception.io"));
		}
	}
}
