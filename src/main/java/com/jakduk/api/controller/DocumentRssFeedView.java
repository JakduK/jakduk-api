package com.jakduk.api.controller;

import com.jakduk.api.common.ApiConst;
import com.jakduk.api.common.util.CoreUtils;
import com.jakduk.api.configuration.ApiProperties;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.simple.BoardFreeOnRSS;
import com.jakduk.api.service.BoardFreeService;
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

	@Resource
	private ApiProperties apiProperties;

	@Autowired
	private BoardFreeService boardFreeService;

	/**
	 * Create a new Channel instance to hold the entries.
	 * <p>By default returns an RSS 2.0 channel, but the subclass can specify any channel.
	 */
	@Override protected Channel newFeed() {
		Channel channel = new Channel("rss_2.0");
		channel.setLink(String.format("%s/%s", apiProperties.getWebServerUrl(), "/rss"));
		channel.setTitle(CoreUtils.getResourceBundleMessage("messages.common", "common.jakduk"));
		channel.setDescription(CoreUtils.getResourceBundleMessage("messages.common", "common.jakduk.rss.description"));

		return channel;
	}

	@Override protected List<Item> buildFeedItems(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ObjectId postId = null;
		Boolean existPosts = true;
		List<Item> items = new ArrayList<>();

		do {
			List<BoardFreeOnRSS> posts = boardFreeService.getBoardFreeOnRss(postId, ApiConst.NUMBER_OF_ITEMS_EACH_PAGES);

			if (ObjectUtils.isEmpty(posts)) {
				existPosts = false;
			} else {
				BoardFreeOnRSS post = posts.stream()
						.sorted(Comparator.comparing(BoardFreeOnRSS::getId))
						.findFirst()
						.orElseThrow(() -> new ServiceException(ServiceError.INTERNAL_SERVER_ERROR));

				postId = new ObjectId(post.getId());
			}

			if (existPosts) {
				List<Item> eachPostsOfRss = posts.stream()
						.map(post -> {
							String url = String.format("%s/%s/%d", apiProperties.getWebServerUrl(), apiProperties.getUrlPath().getBoardFree(), post.getSeq());

							Item item = new Item();
							item.setAuthor(post.getWriter().getUsername());
							item.setTitle(post.getSubject());
							item.setUri(url);
							item.setLink(url);
							item.setDescription(createDescription(CoreUtils.stripHtmlTag(post.getContent())));
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
