package com.jakduk.service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.jakduk.authentication.common.CommonPrincipal;
import com.jakduk.common.CommonConst;
import com.jakduk.dao.BoardFreeOnBest;
import com.jakduk.dao.BoardFreeOnFreeView;
import com.jakduk.dao.JakdukDAO;
import com.jakduk.model.db.BoardCategory;
import com.jakduk.model.db.BoardFree;
import com.jakduk.model.db.BoardFreeComment;
import com.jakduk.model.db.Gallery;
import com.jakduk.model.elasticsearch.BoardFreeOnES;
import com.jakduk.model.embedded.BoardCommentStatus;
import com.jakduk.model.embedded.BoardHistory;
import com.jakduk.model.embedded.BoardImage;
import com.jakduk.model.embedded.BoardItem;
import com.jakduk.model.embedded.BoardStatus;
import com.jakduk.model.embedded.CommonFeelingUser;
import com.jakduk.model.embedded.CommonWriter;
import com.jakduk.model.embedded.GalleryStatus;
import com.jakduk.model.simple.BoardFreeOnComment;
import com.jakduk.model.simple.BoardFreeOnList;
import com.jakduk.model.web.BoardFreeWrite;
import com.jakduk.model.web.BoardListInfo;
import com.jakduk.repository.BoardCategoryRepository;
import com.jakduk.repository.BoardFreeCommentRepository;
import com.jakduk.repository.BoardFreeOnListRepository;
import com.jakduk.repository.BoardFreeRepository;
import com.jakduk.repository.GalleryRepository;

@Service
public class BoardFreeService {
	
	@Value("${kakao.javascript.key}")
	private String kakaoJavascriptKey;
	
	@Autowired
	private BoardFreeRepository boardFreeRepository;
	
	@Autowired
	private BoardFreeOnListRepository boardFreeOnListRepository;
	
	@Autowired
	private BoardCategoryRepository boardCategoryRepository;	
	
	@Autowired
	private BoardFreeCommentRepository boardFreeCommentRepository;
	
	@Autowired
	private GalleryRepository galleryRepository;
	
	@Autowired
	private JakdukDAO jakdukDAO;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SearchService searchService;
	
	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * 자유게시판 글쓰기 페이지
	 * @param model
	 * @return
	 */
	public Model getWrite(Model model) {
		
		List<BoardCategory> boardCategorys = boardCategoryRepository.findByUsingBoard(CommonConst.BOARD_NAME_FREE);
		
		model.addAttribute("boardFreeWrite", new BoardFreeWrite());
		model.addAttribute("boardCategorys", boardCategorys);
		
		return model;
	}
	
	public Integer getEdit(Model model, int seq, HttpServletResponse response) {
		
		CommonPrincipal principal = userService.getCommonPrincipal();
		String accountId = principal.getId();
		
		if (accountId == null) {
			return HttpServletResponse.SC_UNAUTHORIZED;
		}
		
		//BoardFreeWrite boardFreeWrite = boardFreeRepository.boardFreeWriteFindOneBySeq(seq);
		BoardFree boardFree = boardFreeRepository.findOneBySeq(seq);
		
		if (boardFree == null) {
			return HttpServletResponse.SC_NOT_FOUND;
		}
		
		if (boardFree.getWriter() == null) {
			return HttpServletResponse.SC_UNAUTHORIZED;
		}
		
		if (!accountId.equals(boardFree.getWriter().getUserId())) {
			return HttpServletResponse.SC_UNAUTHORIZED;
		}
		
		BoardFreeWrite boardFreeWrite = new BoardFreeWrite();
		
		if (boardFree.getGalleries() != null) {
			List<String> images = new ArrayList<String>();
			
			for (BoardImage gallery : boardFree.getGalleries()) {
				Gallery tempGallery = galleryRepository.findOne(gallery.getId());
				
				if (tempGallery != null) {
					Map<String, String> image = new HashMap<String, String>();
					
					image.put("uid", tempGallery.getId());
					image.put("name", tempGallery.getName());
					image.put("fileName", tempGallery.getFileName());
					image.put("size", String.valueOf(tempGallery.getSize()));
					
					images.add(JSONObject.toJSONString(image));
				}
			}
			
			boardFreeWrite.setImages(images.toString());
		}
		
		boardFreeWrite.setId(boardFree.getId());
		boardFreeWrite.setSeq(boardFree.getSeq());
		boardFreeWrite.setCategoryName(boardFree.getCategoryName());
		boardFreeWrite.setSubject(boardFree.getSubject());
		boardFreeWrite.setContent(boardFree.getContent());
		boardFreeWrite.setViews(boardFree.getViews());
		boardFreeWrite.setWriter(boardFree.getWriter());
		
		List<BoardCategory> boardCategorys = boardCategoryRepository.findByUsingBoard(CommonConst.BOARD_NAME_FREE);
		
		model.addAttribute("boardFreeWrite", boardFreeWrite);
		model.addAttribute("boardCategorys", boardCategorys);
		
		return HttpServletResponse.SC_OK;
	}

