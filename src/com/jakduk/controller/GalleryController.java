package com.jakduk.controller;

import java.io.IOException;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.LocaleResolver;

import com.jakduk.common.CommonConst;
import com.jakduk.service.CommonService;
import com.jakduk.service.GalleryService;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 18.
 * @desc     :
 */

@Controller
@RequestMapping("/gallery")
public class GalleryController {
	
	@Autowired
	private GalleryService galleryService;
	
	@Autowired
	private CommonService commonService;
	
	@Resource
	LocaleResolver localeResolver;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@RequestMapping
	public String root() {
		
		return "redirect:/gallery/list";
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model) {
		
		return "gallery/list";
	}
	
	@RequestMapping(value = "/data/list", method = RequestMethod.GET)
	public void dataList(Model model,
			@RequestParam(required = false) String id) {
		
		galleryService.getList(model, id);
		
	}
	
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public void gallery(@PathVariable String id, 
			HttpServletResponse response) throws IOException {

		Integer status = galleryService.getImage(response, id);
		
		if (!status.equals(HttpServletResponse.SC_OK)) {
			response.sendError(status);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/thumbnail/{id}", method = RequestMethod.GET)
	public void thumbnail(@PathVariable String id,
			HttpServletResponse response) throws IOException {

		Integer status = galleryService.getThumbnail(response, id);
		
		if (!status.equals(HttpServletResponse.SC_OK)) {
			response.sendError(status);
		}
	}		
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public void uploadImage(Model model,
			@RequestParam(required = true) MultipartFile file) {
		
		if (file.isEmpty()) {
		}
		
		Integer status = galleryService.uploadImage(model, file);
	}
	
	@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
	public void removeImage(@PathVariable String id, Model model) {
		
		Integer status = galleryService.removeImage(model, id);
		
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
	
	@RequestMapping(value = "/like/{id}")
	public void setGalleryLike(@PathVariable String id, Model model) {
		
		galleryService.setUserFeeling(model, id, CommonConst.FEELING_TYPE_LIKE);
	}
	
	@RequestMapping(value = "/dislike/{id}")
	public void setGalleryDislike(@PathVariable String id, Model model) {
		
		galleryService.setUserFeeling(model, id, CommonConst.FEELING_TYPE_DISLIKE);
	}	

}
