package com.jakduk.api.common;

import com.jakduk.core.common.util.CoreUtils;
import com.jakduk.core.model.simple.BoardFreeOnRSS;
import com.jakduk.core.repository.board.free.BoardFreeRepository;
import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Content;
import com.rometools.rome.feed.rss.Description;
import com.rometools.rome.feed.rss.Item;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Jang, Pyohwan
 * @since 2016. 12. 2.
 */

@Component("documentRssFeedView")
public class DocumentRssFeedView extends AbstractRssFeedView {

	@Autowired
	private BoardFreeRepository boardFreeRepository;

	@Value("${web.server.url}")
	private String webServerUrl;

	@Value("${web.board.free.path}")
	private String webBoardFreePath;

	/**
	 * Create a new Channel instance to hold the entries.
	 * <p>By default returns an RSS 2.0 channel, but the subclass can specify any channel.
	 */
	@Override protected Channel newFeed() {
		Channel channel = new Channel("rss_2.0");
		channel.setLink(String.format("%s/%s", webServerUrl, "/rss"));
		channel.setTitle(CoreUtils.getResourceBundleMessage("messages.common", "common.jakduk"));
		channel.setDescription(CoreUtils.getResourceBundleMessage("messages.common", "common.jakduk.rss.description"));
		return channel;
	}

	@Override protected List<Item> buildFeedItems(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		List<BoardFreeOnRSS> posts = boardFreeRepository.findPostsOnRss();

		return posts.stream()
				.map(post -> {
					Item item = new Item();
					item.setAuthor(post.getWriter().getUsername());
					item.setTitle(post.getSubject());
					item.setUri(String.format("%s/%s/%d", webServerUrl, webBoardFreePath, post.getSeq()));
					item.setLink(String.format("%s/%s/%d", webServerUrl, webBoardFreePath, post.getSeq()));
					item.setDescription(createDescription(CoreUtils.stripHtmlTag(post.getContent())));
					item.setPubDate(new ObjectId(post.getId()).getDate());

					return item;
				})
				.collect(Collectors.toList());
	}

	private Description createDescription(String content) {
		Description description = new Description();
		description.setType(Content.HTML);
		description.setValue(content);

		return description;
	}
}