	public void checkBoardFreeEdit(BoardFreeWrite boardFreeWrite, BindingResult result) {
		
		String id = boardFreeWrite.getId();
		
		if (id.isEmpty()) {
			result.rejectValue("id", "board.msg.cannot.edit");
		}
	}
	
	/**
	 * 자유게시판 글쓰기 데이터 DB에 삽입
	 * @param boardFree
	 */
	public Integer write(HttpServletRequest request, BoardFreeWrite boardFreeWrite) {
		
		CommonPrincipal principal = userService.getCommonPrincipal();
		String accountId = principal.getId();
		String username = principal.getUsername();
		String type = principal.getType();
		
		if (accountId == null) {
			return HttpServletResponse.SC_UNAUTHORIZED;
		}
		
		BoardFree boardFree = new BoardFree();

		CommonWriter writer = new CommonWriter();
		writer.setUserId(accountId);
		writer.setUsername(username);
		writer.setType(type);
		boardFree.setWriter(writer);
		boardFree.setSeq(commonService.getNextSequence(CommonConst.BOARD_NAME_FREE));
		
		BoardStatus boardStatus = new BoardStatus();
		Device device = DeviceUtils.getCurrentDevice(request);
		
		if (device.isNormal()) {
			boardStatus.setDevice(CommonConst.DEVICE_TYPE_NORMAL);
		} else if (device.isMobile()) {
			boardStatus.setDevice(CommonConst.DEVICE_TYPE_MOBILE);
		} else if (device.isTablet()) {
			boardStatus.setDevice(CommonConst.DEVICE_TYPE_TABLET);
		}
		
		boardFree.setStatus(boardStatus);
		
		List<BoardHistory> historys = new ArrayList<BoardHistory>();
		BoardHistory history = new BoardHistory();
		history.setId(new ObjectId().toString());
		history.setType(CommonConst.BOARD_HISTORY_TYPE_CREATE);
		history.setWriter(writer);
		historys.add(history);
		boardFree.setHistory(historys);
		
		JSONArray jsonArray = null;
		
		if (!boardFreeWrite.getImages().isEmpty()) {
			JSONParser jsonParser = new JSONParser();
			try {			
				jsonArray = (JSONArray) jsonParser.parse(boardFreeWrite.getImages());
				List<BoardImage> galleries = new ArrayList<BoardImage>();
				
				for (int i = 0 ; i < jsonArray.size() ; i++) {
					JSONObject obj = (JSONObject)jsonArray.get(i);
					String id = (String) obj.get("uid");
					
					Gallery gallery = galleryRepository.findOne(id);
					
					if (gallery != null) {
						BoardImage boardImage = new BoardImage();
						boardImage.setId(gallery.getId());
						galleries.add(boardImage);
					}
				}
				
				if (galleries.size() > 0) {
					boardFree.setGalleries(galleries);
				}
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		boardFree.setCategoryName(boardFreeWrite.getCategoryName());
		boardFree.setSubject(boardFreeWrite.getSubject());
		boardFree.setContent(boardFreeWrite.getContent());
		boardFree.setViews(0);

		boardFreeRepository.save(boardFree);
		
		// 글과 연동 된 사진 처리
		if (jsonArray != null) {
			BoardItem boardItem = new BoardItem();
			boardItem.setId(boardFree.getId());
			boardItem.setSeq(boardFree.getSeq());
			
			for (int i = 0 ; i < jsonArray.size() ; i++) {
				JSONObject obj = (JSONObject)jsonArray.get(i);
				String id = (String) obj.get("uid");
				String name = (String) obj.get("name");

				Gallery gallery = galleryRepository.findOne(id);

				if (gallery != null) {
					GalleryStatus status = gallery.getStatus();
					List<BoardItem> posts = gallery.getPosts();
					
					if (posts == null) {
						posts = new ArrayList<BoardItem>();
					}
					
					// 연관된 글이 겹침인지 검사하고, 연관글로 등록한다.
					long itemCount = 0;
					
					if (gallery.getPosts() != null) {
						Stream<BoardItem> sPosts = gallery.getPosts().stream();
						itemCount = sPosts.filter(item -> item.getId().equals(boardItem.getId())).count();
					}
					
					if (itemCount == 0) {
						posts.add(boardItem);
						gallery.setPosts(posts);
					}
					
					if (name != null && !name.isEmpty()) {
						status.setName(CommonConst.GALLERY_NAME_STATUS_INPUT);
						gallery.setName(name);
					} else {
						status.setName(CommonConst.GALLERY_NAME_STATUS_SUBJECT);
						gallery.setName(boardFree.getSubject());
					}

					status.setFrom(CommonConst.BOARD_NAME_FREE);
					status.setStatus(CommonConst.GALLERY_STATUS_USE);
					gallery.setStatus(status);
					galleryRepository.save(gallery);
				}
			}
		}
		
		// 엘라스틱 서치 도큐먼트 생성을 위한 객체.
		BoardFreeOnES boardFreeOnEs = new BoardFreeOnES();
		boardFreeOnEs.setId(boardFree.getId());
		boardFreeOnEs.setSeq(boardFree.getSeq());
		boardFreeOnEs.setWriter(boardFree.getWriter());
		boardFreeOnEs.setCategoryName(boardFree.getCategoryName());
		
		boardFreeOnEs.setSubject(boardFree.getSubject()
				.replaceAll("<(/)?([a-zA-Z0-9]*)(\\s[a-zA-Z0-9]*=[^>]*)?(\\s)*(/)?>","")
				.replaceAll("\r|\n|&nbsp;",""));
		
		boardFreeOnEs.setContent(boardFree.getContent()					
				.replaceAll("<(/)?([a-zA-Z0-9]*)(\\s[a-zA-Z0-9]*=[^>]*)?(\\s)*(/)?>","")
				.replaceAll("\r|\n|&nbsp;",""));
		
		searchService.createDocumentBoard(boardFreeOnEs);
		
		if (logger.isInfoEnabled()) {
			logger.info("new post created. post seq=" + boardFree.getSeq() + ", subject=" + boardFree.getSubject());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("boardFree(new) = " + boardFree);
		}
		
		return HttpServletResponse.SC_OK;		
	}
	
	public Integer edit(HttpServletRequest request, BoardFreeWrite boardFreeWrite) {
		
		CommonPrincipal principal = userService.getCommonPrincipal();
		String accountId = principal.getId();
		String username = principal.getUsername();
		String type = principal.getType();
		
		if (accountId == null) {
			return HttpServletResponse.SC_UNAUTHORIZED;
		}

		CommonWriter writer = new CommonWriter();
		writer.setUserId(accountId);
		writer.setUsername(username);
		writer.setType(type);
		
		BoardFree boardFree = boardFreeRepository.findOne(boardFreeWrite.getId());
		
		if (boardFree == null) {
			return HttpServletResponse.SC_NOT_FOUND;
		}
		
		boardFree.setCategoryName(boardFreeWrite.getCategoryName());
		boardFree.setSubject(boardFreeWrite.getSubject());
		boardFree.setContent(boardFreeWrite.getContent());
		
		BoardStatus boardStatus = boardFree.getStatus();
		
		if (boardStatus == null) {
			boardStatus = new BoardStatus();
		}
		
		Device device = DeviceUtils.getCurrentDevice(request);
		
		if (device.isNormal()) {
			boardStatus.setDevice(CommonConst.DEVICE_TYPE_NORMAL);
		} else if (device.isMobile()) {
			boardStatus.setDevice(CommonConst.DEVICE_TYPE_MOBILE);
		} else if (device.isTablet()) {
			boardStatus.setDevice(CommonConst.DEVICE_TYPE_TABLET);
		}
		
		boardFree.setStatus(boardStatus);
		
		List<BoardHistory> historys = boardFree.getHistory();
		
		if (historys == null) {
			historys = new ArrayList<BoardHistory>();
		}
		
		BoardHistory history = new BoardHistory();
		history.setId(new ObjectId().toString());
		history.setType(CommonConst.BOARD_HISTORY_TYPE_EDIT);
		history.setWriter(writer);
		historys.add(history);
		boardFree.setHistory(historys);
		
		JSONArray jsonArray = null;

		if (!boardFreeWrite.getImages().isEmpty()) {
			JSONParser jsonParser = new JSONParser();
			try {			
				jsonArray = (JSONArray) jsonParser.parse(boardFreeWrite.getImages());
				List<BoardImage> galleries = new ArrayList<BoardImage>();

				for (int i = 0 ; i < jsonArray.size() ; i++) {
					JSONObject obj = (JSONObject)jsonArray.get(i);
					String id = (String) obj.get("uid");

					Gallery gallery = galleryRepository.findOne(id);

					if (gallery != null) {
						BoardImage boardImage = new BoardImage();
						boardImage.setId(gallery.getId());
						galleries.add(boardImage);
					}
				}

				if (galleries.size() > 0) {
					boardFree.setGalleries(galleries);
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		// 글과 연동 된 사진 처리
		if (jsonArray != null) {
			BoardItem boardItem = new BoardItem();
			boardItem.setId(boardFree.getId());
			boardItem.setSeq(boardFree.getSeq());
			
			for (int i = 0 ; i < jsonArray.size() ; i++) {
				JSONObject obj = (JSONObject)jsonArray.get(i);
				String id = (String) obj.get("uid");
				String name = (String) obj.get("name");

				Gallery gallery = galleryRepository.findOne(id);

				if (gallery != null) {
					GalleryStatus status = gallery.getStatus();
					List<BoardItem> posts = gallery.getPosts();
					
					if (status == null) {
						status = new GalleryStatus();
					}
					
					if (posts == null) {
						posts = new ArrayList<BoardItem>();
					}

					// 연관된 글이 겹침인지 검사하고, 연관글로 등록한다.
					long itemCount = 0;
					
					if (gallery.getPosts() != null) {
						Stream<BoardItem> sPosts = gallery.getPosts().stream();
						itemCount = sPosts.filter(item -> item.getId().equals(boardItem.getId())).count();
					}
					
					if (itemCount == 0) {
						posts.add(boardItem);
						gallery.setPosts(posts);
					}
					
					if (name != null && !name.isEmpty()) {
						status.setName(CommonConst.GALLERY_NAME_STATUS_INPUT);
						gallery.setName(name);
					} else {
						status.setName(CommonConst.GALLERY_NAME_STATUS_SUBJECT);
						gallery.setName(boardFree.getSubject());
					}
					
					status.setFrom(CommonConst.BOARD_NAME_FREE);
					status.setStatus(CommonConst.GALLERY_STATUS_USE);
					gallery.setStatus(status);
					galleryRepository.save(gallery);
				}
			}
		}
		
		boardFreeRepository.save(boardFree);
		
		// 엘라스틱 서치 도큐먼트 생성을 위한 객체.
		BoardFreeOnES boardFreeOnEs = new BoardFreeOnES();
		boardFreeOnEs.setId(boardFree.getId());
		boardFreeOnEs.setSeq(boardFree.getSeq());
		boardFreeOnEs.setWriter(boardFree.getWriter());
		boardFreeOnEs.setCategoryName(boardFree.getCategoryName());
		
		boardFreeOnEs.setSubject(boardFree.getSubject()
				.replaceAll("<(/)?([a-zA-Z0-9]*)(\\s[a-zA-Z0-9]*=[^>]*)?(\\s)*(/)?>","")
				.replaceAll("\r|\n|&nbsp;",""));
		
		boardFreeOnEs.setContent(boardFree.getContent()					
				.replaceAll("<(/)?([a-zA-Z0-9]*)(\\s[a-zA-Z0-9]*=[^>]*)?(\\s)*(/)?>","")
				.replaceAll("\r|\n|&nbsp;",""));
		
		searchService.createDocumentBoard(boardFreeOnEs);

		if (logger.isInfoEnabled()) {
			logger.info("post was edited. post seq=" + boardFree.getSeq() + ", subject=" + boardFree.getSubject());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("BoardFree(edit) = " + boardFree);
		}
		
		return HttpServletResponse.SC_OK;
	}
	
	/**
	 * 자유게시판 목록 페이지
	 * @param model
	 * @param boardListInfo
	 * @return
	 */
	public Model getFree(Model model, Locale locale, BoardListInfo boardListInfo) {

		Map<String, Date> createDate = new HashMap<String, Date>();
		List<BoardFreeOnList> posts = new ArrayList<BoardFreeOnList>();
		ArrayList<Integer> seqs = new ArrayList<Integer>();
		Long totalPosts = (long) 0;

		Integer page = boardListInfo.getPage();
		Integer size = boardListInfo.getSize();
		String categoryName = boardListInfo.getCategory();
		
		if (categoryName == null || commonService.isNumeric(categoryName)) {
			categoryName = CommonConst.BOARD_CATEGORY_ALL;
			boardListInfo.setCategory(categoryName);
		}
		
		if (page < 1) {
			page = 1;
			boardListInfo.setPage(page);
		}

		Sort sort = new Sort(Sort.Direction.DESC, Arrays.asList("seq"));
		Pageable pageable = new PageRequest(page - 1, size, sort);

		if (categoryName != null && 
				(categoryName.equals(CommonConst.BOARD_CATEGORY_NONE) || categoryName.equals(CommonConst.BOARD_CATEGORY_ALL))) {
			posts = boardFreeOnListRepository.findAll(pageable).getContent();
			totalPosts = boardFreeOnListRepository.count();
		} else {
			posts = boardFreeRepository.findByCategoryName(categoryName, pageable).getContent();
			totalPosts = boardFreeRepository.countByCategoryName(categoryName);
		}

		Pageable noticePageable = new PageRequest(0, 10, sort);
		List<BoardFreeOnList> notices = boardFreeRepository.findByNotice(noticePageable).getContent();

		for (BoardFreeOnList tempPost : posts) {
			String tempId = tempPost.getId();
			Integer tempSeq = tempPost.getSeq();

			ObjectId objId = new ObjectId(tempId);
			createDate.put(tempId, objId.getDate());

			seqs.add(tempSeq);
		}

		for (BoardFreeOnList tempNotice : notices) {
			String tempId = tempNotice.getId();
			Integer tempSeq = tempNotice.getSeq();

			ObjectId objId = new ObjectId(tempId);
			createDate.put(tempId, objId.getDate());

			seqs.add(tempSeq);
		}

		List<BoardCategory> boardCategorys = boardCategoryRepository.findByUsingBoard(CommonConst.BOARD_NAME_FREE);

		HashMap<String, String> categorys = new HashMap<String, String>();
		categorys.put("all", "board.category.all");

		for (BoardCategory category : boardCategorys) {
			categorys.put(category.getName(), category.getResName());
		}

		HashMap<String, Integer> commentCount = jakdukDAO.getBoardFreeCommentCount(seqs);
		HashMap<String, Integer> usersLikingCount = jakdukDAO.getBoardFreeUsersLikingCount(seqs);
		HashMap<String, Integer> usersDislikingCount = jakdukDAO.getBoardFreeUsersDislikingCount(seqs);

		model.addAttribute("posts", posts);
		model.addAttribute("notices", notices);
		model.addAttribute("categorys", categorys);
		model.addAttribute("boardListInfo", boardListInfo);
		model.addAttribute("totalPosts", totalPosts);
		model.addAttribute("commentCount", commentCount);
		model.addAttribute("usersLikingCount", usersLikingCount);
		model.addAttribute("usersDislikingCount", usersDislikingCount);
		model.addAttribute("createDate", createDate);
		model.addAttribute("dateTimeFormat", commonService.getDateTimeFormat(locale));

		return model;

	}
	
	/**
	 * 자유게시판 게시물 보기 페이지
	 * @param model
	 * @param seq
	 * @param boardListInfo
	 * @return
	 */
	public Integer getFreeView(Model model, Locale locale, int seq, BoardListInfo boardListInfo, Boolean isAddCookie) {
		
		try {
			BoardFree boardFree = boardFreeRepository.findOneBySeq(seq);
			
			if (boardFree == null) {
				return HttpServletResponse.SC_UNAUTHORIZED;
			}
			
			List<BoardImage> images = boardFree.getGalleries();
			BoardCategory boardCategory = boardCategoryRepository.findByName(boardFree.getCategoryName());
			
			if (isAddCookie == true) {
				int views = boardFree.getViews();
				boardFree.setViews(++views);
				boardFreeRepository.save(boardFree);
			}
			
			LocalDate date = LocalDate.now();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			DateTimeFormatter format = DateTimeFormatter.ISO_DATE;
			
			Long timeNow = sdf.parse(date.format(format)).getTime();
			
			if (images != null) {
				List<Gallery> galleries = new ArrayList<Gallery>();
				
				for(BoardImage image : images) {
					Gallery gallery = galleryRepository.findOne(image.getId());
					
					if (gallery != null) {
						galleries.add(gallery);
					}
				}
				
				if (galleries.size() > 0) {
					model.addAttribute("galleries", galleries);
				}
			}
			
			BoardFreeOnFreeView prevPost = jakdukDAO.getBoardFreeById(new ObjectId(boardFree.getId())
				, boardListInfo.getCategory(), Sort.Direction.ASC);
			BoardFreeOnFreeView nextPost = jakdukDAO.getBoardFreeById(new ObjectId(boardFree.getId())
			, boardListInfo.getCategory(), Sort.Direction.DESC);
			
			//# URL 에서 URI 를 제거, 필요 값만 사용(프로토콜, 호스트, 포트)
			//String frontUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "");
			
			model.addAttribute("post", boardFree);
			model.addAttribute("category", boardCategory);
			model.addAttribute("listInfo", boardListInfo);
			model.addAttribute("prev", prevPost);
			model.addAttribute("next", nextPost);
			model.addAttribute("timeNow", timeNow);
			model.addAttribute("dateTimeFormat", commonService.getDateTimeFormat(locale));
			model.addAttribute("kakaoKey", kakaoJavascriptKey);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return HttpServletResponse.SC_OK;
	}
	
	/**
	 * 게시물 좋아요 싫어요
	 * @param model
	 * @param seq
	 * @param status
	 * @return
	 */
	public Model setUsersFeelings(Model model, int seq, String feeling) {
		
		String errCode = CommonConst.BOARD_USERS_FEELINGS_STATUS_NONE;

		CommonPrincipal principal = userService.getCommonPrincipal();
		String userid = principal.getId();
		String username = principal.getUsername();
		
		BoardFree boardFree = boardFreeRepository.findOneBySeq(seq);
		CommonWriter writer = boardFree.getWriter();

		List<CommonFeelingUser> usersLiking = boardFree.getUsersLiking();
		List<CommonFeelingUser> usersDisliking = boardFree.getUsersDisliking();

		if (usersLiking == null) {
			usersLiking = new ArrayList<CommonFeelingUser>(); 
		}
		
		if (usersDisliking == null) {
			usersDisliking = new ArrayList<CommonFeelingUser>(); 
		}
		
		if (userid != null && username != null) {
			if (writer != null && userid.equals(writer.getUserId())) {
				errCode = CommonConst.BOARD_USERS_FEELINGS_STATUS_WRITER;
			}

			if (errCode.equals(CommonConst.BOARD_USERS_FEELINGS_STATUS_NONE)) {
				for (CommonFeelingUser boardUser : usersLiking) {
					if (boardUser != null && userid.equals(boardUser.getUserId())) {
						errCode = CommonConst.BOARD_USERS_FEELINGS_STATUS_ALREADY;
						break;
					}
				}
			}
			
			if (errCode.equals(CommonConst.BOARD_USERS_FEELINGS_STATUS_NONE)) {
				for (CommonFeelingUser boardUser : usersDisliking) {
					if (boardUser != null && userid.equals(boardUser.getUserId())) {
						errCode = CommonConst.BOARD_USERS_FEELINGS_STATUS_ALREADY;
						break;
					}
				}
			}

			if (errCode.equals(CommonConst.BOARD_USERS_FEELINGS_STATUS_NONE)) {
				CommonFeelingUser boardUser = new CommonFeelingUser();
				boardUser.setUserId(userid);
				boardUser.setUsername(username);
				boardUser.setId(new ObjectId().toString());

				switch (feeling) {
				case CommonConst.FEELING_TYPE_LIKE:
					usersLiking.add(boardUser);
					boardFree.setUsersLiking(usersLiking);
					errCode = CommonConst.BOARD_USERS_FEELINGS_STATUS_LIKE; 
					break;
				case CommonConst.FEELING_TYPE_DISLIKE:
					usersDisliking.add(boardUser);
					boardFree.setUsersDisliking(usersDisliking);
					errCode = CommonConst.BOARD_USERS_FEELINGS_STATUS_DISLIKE;
					break;
				default:
					break;
				}
				
				boardFreeRepository.save(boardFree);
			}
		} else {
			errCode = CommonConst.BOARD_USERS_FEELINGS_STATUS_ANONYMOUS;
		}
		
		model.addAttribute("errorCode", errCode);
		model.addAttribute("numberOfLike", usersLiking.size());
		model.addAttribute("numberOfDislike", usersDisliking.size());
		
		return model;
	}
	
	public void freeCommentWrite(HttpServletRequest request, Integer seq, String content) {
		BoardFreeComment boardFreeComment = new BoardFreeComment();
		
		if (!commonService.isAnonymousUser()) {
			BoardFreeOnComment boardFreeOnComment = boardFreeRepository.boardFreeOnCommentFindBySeq(seq);
			
			BoardItem boardItem = new BoardItem();
			boardItem.setId(boardFreeOnComment.getId());
			boardItem.setSeq(boardFreeOnComment.getSeq());
			
			boardFreeComment.setBoardItem(boardItem);
			
			CommonPrincipal principal = userService.getCommonPrincipal();
			CommonWriter writer = new CommonWriter();
			writer.setUserId(principal.getId());
			writer.setUsername(principal.getUsername());
			writer.setType(principal.getType());
			boardFreeComment.setWriter(writer);
			
			boardFreeComment.setContent(content);
			
			BoardCommentStatus status = new BoardCommentStatus();
			Device device = DeviceUtils.getCurrentDevice(request);
			
			if (device.isNormal()) {
				status.setDevice(CommonConst.DEVICE_TYPE_NORMAL);
			} else if (device.isMobile()) {
				status.setDevice(CommonConst.DEVICE_TYPE_MOBILE);
			} else if (device.isTablet()) {
				status.setDevice(CommonConst.DEVICE_TYPE_TABLET);
			}
			
			boardFreeComment.setStatus(status);
			
			boardFreeCommentRepository.save(boardFreeComment);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("You can't write comment and need to login.");
			}
		}
	}
	
	public void getFreeComment(Model model, int seq, String commentId) {
		BoardFreeOnComment boardFreeOnComment = boardFreeRepository.boardFreeOnCommentFindBySeq(seq);
		
		BoardItem boardItem = new BoardItem();
		boardItem.setId(boardFreeOnComment.getId());
		boardItem.setSeq(boardFreeOnComment.getSeq());
		
		List<BoardFreeComment> comments;
		
		if (commentId != null && !commentId.isEmpty()) {
			comments  = jakdukDAO.getBoardFreeComment(seq, new ObjectId(commentId));
		} else {
			comments  = jakdukDAO.getBoardFreeComment(seq, null);
		}
		
		Integer count = boardFreeCommentRepository.countByBoardItem(boardItem);
		
		model.addAttribute("comments", comments);
		model.addAttribute("count", count);		
	}
	
	public void getFreeCommentCount(Model model, int seq) {
		BoardFreeOnComment boardFreeOnComment = boardFreeRepository.boardFreeOnCommentFindBySeq(seq);
		
		BoardItem boardItem = new BoardItem();
		boardItem.setId(boardFreeOnComment.getId());
		boardItem.setSeq(boardFreeOnComment.getSeq());
		
		Integer count = boardFreeCommentRepository.countByBoardItem(boardItem);
		
		model.addAttribute("count", count);
	}
	
	public Model setUsersCommentFeelings(Model model, int seq, String id, String feeling) {
		
		String errCode = CommonConst.BOARD_USERS_FEELINGS_STATUS_NONE;

		CommonPrincipal principal = userService.getCommonPrincipal();
		String userid = principal.getId();
		String username = principal.getUsername();
		
		BoardFreeComment boardComment = boardFreeCommentRepository.findById(id);
		CommonWriter writer = boardComment.getWriter();

		List<CommonFeelingUser> usersLiking = boardComment.getUsersLiking();
		List<CommonFeelingUser> usersDisliking = boardComment.getUsersDisliking();

		if (usersLiking == null) {
			usersLiking = new ArrayList<CommonFeelingUser>(); 
		}
		
		if (usersDisliking == null) {
			usersDisliking = new ArrayList<CommonFeelingUser>(); 
		}
		
		if (userid != null && username != null) {
			
			if (userid.equals(writer.getUserId())) {
				errCode = CommonConst.BOARD_USERS_FEELINGS_STATUS_WRITER;
			}

			if (errCode.equals(CommonConst.BOARD_USERS_FEELINGS_STATUS_NONE)) {
				for (CommonFeelingUser boardUser : usersLiking) {
					if (boardUser != null && userid.equals(boardUser.getUserId())) {
						errCode = CommonConst.BOARD_USERS_FEELINGS_STATUS_ALREADY;
						break;
					}
				}
			}
			
			if (errCode.equals(CommonConst.BOARD_USERS_FEELINGS_STATUS_NONE)) {
				for (CommonFeelingUser boardUser : usersDisliking) {
					if (boardUser != null && userid.equals(boardUser.getUserId())) {
						errCode = CommonConst.BOARD_USERS_FEELINGS_STATUS_ALREADY;
						break;
					}
				}
			}

			if (errCode.equals(CommonConst.BOARD_USERS_FEELINGS_STATUS_NONE)) {
				CommonFeelingUser boardUser = new CommonFeelingUser();
				boardUser.setUserId(userid);
				boardUser.setUsername(username);
				boardUser.setId(new ObjectId().toString());

				switch (feeling) {
				case CommonConst.FEELING_TYPE_LIKE:
					usersLiking.add(boardUser);
					boardComment.setUsersLiking(usersLiking);
					errCode = CommonConst.BOARD_USERS_FEELINGS_STATUS_LIKE; 
					break;
				case CommonConst.FEELING_TYPE_DISLIKE:
					usersDisliking.add(boardUser);
					boardComment.setUsersDisliking(usersDisliking);
					errCode = CommonConst.BOARD_USERS_FEELINGS_STATUS_DISLIKE;
					break;
				default:
					break;
				}
				
				boardFreeCommentRepository.save(boardComment);
			}
		} else {
			errCode = CommonConst.BOARD_USERS_FEELINGS_STATUS_ANONYMOUS;
		}
		
		model.addAttribute("errorCode", errCode);
		model.addAttribute("numberOfLike", usersLiking.size());
		model.addAttribute("numberOfDislike", usersDisliking.size());
		
		return model;
	}
	
	public Integer deleteFree(Model model, int seq, String type) {
		
		CommonPrincipal principal = userService.getCommonPrincipal();
		String accountId = principal.getId();
		String accountUsername = principal.getUsername();
		String accountType = principal.getType();

		CommonWriter writer = new CommonWriter();
		writer.setUserId(accountId);
		writer.setUsername(accountUsername);
		writer.setType(accountType);
		
		if (accountId == null) {
			return HttpServletResponse.SC_UNAUTHORIZED;
		}
		
		BoardFree boardFree = boardFreeRepository.findOneBySeq(seq);
		
		if (boardFree.getWriter() == null) {
			return HttpServletResponse.SC_UNAUTHORIZED;
		}
		
		if (!accountId.equals(boardFree.getWriter().getUserId())) {
			return HttpServletResponse.SC_UNAUTHORIZED;
		}
		
		BoardItem boardItem = new BoardItem();
		boardItem.setId(boardFree.getId());
		boardItem.setSeq(boardFree.getSeq());
		
		Integer count = boardFreeCommentRepository.countByBoardItem(boardItem);
		
		if (type.equals(CommonConst.BOARD_DELETE_TYPE_POSTONLY) && count < 1) {
			return HttpServletResponse.SC_NOT_ACCEPTABLE;
		} else if (type.equals(CommonConst.BOARD_DELETE_TYPE_ALL) && count > 0) {
			return HttpServletResponse.SC_NOT_ACCEPTABLE;
		}
		
		// 글이 지워질 때, 연동된 사진도 끊어주어야 한다.
		// 근데 사진을 지워야 하나 말아야 하는지는 고민해보자. 왜냐하면 연동된 글이 없을수도 있지 않나?
		
		switch (type) {
		case CommonConst.BOARD_DELETE_TYPE_POSTONLY:
			
			boardFree.setContent(null);
			boardFree.setSubject(null);
			boardFree.setWriter(null);
			
			List<BoardHistory> historys = boardFree.getHistory();
			
			if (historys == null) {
				historys = new ArrayList<BoardHistory>();
			}
			
			BoardHistory history = new BoardHistory();
			history.setId(new ObjectId().toString());
			history.setType(CommonConst.BOARD_HISTORY_TYPE_DELETE);
			history.setWriter(writer);
			historys.add(history);
			boardFree.setHistory(historys);
			
			BoardStatus boardStatus = boardFree.getStatus();
			
			if (boardStatus == null) {
				boardStatus = new BoardStatus();
			}
			
			boardStatus.setDelete(CommonConst.BOARD_HISTORY_TYPE_DELETE);
			boardFree.setStatus(boardStatus);
			
			boardFreeRepository.save(boardFree);

			if (logger.isInfoEnabled()) {
				logger.info("A post was deleted(post only). post seq=" + boardFree.getSeq() + ", subject=" + boardFree.getSubject());
			}

			if (logger.isDebugEnabled()) {
				logger.debug("Delete(post only) post. BoardFree = " + boardFree);
			}
			
			break;
			
		case CommonConst.BOARD_DELETE_TYPE_ALL:
			boardFreeRepository.delete(boardFree);

			if (logger.isInfoEnabled()) {
				logger.info("A post was deleted(all). post seq=" + boardFree.getSeq() + ", subject=" + boardFree.getSubject());
			}

			if (logger.isDebugEnabled()) {
				logger.debug("Delete(all) post. BoardFree = " + boardFree);
			}
			
			break;
		}
		
		searchService.deleteDocumentBoard(boardFree.getId());
		
		return HttpServletResponse.SC_OK;
	}
	
	public Integer setNotice(int seq, String type) {
		
		if (!commonService.isAdmin()) {
			return HttpServletResponse.SC_UNAUTHORIZED;
		}
		
		CommonPrincipal principal = userService.getCommonPrincipal();
		String accountId = principal.getId();
		String accountUsername = principal.getUsername();
		String accountType = principal.getType();

		CommonWriter writer = new CommonWriter();
		writer.setUserId(accountId);
		writer.setUsername(accountUsername);
		writer.setType(accountType);
		
		if (accountId == null) {
			return HttpServletResponse.SC_UNAUTHORIZED;
		}
		
		BoardFree boardFree = boardFreeRepository.findOneBySeq(seq);
		BoardStatus status = boardFree.getStatus();
		
		if (status == null) {
			status = new BoardStatus();
		}
		
		String notice = status.getNotice();
		String noticeType = ""; 
		
		switch (type) {
		case CommonConst.COMMON_TYPE_SET:
			if (notice != null && notice.equals(CommonConst.BOARD_HISTORY_TYPE_NOTICE)) {			
				return HttpServletResponse.SC_NOT_ACCEPTABLE;
			}
			
			status.setNotice(CommonConst.BOARD_HISTORY_TYPE_NOTICE);
			noticeType = CommonConst.BOARD_HISTORY_TYPE_NOTICE;
			break;
			
		case CommonConst.COMMON_TYPE_CANCEL:
			if (notice == null) {
				return HttpServletResponse.SC_NOT_ACCEPTABLE;
			}
			
			status.setNotice(null);
			noticeType = CommonConst.BOARD_HISTORY_TYPE_CANCEL_NOTICE;
			break;
		}
		
		boardFree.setStatus(status);
		
		if (!noticeType.isEmpty()) {
			List<BoardHistory> historys = boardFree.getHistory();
			
			if (historys == null) {
				historys = new ArrayList<BoardHistory>();
			}
			
			BoardHistory history = new BoardHistory();
			history.setId(new ObjectId().toString());
			history.setType(noticeType);
			history.setWriter(writer);
			historys.add(history);
			boardFree.setHistory(historys);
		}
		
		boardFreeRepository.save(boardFree);
		
		if (logger.isInfoEnabled()) {
			logger.info("Set notice. post seq=" + boardFree.getSeq() + ", type=" + status.getNotice());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Set notice. BoardFree = " + boardFree);
		}
		
		return HttpServletResponse.SC_OK;
	}
	
	/**
	 * 구조를 바꾸고 싶다. 너무 복잡하다.
	 * @param model
	 * @return
	 */
	public Integer getDataFreeTopList(Model model) {
		
		LocalDate date = LocalDate.now().minusWeeks(1);
		Instant instant = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
		
		HashMap<String, Integer> boardFreeCount = jakdukDAO.getBoardFreeCountOfLikeBest(new ObjectId(Date.from(instant)));
		HashMap<String, Integer> boardFreeCommentCount = jakdukDAO.getBoardFreeCountOfCommentBest(new ObjectId(Date.from(instant)));
		
		ArrayList<ObjectId> likeIds = new ArrayList<ObjectId>();
		ArrayList<ObjectId> commentIds = new ArrayList<ObjectId>();

		Iterator<?> likeIterator = boardFreeCount.entrySet().iterator();
		Iterator<?> commentIterator = boardFreeCommentCount.entrySet().iterator();
		
		// 좋아요 많은 글 뽑아내기
		while (likeIterator.hasNext()) {
			Entry<String, Integer> entry = (Entry<String, Integer>) likeIterator.next();
			ObjectId objId = new ObjectId(entry.getKey().toString());
			likeIds.add(objId);
		}
		
		List<BoardFreeOnBest> boardFreeList = jakdukDAO.getBoardFreeListOfTop(likeIds);
		
		for (BoardFreeOnBest boardFree : boardFreeList) {
			String id = boardFree.getId();
			Integer count = boardFreeCount.get(id);
			boardFree.setCount(count);
		}
		
		boardFreeList = boardFreeList.stream().sorted((h1, h2) -> h2.getCount() - h1.getCount())
				.collect(Collectors.toList());
		
		// 댓글 많은 글 뽑아내기
		while (commentIterator.hasNext()) {
			Entry<String, Integer> entry = (Entry<String, Integer>) commentIterator.next();
			ObjectId objId = new ObjectId(entry.getKey().toString());
			commentIds.add(objId);
		}
		
		List<BoardFreeOnBest> boardFreeCommentList = jakdukDAO.getBoardFreeListOfTop(commentIds);
		
		for (BoardFreeOnBest boardFree : boardFreeCommentList) {
			String id = boardFree.getId();
			Integer count = boardFreeCommentCount.get(id);
			boardFree.setCount(count);
		}
		
		boardFreeCommentList = boardFreeCommentList.stream().sorted((h1, h2) -> h2.getCount() - h1.getCount())
				.collect(Collectors.toList());
		
		model.addAttribute("topLike", boardFreeList);
		model.addAttribute("topComment", boardFreeCommentList);
		
		return HttpServletResponse.SC_OK;		
	}
}
