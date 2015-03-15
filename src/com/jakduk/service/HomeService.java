package com.jakduk.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.jakduk.common.CommonConst;
import com.jakduk.dao.BoardFreeOnRSS;
import com.jakduk.dao.JakdukDAO;
import com.jakduk.dao.UserOnHome;
import com.jakduk.model.db.Encyclopedia;
import com.jakduk.model.db.HomeDescription;
import com.jakduk.model.simple.BoardFreeCommentOnHome;
import com.jakduk.model.simple.BoardFreeOnHome;
import com.jakduk.model.simple.GalleryOnList;
import com.jakduk.repository.BoardFreeCommentOnHomeRepository;
import com.jakduk.repository.BoardFreeOnHomeRepository;
import com.jakduk.repository.BoardFreeRepository;
import com.jakduk.repository.EncyclopediaRepository;
import com.jakduk.repository.HomeDescriptionRepository;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 4.
 * @desc     :
 */

@Service
public class HomeService {
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private JakdukDAO jakdukDAO;
	
	@Autowired
	private EncyclopediaRepository encyclopediaRepository;
	
	@Autowired
	private BoardFreeOnHomeRepository boardFreeOnHomeRepository;
	
	@Autowired
	private BoardFreeRepository boardFreeRepository;
	
	@Autowired
	private BoardFreeCommentOnHomeRepository boardFreeCommentOnHomeRepository;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	public Model getHome(Model model, Locale locale) {
		
		try {
			List<GalleryOnList> galleries = jakdukDAO.getGalleryList(Direction.DESC, CommonConst.HOME_SIZE_GALLERY, null);
			
			LocalDate date = LocalDate.now();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			DateTimeFormatter format = DateTimeFormatter.ISO_DATE;
			
			Long timeNow = sdf.parse(date.format(format)).getTime();

			model.addAttribute("galleries", galleries);
			model.addAttribute("timeNow", timeNow);
			model.addAttribute("dateTimeFormat", commonService.getDateTimeFormat(locale));
		}  catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} 
		
		return model;
	}
	
	public void getJumbotron(Model model, String language) {
		
		Integer count = encyclopediaRepository.countByLanguage(language);
		int random = (int)(Math.random() * count) + 1;
		Encyclopedia encyclopedia = encyclopediaRepository.findOneBySeqAndLanguage(random, language);
		
		model.addAttribute("encyclopedia", encyclopedia);
	}
	
	public Model getBoardLatest(Model model) {
		
		Sort sort = new Sort(Sort.Direction.DESC, Arrays.asList("seq"));
		Pageable pageable = new PageRequest(0, CommonConst.HOME_SIZE_POST, sort);
		
		List<BoardFreeOnHome> posts = boardFreeOnHomeRepository.findAll(pageable).getContent();
		
		model.addAttribute("posts", posts);
		
		return model;
	}
	
	public Model getUserLatest(Model model, String language) {
		
		List<UserOnHome> posts = jakdukDAO.getUserOnHome(language);
		
		model.addAttribute("users", posts);
		
		return model;
	}
	
	public Model getGalleryLatest(Model model) {
		
		List<GalleryOnList> galleries = jakdukDAO.getGalleryList(Direction.DESC, CommonConst.HOME_SIZE_GALLERY, null);
		
		model.addAttribute("galleries", galleries);
		
		return model;
	}
	
	public Integer getRss(HttpServletResponse response, Locale locale, MessageSource messageSource) {
		
		String link = "https://jakduk.com";
		
		SyndFeed feed = new SyndFeedImpl();
		feed.setFeedType("rss_2.0");
		feed.setEncoding("UTF-8");
		feed.setTitle(messageSource.getMessage("common.jakduk", null, locale));
		feed.setLink(link);
		feed.setDescription(messageSource.getMessage("common.jakduk.rss.description", null, locale));
		feed.setPublishedDate(new Date());
		
		List<BoardFreeOnRSS> posts = jakdukDAO.getRSS();
		
		List<SyndEntry> entries = new ArrayList<SyndEntry>();
		SyndEntry entry;
		SyndContent description;

		for (BoardFreeOnRSS post : posts) {
			entry = new SyndEntryImpl();
			entry.setTitle(post.getSubject());
			entry.setLink(link + "/board/free/" + post.getSeq());
			entry.setPublishedDate(new ObjectId(post.getId()).getDate());
			entry.setAuthor(post.getWriter().getUsername());
			
			// updateDate 되는 줄 알고 스트림 API 써가며 만들어 놨더니, XML로 안나오네.
			/*
			List<BoardHistory> history = post.getHistory();
			
			if (history != null) {
				Stream<BoardHistory> sHistory = history.stream();
				
				List<BoardHistory> uHistory = sHistory
						.filter(item -> item.getType().equals(CommonConst.BOARD_HISTORY_TYPE_EDIT) || item.getType().equals(CommonConst.BOARD_HISTORY_TYPE_EDIT))
						.sorted((h1, h2) -> h1.getId().compareTo(h2.getId()))
						.limit(1)
						.collect(Collectors.toList());
				
				if (!uHistory.isEmpty()) {
					logger.debug("phjang =" + uHistory);
					entry.setUpdatedDate(new ObjectId(uHistory.get(0).getId()).getDate());
				}
			}
			*/
			
			description = new SyndContentImpl();
			description.setType("text/plain");
			description.setValue(post.getContent().replaceAll("<(/)?([a-zA-Z0-9]*)(\\s[a-zA-Z0-9]*=[^>]*)?(\\s)*(/)?>","").replaceAll("\r|\n|&nbsp;",""));
			entry.setDescription(description);
			entries.add(entry);									
		}

		feed.setEntries(entries);
		
		SyndFeedOutput output = new SyndFeedOutput();
		
		try {
			output.output(feed, response.getWriter());
		} catch (IOException | FeedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return HttpServletResponse.SC_OK;
	}		
	
	public Model getBoardCommentLatest(Model model) {
		
		Sort sort = new Sort(Sort.Direction.DESC, Arrays.asList("_id"));
		Pageable pageable = new PageRequest(0, CommonConst.HOME_SIZE_LINE_NUMBER, sort);
		
		List<BoardFreeCommentOnHome> comments = boardFreeCommentOnHomeRepository.findAll(pageable).getContent();
		
		for (BoardFreeCommentOnHome comment : comments) {
			String content = comment.getContent().replaceAll("<(/)?([a-zA-Z0-9]*)(\\s[a-zA-Z0-9]*=[^>]*)?(\\s)*(/)?>","").replaceAll("\r|\n|&nbsp;","");
			if (content.length() > CommonConst.HOME_COMMENT_CONTENT_MAX_LENGTH) {
				content = content.substring(0, CommonConst.HOME_COMMENT_CONTENT_MAX_LENGTH);
				content = String.format("%s...", content);
			}
			comment.setContent(content);
		}
		
		model.addAttribute("comments", comments);
		
		return model;
	}
	
	public Model getHomeDescription(Model model) {
		
		HomeDescription homeDescription = jakdukDAO.getHomeDescription();
		
		model.addAttribute("homeDescription", homeDescription);
		
		return model;
	}	
	
}
