package com.jakduk.controller;

import com.jakduk.common.CommonConst;
import com.jakduk.exception.SuccessButNoContentException;
import com.jakduk.model.db.Gallery;
import com.jakduk.service.CommonService;
import com.jakduk.service.GalleryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
	
	@Resource
	LocaleResolver localeResolver;

	@RequestMapping
	public String root() {
		
		return "redirect:/gallery/list";
	}
	
	@RequestMapping(value = "/list/refresh", method = RequestMethod.GET)
	public String refreshList() {
		
		return "redirect:/gallery/list";
	}		
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model,
			HttpServletRequest request) {
		
		Locale locale = localeResolver.resolveLocale(request);
		galleryService.getList(model, locale);
		
		return "gallery/list";
	}

	// 사진 가져오기.
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public void gallery(@PathVariable String id,
						HttpServletRequest request,
						HttpServletResponse response) {

		Locale locale = localeResolver.resolveLocale(request);

		Gallery gallery = galleryService.findOneById(id);

		if (Objects.isNull(gallery))
			throw new SuccessButNoContentException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.no.such.element"));

		try {
			ByteArrayOutputStream byteStream = galleryService.getImage(locale, gallery, CommonConst.IMAGE_TYPE.FULL);

			response.setContentType(gallery.getContentType());
			byteStream.writeTo(response.getOutputStream());

		} catch (IOException exception) {
			throw new RuntimeException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.io"));
		}
	}

	// 사진 썸네일 가져오기.
	@ResponseBody
	@RequestMapping(value = "/thumbnail/{id}", method = RequestMethod.GET)
	public void thumbnail(@PathVariable String id,
						  HttpServletRequest request,
						  HttpServletResponse response) {

		Locale locale = localeResolver.resolveLocale(request);

		Gallery gallery = galleryService.findOneById(id);

		if (Objects.isNull(gallery))
			throw new SuccessButNoContentException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.no.such.element"));

		try {
			ByteArrayOutputStream byteStream = galleryService.getImage(locale, gallery, CommonConst.IMAGE_TYPE.THUMBNAIL);

			response.setContentType(gallery.getContentType());
			byteStream.writeTo(response.getOutputStream());

		} catch (IOException exception) {
			throw new RuntimeException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.io"));
		}
	}		

	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public String view(@PathVariable String id, Model model
			, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		Locale locale = localeResolver.resolveLocale(request);
		Boolean isAddCookie = commonService.addViewsCookie(request, response, CommonConst.COOKIE_NAME_BOARD_FREE, id);
		Integer status = galleryService.getGallery(model, locale, id, isAddCookie);
		
		if (!status.equals(HttpServletResponse.SC_OK)) {
			response.sendError(status);
			return null;
		}
		
		return "gallery/view";		
	}	
	
	@RequestMapping(value = "/{id}/{feeling}")
	public void setGalleryFeeling(@PathVariable String id,
								  @PathVariable CommonConst.FEELING_TYPE feeling,
								  Model model) {
		
		galleryService.setUserFeeling(model, id, feeling);
	}
}
