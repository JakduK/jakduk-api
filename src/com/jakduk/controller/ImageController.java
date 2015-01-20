package com.jakduk.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
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

import com.jakduk.service.ImageServie;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 18.
 * @desc     :
 */

@Controller
@RequestMapping("/image")
public class ImageController {
	
	@Autowired
	private ImageServie imageService;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public void image(@PathVariable String id, Model model,
			HttpServletResponse response) {

		Integer status = imageService.getImage(response, model, id);
	}	
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public void imageUpload(Model model,
			@RequestParam(required = true) MultipartFile file) {
		
		if (file.isEmpty()) {
		}
		
		Integer status = imageService.uploadImage(model, file);
	}	

}
