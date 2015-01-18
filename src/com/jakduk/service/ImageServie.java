package com.jakduk.service;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.jakduk.authentication.common.CommonPrincipal;
import com.jakduk.model.db.Image;
import com.jakduk.model.embedded.BoardWriter;
import com.jakduk.repository.ImageRepository;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 18.
 * @desc     :
 */

@Service
public class ImageServie {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ImageRepository imageRepository;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	public Integer uploadImage(Model model, MultipartFile file) {
		
		try {
			Image image = new Image();
			
			CommonPrincipal principal = userService.getCommonPrincipal();
			String userid = principal.getId();
			String username = principal.getUsername();
			String type = principal.getType();

			BoardWriter writer = new BoardWriter();
			writer.setUserId(userid);
			writer.setUsername(username);
			writer.setType(type);
			image.setWriter(writer);
			image.setName(file.getOriginalFilename());
			image.setSize(file.getSize());
			image.setContentType(file.getContentType());
			imageRepository.save(image);
			
			logger.debug("image=" + image);
			
			File uploadFile = new File("/home/Pyohwan/test/" + image.getId());
			file.transferTo(uploadFile);
			model.addAttribute("image", image);
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return HttpServletResponse.SC_OK;
	}
	
	public Integer getImage(Model model, String id) {
		
		Image image = imageRepository.findOne(id);
		model.addAttribute("image", image);
		
		return HttpServletResponse.SC_OK;
	}
}
