package com.jakduk.service;

import com.jakduk.common.CommonConst;
import com.jakduk.dao.JakdukDAO;
import com.jakduk.model.db.Encyclopedia;
import com.jakduk.model.db.HomeDescription;
import com.jakduk.model.simple.*;
import com.jakduk.repository.BoardFreeCommentOnHomeRepository;
import com.jakduk.repository.BoardFreeOnHomeRepository;
import com.jakduk.repository.EncyclopediaRepository;
import com.sun.syndication.feed.synd.*;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
	private BoardFreeCommentOnHomeRepository boardFreeCommentOnHomeRepository;

	public Model getHome(Model model, Locale locale) {
		
		try {
			//List<GalleryOnList> galleries = jakdukDAO.getGalleryList(Direction.DESC, CommonConst.HOME_SIZE_GALLERY, null);
			
			LocalDate date = LocalDate.now();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			DateTimeFormatter format = DateTimeFormatter.ISO_DATE;
			
			Long timeNow = sdf.parse(date.format(format)).getTime();

			//model.addAttribute("galleries", galleries);
			model.addAttribute("timeNow", timeNow);
			model.addAttribute("dateTimeFormat", commonService.getDateTimeFormat(locale));
		}  catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} 
		
		return model;
	}
	
	public Encyclopedia getEncyclopedia(String language) {
		
		Integer count = encyclopediaRepository.countByLanguage(language);
		int random = (int)(Math.random() * count) + 1;
		Encyclopedia encyclopedia = encyclopediaRepository.findOneBySeqAndLanguage(random, language);

		return encyclopedia;

	}

	// 최근 글 가져오기.
	public List<BoardFreeOnHome> getBoardLatest() {

		Sort sort = new Sort(Sort.Direction.DESC, Arrays.asList("seq"));
		Pageable pageable = new PageRequest(0, CommonConst.HOME_SIZE_POST, sort);
		
		List<BoardFreeOnHome> posts = boardFreeOnHomeRepository.findAll(pageable).getContent();

		return posts;
	}

	// 최근 가입 회원 가져오기.
	public List<UserOnHome> getUsersLatest(String language) {
		return jakdukDAO.getUserOnHome(language);
	}

	// 최근 그림 목록 가져오기.
	public List<GalleryOnList> getGalleriesLatest() {
		return jakdukDAO.getGalleryList(Direction.DESC, CommonConst.HOME_SIZE_GALLERY, null);
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
						.filter(item -> item.getProviderId().equals(CommonConst.BOARD_HISTORY_TYPE_EDIT) || item.getProviderId().equals(CommonConst.BOARD_HISTORY_TYPE_EDIT))
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

	// 최근 댓글 가져오기.
	public List<BoardFreeCommentOnHome> getBoardCommentsLatest() {
		
		Sort sort = new Sort(Sort.Direction.DESC, Arrays.asList("_id"));
		Pageable pageable = new PageRequest(0, CommonConst.HOME_SIZE_LINE_NUMBER, sort);
		
		List<BoardFreeCommentOnHome> comments = boardFreeCommentOnHomeRepository.findAll(pageable).getContent();
		
		for (BoardFreeCommentOnHome comment : comments) {
			String content = comment.getContent();

			if (Objects.nonNull(content)) {
				content = content.replaceAll("<(/)?([a-zA-Z0-9]*)(\\s[a-zA-Z0-9]*=[^>]*)?(\\s)*(/)?>","").replaceAll("\r|\n|&nbsp;","");

				Integer contentLength = content.length() + comment.getWriter().getUsername().length();

				if (contentLength > CommonConst.HOME_COMMENT_CONTENT_MAX_LENGTH) {
					content = content.substring(0, CommonConst.HOME_COMMENT_CONTENT_MAX_LENGTH - comment.getWriter().getUsername().length());
					content = String.format("%s...", content);
				}
				comment.setContent(content);
			}
		}

		return comments;
	}

	// 알림판 가져오기.
	public HomeDescription getHomeDescription() {
		
		HomeDescription homeDescription = jakdukDAO.getHomeDescription();

		if (Objects.nonNull(homeDescription)) {
			return homeDescription;
		} else {
			return new HomeDescription();
		}
	}
	
}
