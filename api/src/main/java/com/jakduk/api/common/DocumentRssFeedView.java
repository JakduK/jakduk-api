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
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Jang, Pyohwan
 * @since 2016. 12. 2.
 */

@Component("documentRssFeedView")
public class DocumentRssFeedView extends AbstractRssFeedView {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private BoardFreeRepository boardFreeRepository;

	private final String link = "https://jakduk.com";

	/**
	 * Create a new Channel instance to hold the entries.
	 * <p>By default returns an RSS 2.0 channel, but the subclass can specify any channel.
	 */
	@Override protected Channel newFeed() {
		Locale locale = LocaleContextHolder.getLocale();

		Channel channel = new Channel("rss_2.0");
		channel.setLink(link + "/rss");
		channel.setTitle(messageSource.getMessage("common.jakduk", null, locale));
		channel.setDescription(messageSource.getMessage("common.jakduk.rss.description", null, locale));
		return channel;
	}

	/**
	 * Subclasses must implement this method to build feed items, given the model.
	 * <p>Note that the passed-in HTTP response is just supposed to be used for
	 * setting cookies or other HTTP headers. The built feed itself will automatically
	 * get written to the response after this method returns.
	 *
	 * @param model    the model Map
	 * @param request  in case we need locale etc. Shouldn't look at attributes.
	 * @param response in case we need to set cookies. Shouldn't write to it.
	 * @return the feed items to be added to the feed
	 * @throws Exception any exception that occurred during document building
	 * @see Item
	 */
	@Override protected List<Item> buildFeedItems(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		List<BoardFreeOnRSS> posts = boardFreeRepository.findPostsWithRss();

		List<Item> items = posts.stream()
				.map(post -> {
					Item item = new Item();
					item.setAuthor(post.getWriter().getUsername());
					item.setTitle(post.getSubject());
					item.setUri(link + "/board/free/" + post.getSeq());
					item.setLink(link + "/board/free/" + post.getSeq());
					item.setDescription(createDescription(CoreUtils.stripHtmlTag(post.getContent())));
					item.setPubDate(new ObjectId(post.getId()).getDate());

					return item;
				})
				.collect(Collectors.toList());

		return items;
	}

	private Description createDescription(String content) {
		Description description = new Description();
		description.setType(Content.HTML);
		description.setValue(content);

		return description;
	}
}
