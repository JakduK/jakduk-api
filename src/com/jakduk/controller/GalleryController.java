package com.jakduk.controller;

import java.io.IOException;

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
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@RequestMapping
	public String root() {
		
		return "redirect:/gallery/list";
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String intro(Model model) {
		
		galleryService.getList(model);
		
		return "gallery/list";
	}
	
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public void gallery(@PathVariable String id, Model model,
			HttpServletResponse response) throws IOException {

		Integer status = galleryService.getImage(response, model, id);
		
		if (!status.equals(HttpServletResponse.SC_OK)) {
			response.sendError(status);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/thumbnail/{id}", method = RequestMethod.GET)
	public void thumbnail(@PathVariable String id, Model model,
			HttpServletResponse response) throws IOException {

		Integer status = galleryService.getThumbnail(response, model, id);
		
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
	public String view(@PathVariable String id, Model model) {
		
		return "gallery/view";		
	}	

}
