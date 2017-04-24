package com.jakduk.api.controller;

import com.jakduk.api.common.ApiConst;
import com.jakduk.api.service.BoardFreeService;
import com.jakduk.api.service.GalleryService;
import com.jakduk.core.common.CoreConst;
import com.jakduk.core.common.util.DateUtils;
import com.jakduk.core.common.util.FileUtils;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.model.db.Gallery;
import com.jakduk.core.model.db.UserPicture;
import com.jakduk.core.model.simple.BoardFreeOnSitemap;
import com.jakduk.core.service.UserPictureService;
import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.W3CDateFormat;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import org.apache.commons.lang3.CharEncoding;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 18.
 * @desc     :
 */

@Controller
public class DefaultViewController {

	@Autowired
	private GalleryService galleryService;

	@Autowired
	private UserPictureService userPictureService;

	@Autowired
	private BoardFreeService boardFreeService;

	@Value("${core.storage.user.picture.large.path}")
	private String storageUserPictureLargePath;

	@Value("${core.storage.user.picture.small.path}")
	private String storageUserPictureSmallPath;

	@Value("${web.server.url}")
	private String webServerUrl;

	@Value("${web.board.free.path}")
	private String webBoardFreePath;

	// RSS
	@RequestMapping(value = "/rss", method = RequestMethod.GET, produces = "application/*")
	public String getRss() {
		return "documentRssFeedView";
	}

	// Sitemap
	@RequestMapping(value = "/sitemap", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public void getSitemap(HttpServletResponse servletResponse) {

		try {
			WebSitemapGenerator wsg =  WebSitemapGenerator.builder(webServerUrl, null)
					.dateFormat(new W3CDateFormat(W3CDateFormat.Pattern.SECOND))
					.build();

			ObjectId postId = null;
			Boolean existPosts = true;

			do {
				List<BoardFreeOnSitemap> posts = boardFreeService.getBoardFreeOnSitemap(postId, ApiConst.NUMBER_OF_ITEMS_EACH_PAGES);

				if (ObjectUtils.isEmpty(posts)) {
					existPosts = false;
				} else {
					BoardFreeOnSitemap post = posts.stream()
							.sorted(Comparator.comparing(BoardFreeOnSitemap::getId))
							.findFirst()
							.orElseThrow(() -> new ServiceException(ServiceError.INTERNAL_SERVER_ERROR));

					postId = new ObjectId(post.getId());
				}

				if (existPosts) {
					posts.forEach(post-> {
						try {
							WebSitemapUrl url = new WebSitemapUrl
									.Options(String.format("%s/%s/%d", webServerUrl, webBoardFreePath, post.getSeq()))
									.lastMod(DateUtils.localDateTimeToDate(post.getLastUpdated()))
									.priority(0.5)
									.changeFreq(ChangeFreq.DAILY)
									.build();

							wsg.addUrl(url);
						} catch (MalformedURLException e) {
							throw new ServiceException(ServiceError.IO_EXCEPTION, e);
						}
					});
				}
			} while (existPosts);

			servletResponse.setContentType(MediaType.APPLICATION_XML_VALUE);
			servletResponse.setCharacterEncoding(CharEncoding.UTF_8);
			servletResponse.getWriter().println(wsg.writeAsStrings().get(0));

		} catch (IOException e) {
			throw new ServiceException(ServiceError.IO_EXCEPTION, e);
		}

	}

	// 사진 가져오기.
	@GetMapping("/${api.gallery.image.url.path}/{id}")
	public void getGallery(@PathVariable String id,
						   HttpServletResponse response) {

		Gallery gallery = galleryService.findOneById(id);

		ByteArrayOutputStream byteStream = galleryService.getGalleryOutStream(gallery.getId(), gallery.getContentType(),
				CoreConst.IMAGE_TYPE.FULL);
		response.setContentType(gallery.getContentType());

		try {
			byteStream.writeTo(response.getOutputStream());
		} catch (IOException e) {
			throw new ServiceException(ServiceError.NOT_FOUND_GALLERY, e);
		}
	}

	// 사진 썸네일 가져오기.
	@GetMapping("/${api.gallery.thumbnail.url.path}/{id}")
	public void getGalleyThumbnail(@PathVariable String id,
								   HttpServletResponse response) {

		Gallery gallery = galleryService.findOneById(id);

		ByteArrayOutputStream byteStream = galleryService.getGalleryOutStream(gallery.getId(), gallery.getContentType(),
				CoreConst.IMAGE_TYPE.THUMBNAIL);
		response.setContentType(gallery.getContentType());

		try {
			byteStream.writeTo(response.getOutputStream());
		} catch (IOException e) {
			throw new ServiceException(ServiceError.NOT_FOUND_GALLERY, e);
		}
	}

	// 회원 프로필 사진 가져오기.
	@RequestMapping(value = "/${api.user.picture.large.url.path}/{id}", method = RequestMethod.GET)
	public void getUserPicture(@PathVariable String id,
							 HttpServletResponse response) {

		UserPicture userPicture = userPictureService.findOneById(id);

		ObjectId objectId = new ObjectId(userPicture.getId());
		LocalDate localDate = objectId.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		try {
			ByteArrayOutputStream byteStream = FileUtils.readImageFile(storageUserPictureLargePath, localDate, userPicture.getId(), userPicture.getContentType());
			response.setContentType(userPicture.getContentType());

			byteStream.writeTo(response.getOutputStream());
		} catch (IOException e) {
			throw new ServiceException(ServiceError.IO_EXCEPTION, e);
		}
	}

	// 회원 프로필 작은 사진 가져오기.
	@RequestMapping(value = "/${api.user.picture.small.url.path}/{id}", method = RequestMethod.GET)
	public void getUserSmallPicture(@PathVariable String id,
									HttpServletResponse response) {

		UserPicture userPicture = userPictureService.findOneById(id);

		ObjectId objectId = new ObjectId(userPicture.getId());
		LocalDate localDate = objectId.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		try {
			ByteArrayOutputStream byteStream = FileUtils.readImageFile(storageUserPictureSmallPath, localDate, userPicture.getId(), userPicture.getContentType());
			response.setContentType(userPicture.getContentType());

			byteStream.writeTo(response.getOutputStream());
		} catch (IOException e) {
			throw new ServiceException(ServiceError.IO_EXCEPTION, e);
		}
	}

}
