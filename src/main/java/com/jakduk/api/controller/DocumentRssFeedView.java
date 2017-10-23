package com.jakduk.api.controller;

import com.jakduk.api.common.Constants;
import com.jakduk.api.common.util.JakdukUtils;
import com.jakduk.api.common.util.UrlGenerationUtils;
import com.jakduk.api.configuration.JakdukProperties;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.simple.ArticleOnRSS;
import com.jakduk.api.service.ArticleService;
import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Content;
import com.rometools.rome.feed.rss.Description;
import com.rometools.rome.feed.rss.Item;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Jang, Pyohwan
 * @since 2016. 12. 2.
 */

@Component("documentRssFeedView")
public class DocumentRssFeedView extends AbstractRssFeedView {

	@Resource private JakdukProperties jakdukProperties;

	@Autowired private UrlGenerationUtils urlGenerationUtils;
	@Autowired private ArticleService articleService;

	/**
	 * Create a new Channel instance to hold the entries.
	 * <p>By default returns an RSS 2.0 channel, but the subclass can specify any channel.
	 */
	@Override protected Channel newFeed() {
		Channel channel = new Channel("rss_2.0");
		channel.setLink(String.format("%s/%s", jakdukProperties.getWebServerUrl(), "/rss"));
		channel.setTitle(JakdukUtils.getMessageSource("common.jakduk"));
		channel.setDescription(JakdukUtils.getMessageSource("common.jakduk.rss.description"));

		return channel;
	}

	@Override protected List<Item> buildFeedItems(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ObjectId postId = null;
		Boolean existPosts = true;
		List<Item> items = new ArrayList<>();

		do {
			List<ArticleOnRSS> posts = articleService.getBoardFreeOnRss(postId, Constants.NUMBER_OF_ITEMS_EACH_PAGES);

			if (ObjectUtils.isEmpty(posts)) {
				existPosts = false;
			} else {
				ArticleOnRSS post = posts.stream()
						.sorted(Comparator.comparing(ArticleOnRSS::getId))
						.findFirst()
						.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_ARTICLE));

				postId = new ObjectId(post.getId());
			}

			if (existPosts) {
				List<Item> eachPostsOfRss = posts.stream()
						.map(post -> {
							String url = urlGenerationUtils.generateArticleDetailUrl(post.getBoard(), post.getSeq());

							Item item = new Item();
							item.setAuthor(post.getWriter().getUsername());
							item.setTitle(post.getSubject());
							item.setUri(url);
							item.setLink(url);
							item.setDescription(createDescription(JakdukUtils.stripHtmlTag(post.getContent())));
							item.setPubDate(new ObjectId(post.getId()).getDate());

							return item;
						})
						.collect(Collectors.toList());

				items.addAll(eachPostsOfRss);

			}
		} while (existPosts);

		return items;

	}

	private Description createDescription(String content) {
		Description description = new Description();
		description.setType(Content.HTML);
		description.setValue(content);

		return description;
	}
}
