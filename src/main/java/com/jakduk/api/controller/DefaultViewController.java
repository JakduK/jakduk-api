package com.jakduk.api.controller;

import com.jakduk.api.common.Constants;
import com.jakduk.api.common.util.DateUtils;
import com.jakduk.api.common.util.FileUtils;
import com.jakduk.api.common.util.UrlGenerationUtils;
import com.jakduk.api.configuration.JakdukProperties;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.db.Gallery;
import com.jakduk.api.model.db.UserPicture;
import com.jakduk.api.model.simple.ArticleOnSitemap;
import com.jakduk.api.service.ArticleService;
import com.jakduk.api.service.GalleryService;
import com.jakduk.api.service.UserPictureService;
import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.W3CDateFormat;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
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

	@Resource private JakdukProperties jakdukProperties;
	@Resource private JakdukProperties.Storage storageProperties;

	@Autowired private UrlGenerationUtils urlGenerationUtils;
	@Autowired private GalleryService galleryService;
	@Autowired private UserPictureService userPictureService;
	@Autowired private ArticleService articleService;

	// RSS
	@RequestMapping(value = "/rss", method = RequestMethod.GET, produces = "application/*")
	public String getRss() {
		return "documentRssFeedView";
	}

	// Sitemap
	@GetMapping(value = "/sitemap", produces = MediaType.APPLICATION_XML_VALUE)
	public void getSitemap(HttpServletResponse servletResponse) {

		try {
			WebSitemapGenerator wsg =  WebSitemapGenerator.builder(jakdukProperties.getWebServerUrl(), null)
					.dateFormat(new W3CDateFormat(W3CDateFormat.Pattern.SECOND))
					.build();

			ObjectId postId = null;
			Boolean existPosts = true;

			do {
				List<ArticleOnSitemap> articles = articleService.getBoardFreeOnSitemap(postId, Constants.NUMBER_OF_ITEMS_EACH_PAGES);

				if (CollectionUtils.isEmpty(articles)) {
					existPosts = false;
				} else {
					ArticleOnSitemap article = articles.stream()
							.sorted(Comparator.comparing(ArticleOnSitemap::getId))
							.findFirst()
							.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_ARTICLE));

					postId = new ObjectId(article.getId());
				}

				if (existPosts) {
					articles.forEach(post-> {
						try {
							WebSitemapUrl url = new WebSitemapUrl
									.Options(urlGenerationUtils.generateArticleDetailUrl(post.getBoard(), post.getSeq()))
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
			servletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
			servletResponse.getWriter().println(wsg.writeAsStrings().get(0));

		} catch (IOException e) {
			throw new ServiceException(ServiceError.IO_EXCEPTION, e);
		}

	}

	// 사진 가져오기.
	@GetMapping("/${jakduk.api-url-path.gallery-image}/{id}")
	public void getGallery(@PathVariable String id,
						   HttpServletResponse response) {

		Gallery gallery = galleryService.findOneById(id);

		ByteArrayOutputStream byteStream = galleryService.getGalleryOutStream(gallery.getId(), gallery.getContentType(),
				Constants.IMAGE_TYPE.FULL);
		response.setContentType(gallery.getContentType());

		try {
			byteStream.writeTo(response.getOutputStream());
		} catch (IOException e) {
			throw new ServiceException(ServiceError.NOT_FOUND_GALLERY, e);
		}
	}

	// 사진 썸네일 가져오기.
	@GetMapping("/${jakduk.api-url-path.gallery-thumbnail}/{id}")
	public void getGalleyThumbnail(@PathVariable String id,
								   HttpServletResponse response) {

		Gallery gallery = galleryService.findOneById(id);

		ByteArrayOutputStream byteStream = galleryService.getGalleryOutStream(gallery.getId(), gallery.getContentType(),
				Constants.IMAGE_TYPE.THUMBNAIL);
		response.setContentType(gallery.getContentType());

		try {
			byteStream.writeTo(response.getOutputStream());
		} catch (IOException e) {
			throw new ServiceException(ServiceError.NOT_FOUND_GALLERY, e);
		}
	}

	// 회원 프로필 사진 가져오기.
	@RequestMapping(value = "/${jakduk.api-url-path.user-picture-large}/{id}", method = RequestMethod.GET)
	public void getUserPicture(@PathVariable String id,
							 HttpServletResponse response) {

		UserPicture userPicture = userPictureService.findOneById(id);

		ObjectId objectId = new ObjectId(userPicture.getId());
		LocalDate localDate = objectId.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		try {
			ByteArrayOutputStream byteStream = FileUtils.readImageFile(storageProperties.getUserPictureLargePath(), localDate, userPicture.getId(), userPicture.getContentType());
			response.setContentType(userPicture.getContentType());

			byteStream.writeTo(response.getOutputStream());
		} catch (IOException e) {
			throw new ServiceException(ServiceError.IO_EXCEPTION, e);
		}
	}

	// 회원 프로필 작은 사진 가져오기.
	@RequestMapping(value = "/${jakduk.api-url-path.user-picture-small}/{id}", method = RequestMethod.GET)
	public void getUserSmallPicture(@PathVariable String id,
									HttpServletResponse response) {

		UserPicture userPicture = userPictureService.findOneById(id);

		ObjectId objectId = new ObjectId(userPicture.getId());
		LocalDate localDate = objectId.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		try {
			ByteArrayOutputStream byteStream = FileUtils.readImageFile(storageProperties.getUserPictureSmallPath(), localDate, userPicture.getId(), userPicture.getContentType());
			response.setContentType(userPicture.getContentType());

			byteStream.writeTo(response.getOutputStream());
		} catch (IOException e) {
			throw new ServiceException(ServiceError.IO_EXCEPTION, e);
		}
	}

}
