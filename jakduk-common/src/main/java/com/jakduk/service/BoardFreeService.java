package com.jakduk.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakduk.authentication.common.CommonPrincipal;
import com.jakduk.common.CommonConst;
import com.jakduk.dao.BoardDAO;
import com.jakduk.exception.ServiceError;
import com.jakduk.exception.ServiceException;
import com.jakduk.exception.UserFeelingException;
import com.jakduk.model.db.BoardCategory;
import com.jakduk.model.db.BoardFree;
import com.jakduk.model.db.BoardFreeComment;
import com.jakduk.model.db.Gallery;
import com.jakduk.model.elasticsearch.BoardFreeOnES;
import com.jakduk.model.elasticsearch.CommentOnES;
import com.jakduk.model.elasticsearch.GalleryOnES;
import com.jakduk.model.embedded.*;
import com.jakduk.model.etc.BoardFreeOnBest;
import com.jakduk.model.simple.BoardFreeOfMinimum;
import com.jakduk.model.simple.BoardFreeOnList;
import com.jakduk.model.web.BoardFreeWrite;
import com.jakduk.model.web.BoardListInfo;
import com.jakduk.notification.SlackService;
import com.jakduk.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
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
	private BoardDAO boardDAO;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SearchService searchService;

	@Autowired
	private SlackService slackService;

	public BoardFreeOfMinimum findBoardFreeOfMinimumBySeq(Integer seq) {
		return boardFreeRepository.findBoardFreeOfMinimumBySeq(seq);
	}

	public Integer countCommentsByBoardItem(BoardItem boardItem) {
		return boardFreeCommentRepository.countByBoardItem(boardItem);
	}

	public Optional<BoardCategory>  findCategoryByCode(String code) {
		return boardCategoryRepository.findOneByCode(code);
	}

	public void saveBoardFree(BoardFree boardFree) {
		boardFreeRepository.save(boardFree);
	}

	/**
	 * 자유게시판 글쓰기 페이지
	 * @param model
	 * @return
	 */
	public Model getWrite(Model model) {

		List<BoardCategory> boardCategories = this.getFreeCategories();
		
		model.addAttribute("boardFreeWrite", new BoardFreeWrite());
		model.addAttribute("boardCategories", boardCategories);
		
		return model;
	}
	
	public Integer getEdit(Model model, int seq, HttpServletResponse response) {
		
		CommonPrincipal principal = userService.getCommonPrincipal();
		String accountId = principal.getId();
		
		if (accountId == null) {
			return HttpServletResponse.SC_UNAUTHORIZED;
		}
		
		//BoardFreeWrite boardFreeWrite = boardFreeRepository.boardFreeWriteFindOneBySeq(seq);
		Optional<BoardFree> boardFree = boardFreeRepository.findOneBySeq(seq);
		
		if (!boardFree.isPresent())
			return HttpServletResponse.SC_NOT_FOUND;

		BoardFree getBoardFree = boardFree.get();

		if (getBoardFree.getWriter() == null) {
			return HttpServletResponse.SC_UNAUTHORIZED;
		}
		
		if (!accountId.equals(getBoardFree.getWriter().getUserId())) {
			return HttpServletResponse.SC_UNAUTHORIZED;
		}
		
		BoardFreeWrite boardFreeWrite = new BoardFreeWrite();
		
		if (getBoardFree.getGalleries() != null) {
			List<String> images = new ArrayList<String>();
			
			for (BoardImage gallery : getBoardFree.getGalleries()) {
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
		
		boardFreeWrite.setId(getBoardFree.getId());
		boardFreeWrite.setSeq(getBoardFree.getSeq());
		boardFreeWrite.setCategoryName(getBoardFree.getCategory().toString());
		boardFreeWrite.setSubject(getBoardFree.getSubject());
		boardFreeWrite.setContent(getBoardFree.getContent());
		boardFreeWrite.setViews(getBoardFree.getViews());
		boardFreeWrite.setWriter(getBoardFree.getWriter());
		
		List<BoardCategory> boardCategories = this.getFreeCategories();
		
		model.addAttribute("boardFreeWrite", boardFreeWrite);
		model.addAttribute("boardCategories", boardCategories);
		
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
	 */
	/*
	public Integer write(HttpServletRequest request, BoardFreeWrite boardFreeWrite) {
		
		CommonPrincipal principal = userService.getCommonPrincipal();
		String accountId = principal.getId();
		String username = principal.getUsername();
		CommonConst.ACCOUNT_TYPE accountType = principal.getProviderId();
		
		if (accountId == null) {
			return HttpServletResponse.SC_UNAUTHORIZED;
		}
		
		BoardFree boardFree = new BoardFree();

		CommonWriter writer = new CommonWriter(accountId, username, accountType);

		boardFree.setWriter(writer);
		boardFree.setSeq(commonService.getNextSequence(CommonConst.BOARD_NAME_FREE));
		
		BoardStatus boardStatus = new BoardStatus();
		Device device = DeviceUtils.getCurrentDevice(request);
        boardStatus.setDevice(commonService.getDeviceInfo(device));

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

		// 임시 처리.
		//boardFree.setCategory(boardFreeWrite.getCategoryName());
		boardFree.setCategory(CommonConst.BOARD_CATEGORY_TYPE.ALL);
		boardFree.setSubject(boardFreeWrite.getSubject());
		boardFree.setContent(boardFreeWrite.getContent());
		boardFree.setViews(0);

		boardFreeRepository.save(boardFree);
		
		// 글과 연동 된 사진 처리
		if (jsonArray != null) {
			BoardItem boardItem = new BoardItem(boardFree.getId(), boardFree.getSeq());

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
					
					// 엘라스틱 서치 gallery 도큐먼트 생성을 위한 객체.
					GalleryOnES galleryOnES = new GalleryOnES();
					galleryOnES.setId(gallery.getId());
					galleryOnES.setWriter(gallery.getWriter());
					galleryOnES.setName(gallery.getName());
					
					searchService.createDocumentGallery(galleryOnES);
				}
			}
		}
		
		// 엘라스틱 서치 도큐먼트 생성을 위한 객체.
		BoardFreeOnES boardFreeOnEs = new BoardFreeOnES();
		boardFreeOnEs.setId(boardFree.getId());
		boardFreeOnEs.setSeq(boardFree.getSeq());
		boardFreeOnEs.setWriter(boardFree.getWriter());
		boardFreeOnEs.setCategoryName(boardFree.getCategory().toString());
		
		boardFreeOnEs.setSubject(boardFree.getSubject()
				.replaceAll("<(/)?([a-zA-Z0-9]*)(\\s[a-zA-Z0-9]*=[^>]*)?(\\s)*(/)?>","")
				.replaceAll("\r|\n|&nbsp;",""));
		
		boardFreeOnEs.setContent(boardFree.getContent()					
				.replaceAll("<(/)?([a-zA-Z0-9]*)(\\s[a-zA-Z0-9]*=[^>]*)?(\\s)*(/)?>","")
				.replaceAll("\r|\n|&nbsp;",""));
		
		searchService.createDocumentBoard(boardFreeOnEs);

		// 슬랙 알림
		slackService.sendPost(
			boardFree.getWriter().getUsername(),
			boardFree.getSubject(),
			"New post created.",
			UrlUtils.buildFullRequestUrl(
				request.getScheme(),
				request.getServerName(),
				request.getServerPort(),
				request.getContextPath(), null) + "/board/free/" + boardFree.getSeq()
		);
		
		if (log.isInfoEnabled()) {
			log.info("new post created. post seq=" + boardFree.getSeq() + ", subject=" + boardFree.getSubject());
		}

		if (log.isDebugEnabled()) {
			log.debug("boardFree(new) = " + boardFree);
		}

		return HttpServletResponse.SC_OK;		
	}
	*/

	public void addFreePost(String subject, String content, CommonConst.BOARD_CATEGORY_TYPE categoryCode, String images, CommonConst.DEVICE_TYPE device) {

		CommonPrincipal principal = userService.getCommonPrincipal();
		String accountId = principal.getId();
		String username = principal.getUsername();
		CommonConst.ACCOUNT_TYPE accountType = principal.getProviderId();

		if (Objects.isNull(accountId))
			throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

		Optional<BoardCategory> boardCategory = boardCategoryRepository.findOneByCode(categoryCode.name());

		if (!boardCategory.isPresent())
			throw new ServiceException(ServiceError.CATEGORY_NOT_FOUND);

		BoardFree boardFree = new BoardFree();

		boardFree.setCategory(categoryCode);
		boardFree.setSubject(subject);
		boardFree.setContent(content);
		boardFree.setViews(0);
		boardFree.setSeq(commonService.getNextSequence(CommonConst.BOARD_NAME_FREE));

		CommonWriter writer = new CommonWriter(accountId, username, accountType);
		boardFree.setWriter(writer);

		BoardStatus boardStatus = new BoardStatus(device);
		boardFree.setStatus(boardStatus);

		List<BoardHistory> historys = new ArrayList<>();
		BoardHistory history = new BoardHistory(new ObjectId().toString(), CommonConst.BOARD_HISTORY_TYPE.CREATE, writer);
		historys.add(history);
		boardFree.setHistory(historys);

		/*
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
		*/

		boardFreeRepository.save(boardFree);

		/*
		// 글과 연동 된 사진 처리
		if (jsonArray != null) {
			BoardItem boardItem = new BoardItem(boardFree.getId(), boardFree.getSeq());

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

					// 엘라스틱 서치 gallery 도큐먼트 생성을 위한 객체.
					GalleryOnES galleryOnES = new GalleryOnES();
					galleryOnES.setId(gallery.getId());
					galleryOnES.setWriter(gallery.getWriter());
					galleryOnES.setName(gallery.getName());

					searchService.createDocumentGallery(galleryOnES);
				}
			}
		}
		*/

		// 엘라스틱 서치 도큐먼트 생성을 위한 객체.
		/*
		BoardFreeOnES boardFreeOnEs = new BoardFreeOnES();
		boardFreeOnEs.setId(boardFree.getId());
		boardFreeOnEs.setSeq(boardFree.getSeq());
		boardFreeOnEs.setWriter(boardFree.getWriter());
		boardFreeOnEs.setCategoryName(boardFree.getCategory().toString());

		boardFreeOnEs.setSubject(boardFree.getSubject()
				.replaceAll("<(/)?([a-zA-Z0-9]*)(\\s[a-zA-Z0-9]*=[^>]*)?(\\s)*(/)?>","")
				.replaceAll("\r|\n|&nbsp;",""));

		boardFreeOnEs.setContent(boardFree.getContent()
				.replaceAll("<(/)?([a-zA-Z0-9]*)(\\s[a-zA-Z0-9]*=[^>]*)?(\\s)*(/)?>","")
				.replaceAll("\r|\n|&nbsp;",""));

		searchService.createDocumentBoard(boardFreeOnEs);
		*/

		/*
		// 슬랙 알림
		slackService.sendPost(
				boardFree.getWriter().getUsername(),
				boardFree.getSubject(),
				"New post created.",
				UrlUtils.buildFullRequestUrl(
						request.getScheme(),
						request.getServerName(),
						request.getServerPort(),
						request.getContextPath(), null) + "/board/free/" + boardFree.getSeq()
		);
		*/

		if (log.isInfoEnabled()) {
			log.info("new post created. post seq=" + boardFree.getSeq() + ", subject=" + boardFree.getSubject());
		}

		if (log.isDebugEnabled()) {
			log.debug("boardFree(new) = " + boardFree);
		}
	}

	/*
	public Integer edit(HttpServletRequest request, BoardFreeWrite boardFreeWrite) {
		
		CommonPrincipal principal = userService.getCommonPrincipal();
		String accountId = principal.getId();
		String username = principal.getUsername();
		CommonConst.ACCOUNT_TYPE accountType = principal.getProviderId();
		
		if (accountId == null) {
			return HttpServletResponse.SC_UNAUTHORIZED;
		}

		CommonWriter writer = new CommonWriter(accountId, username, accountType);

		BoardFree boardFree = boardFreeRepository.findOne(boardFreeWrite.getId());
		
		if (boardFree == null) {
			return HttpServletResponse.SC_NOT_FOUND;
		}

		// 임시 처리.
		//boardFree.setCategory(boardFreeWrite.getCategoryName());
		boardFree.setCategory(CommonConst.BOARD_CATEGORY_TYPE.ALL);
		boardFree.setSubject(boardFreeWrite.getSubject());
		boardFree.setContent(boardFreeWrite.getContent());
		
		BoardStatus boardStatus = boardFree.getStatus();
		
		if (boardStatus == null) {
			boardStatus = new BoardStatus();
		}
		
		Device device = DeviceUtils.getCurrentDevice(request);

        boardStatus.setDevice(commonService.getDeviceInfo(device));

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
			BoardItem boardItem = new BoardItem(boardFree.getId(), boardFree.getSeq());

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
					
					// 엘라스틱 서치 gallery 도큐먼트 생성을 위한 객체.
					GalleryOnES galleryOnES = new GalleryOnES();
					galleryOnES.setId(gallery.getId());
					galleryOnES.setWriter(gallery.getWriter());
					galleryOnES.setName(gallery.getName());
					
					searchService.createDocumentGallery(galleryOnES);
				}
			}
		}
		
		boardFreeRepository.save(boardFree);
		
		// 엘라스틱 서치 도큐먼트 생성을 위한 객체.
		BoardFreeOnES boardFreeOnEs = new BoardFreeOnES();
		boardFreeOnEs.setId(boardFree.getId());
		boardFreeOnEs.setSeq(boardFree.getSeq());
		boardFreeOnEs.setWriter(boardFree.getWriter());
		boardFreeOnEs.setCategoryName(boardFree.getCategory().toString());
		
		boardFreeOnEs.setSubject(boardFree.getSubject()
				.replaceAll("<(/)?([a-zA-Z0-9]*)(\\s[a-zA-Z0-9]*=[^>]*)?(\\s)*(/)?>","")
				.replaceAll("\r|\n|&nbsp;",""));
		
		boardFreeOnEs.setContent(boardFree.getContent()					
				.replaceAll("<(/)?([a-zA-Z0-9]*)(\\s[a-zA-Z0-9]*=[^>]*)?(\\s)*(/)?>","")
				.replaceAll("\r|\n|&nbsp;",""));
		
		searchService.createDocumentBoard(boardFreeOnEs);

		if (log.isInfoEnabled()) {
			log.info("post was edited. post seq=" + boardFree.getSeq() + ", subject=" + boardFree.getSubject());
		}

		if (log.isDebugEnabled()) {
			log.debug("BoardFree(edit) = " + boardFree);
		}
		
		return HttpServletResponse.SC_OK;
	}
	*/

	/**
	 * 자유게시판 말머리 목록
	 * @return 말머리 목록
     */
	public List<BoardCategory> getFreeCategories() {

		return boardDAO.getBoardCategories(commonService.getLanguageCode(LocaleContextHolder.getLocale(), null));
	}

	/**
	 * 자유게시판 글 목록
	 * @param category 말머리
	 * @param page 페이지
	 * @param size 크기
     * @return 글 목록
     */
	public Page<BoardFreeOnList> getFreePosts(CommonConst.BOARD_CATEGORY_TYPE category, Integer page, Integer size) {

		Sort sort = new Sort(Sort.Direction.DESC, Collections.singletonList("_id"));
		Pageable pageable = new PageRequest(page - 1, size, sort);
		Page<BoardFreeOnList> posts = null;

		switch (category) {
			case ALL:
				posts = boardFreeOnListRepository.findAll(pageable);
				break;
			case FOOTBALL:
			case FREE:
				posts = boardFreeOnListRepository.findByCategory(category, pageable);
				break;
		}

		return posts;
	}

	/**
	 * 자유 게시판 공지글 목록
     * @return 공지글 목록
     */
	public Page<BoardFreeOnList> getFreeNotices() {

		Sort sort = new Sort(Sort.Direction.DESC, Collections.singletonList("_id"));
		Pageable noticePageable = new PageRequest(0, 10, sort);

		return boardFreeRepository.findByNotice(noticePageable);
	}

	public Model getFreeCommentsList(Model model, Locale locale, BoardListInfo boardListInfo) {
		
		long totalComments = boardFreeCommentRepository.count();

		model.addAttribute("boardListInfo", boardListInfo);
		model.addAttribute("totalComments", totalComments);
		try {
			model.addAttribute("dateTimeFormat", new ObjectMapper().writeValueAsString(commonService.getDateTimeFormat(locale)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return model;

	}

	public Optional<BoardFree> getFreePost(int seq) {
		return boardFreeRepository.findOneBySeq(seq);
	}



	// 글 감정 표현.
	public BoardFree setFreeFeelings(Locale locale, Integer seq, CommonConst.FEELING_TYPE feeling) {
		CommonPrincipal principal = userService.getCommonPrincipal();
		String userId = principal.getId();
		String username = principal.getUsername();

		Optional<BoardFree> boardFree = boardFreeRepository.findOneBySeq(seq);
		if (!boardFree.isPresent())
			throw new ServiceException(ServiceError.POST_NOT_FOUND);

		BoardFree getBoardFree = boardFree.get();
		CommonWriter writer = getBoardFree.getWriter();

		List<CommonFeelingUser> usersLiking = getBoardFree.getUsersLiking();
		List<CommonFeelingUser> usersDisliking = getBoardFree.getUsersDisliking();

		if (Objects.isNull(usersLiking)) usersLiking = new ArrayList<>();
		if (Objects.isNull(usersDisliking)) usersDisliking = new ArrayList<>();

		// 이 게시물의 작성자라서 감정 표현을 할 수 없음
		if (userId.equals(writer.getUserId())) {
			throw new UserFeelingException(CommonConst.USER_FEELING_ERROR_CODE.WRITER.toString()
					, commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.you.are.writer"));
		}

		// 해당 회원이 좋아요를 이미 했는지 검사
		for (CommonFeelingUser feelingUser : usersLiking) {
			if (Objects.nonNull(feelingUser) && userId.equals(feelingUser.getUserId())) {
				throw new UserFeelingException(CommonConst.USER_FEELING_ERROR_CODE.ALREADY.toString()
						, commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.select.already.like"));
			}
		}

		// 해당 회원이 싫어요를 이미 했는지 검사
		for (CommonFeelingUser feelingUser : usersDisliking) {
			if (Objects.nonNull(feelingUser) && userId.equals(feelingUser.getUserId())) {
				throw new UserFeelingException(CommonConst.USER_FEELING_ERROR_CODE.ALREADY.toString()
						, commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.select.already.like"));
			}
		}

		CommonFeelingUser feelingUser = new CommonFeelingUser(new ObjectId().toString(), userId, username);

		switch (feeling) {
			case LIKE:
				usersLiking.add(feelingUser);
				getBoardFree.setUsersLiking(usersLiking);
				break;
			case DISLIKE:
				usersDisliking.add(feelingUser);
				getBoardFree.setUsersDisliking(usersDisliking);
				break;
		}

		boardFreeRepository.save(getBoardFree);

		return getBoardFree;
	}

	// 게시판 댓글 추가.
	public BoardFreeComment addFreeComment(Integer seq, String contents, CommonConst.DEVICE_TYPE device) {
		BoardFreeComment boardFreeComment = new BoardFreeComment();

		BoardFreeOfMinimum boardFreeOnComment = boardFreeRepository.findBoardFreeOfMinimumBySeq(seq);

		BoardItem boardItem = new BoardItem(boardFreeOnComment.getId(), boardFreeOnComment.getSeq());
		boardFreeComment.setBoardItem(boardItem);

		CommonPrincipal principal = userService.getCommonPrincipal();
		CommonWriter writer = new CommonWriter(principal.getId(), principal.getUsername(), principal.getProviderId());

		boardFreeComment.setWriter(writer);
		boardFreeComment.setContent(contents);

		BoardCommentStatus status = new BoardCommentStatus();
		status.setDevice(device);
		boardFreeComment.setStatus(status);

		boardFreeCommentRepository.save(boardFreeComment);

		// 엘라스틱 서치 도큐먼트 생성을 위한 객체.
		CommentOnES commentOnES = new CommentOnES();
		commentOnES.setId(boardFreeComment.getId());
		commentOnES.setWriter(boardFreeComment.getWriter());
		commentOnES.setBoardItem(boardFreeComment.getBoardItem());

		commentOnES.setContent(boardFreeComment.getContent()
				.replaceAll("<(/)?([a-zA-Z0-9]*)(\\s[a-zA-Z0-9]*=[^>]*)?(\\s)*(/)?>","")
				.replaceAll("\r|\n|&nbsp;",""));

		searchService.createDocumentComment(commentOnES);

		return boardFreeComment;
	}

	// 게시판 댓글 목록
	public List<BoardFreeComment> getFreeComments(Integer seq, String commentId) {
		BoardFreeOfMinimum boardFreeOnComment = boardFreeRepository.findBoardFreeOfMinimumBySeq(seq);

		List<BoardFreeComment> comments;
		
		if (commentId != null && !commentId.isEmpty()) {
			comments  = boardDAO.getBoardFreeComment(seq, new ObjectId(commentId));
		} else {
			comments  = boardDAO.getBoardFreeComment(seq, null);
		}

		return comments;
	}
	
	/**
	 * 자유게시판 감정 표현.
	 * @param locale
	 * @param commentId
	 * @param feeling
     * @return
     */
	public BoardFreeComment setFreeCommentFeeling(Locale locale, String commentId, CommonConst.FEELING_TYPE feeling) {

		CommonPrincipal principal = userService.getCommonPrincipal();
		String userId = principal.getId();
		String username = principal.getUsername();

		BoardFreeComment boardComment = boardFreeCommentRepository.findById(commentId);
		CommonWriter writer = boardComment.getWriter();

		List<CommonFeelingUser> usersLiking = boardComment.getUsersLiking();
		List<CommonFeelingUser> usersDisliking = boardComment.getUsersDisliking();

		if (Objects.isNull(usersLiking)) usersLiking = new ArrayList<>();
		if (Objects.isNull(usersDisliking)) usersDisliking = new ArrayList<>();

		// 이 게시물의 작성자라서 감정 표현을 할 수 없음
		if (userId.equals(writer.getUserId())) {
			throw new UserFeelingException(CommonConst.USER_FEELING_ERROR_CODE.WRITER.toString()
					, commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.you.are.writer"));
		}

		// 해당 회원이 좋아요를 이미 했는지 검사
		for (CommonFeelingUser feelingUser : usersLiking) {
			if (Objects.nonNull(feelingUser) && userId.equals(feelingUser.getUserId())) {
				throw new UserFeelingException(CommonConst.USER_FEELING_ERROR_CODE.ALREADY.toString()
						, commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.select.already.like"));
			}
		}

		// 해당 회원이 싫어요를 이미 했는지 검사
		for (CommonFeelingUser feelingUser : usersDisliking) {
			if (Objects.nonNull(feelingUser) && userId.equals(feelingUser.getUserId())) {
				throw new UserFeelingException(CommonConst.USER_FEELING_ERROR_CODE.ALREADY.toString()
						, commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.select.already.like"));
			}
		}

		CommonFeelingUser feelingUser = new CommonFeelingUser(new ObjectId().toString(), userId, username);

		switch (feeling) {
			case LIKE:
				usersLiking.add(feelingUser);
				boardComment.setUsersLiking(usersLiking);
				break;
			case DISLIKE:
				usersDisliking.add(feelingUser);
				boardComment.setUsersDisliking(usersDisliking);
				break;
		}

		boardFreeCommentRepository.save(boardComment);

		return boardComment;
	}

	/*
	public Integer deleteFree(Model model, int seq, String type) {
		
		CommonPrincipal principal = userService.getCommonPrincipal();
		String accountId = principal.getId();
		String accountUsername = principal.getUsername();
		CommonConst.ACCOUNT_TYPE accountType = principal.getProviderId();

		CommonWriter writer = new CommonWriter(accountId, accountUsername, accountType);
		
		if (accountId == null) {
			return HttpServletResponse.SC_UNAUTHORIZED;
		}
		
		Optional<BoardFree> boardFree = boardFreeRepository.findOneBySeq(seq);

		if (!boardFree.isPresent())
			throw new ServiceException(ServiceError.POST_NOT_FOUND);

		BoardFree getBoardFree = boardFree.get();
		
		if (getBoardFree.getWriter() == null) {
			return HttpServletResponse.SC_UNAUTHORIZED;
		}
		
		if (!accountId.equals(getBoardFree.getWriter().getUserId())) {
			return HttpServletResponse.SC_UNAUTHORIZED;
		}
		
		BoardItem boardItem = new BoardItem(getBoardFree.getId(), getBoardFree.getSeq());

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

			getBoardFree.setContent(null);
			getBoardFree.setSubject(null);
			getBoardFree.setWriter(null);
			
			List<BoardHistory> historys = getBoardFree.getHistory();
			
			if (historys == null) {
				historys = new ArrayList<>();
			}
			
			BoardHistory history = new BoardHistory();
			history.setId(new ObjectId().toString());
			history.setType(CommonConst.BOARD_HISTORY_TYPE_DELETE);
			history.setWriter(writer);
			historys.add(history);
			getBoardFree.setHistory(historys);
			
			BoardStatus boardStatus = getBoardFree.getStatus();
			
			if (boardStatus == null) {
				boardStatus = new BoardStatus();
			}
			
			boardStatus.setDelete(CommonConst.BOARD_HISTORY_TYPE_DELETE);
			getBoardFree.setStatus(boardStatus);
			
			boardFreeRepository.save(getBoardFree);

			if (log.isInfoEnabled()) {
				log.info("A post was deleted(post only). post seq=" + getBoardFree.getSeq() + ", subject=" + getBoardFree.getSubject());
			}

			if (log.isDebugEnabled()) {
				log.debug("Delete(post only) post. BoardFree = " + boardFree);
			}
			
			break;
			
		case CommonConst.BOARD_DELETE_TYPE_ALL:
			boardFreeRepository.delete(getBoardFree);

			if (log.isInfoEnabled()) {
				log.info("A post was deleted(all). post seq=" + getBoardFree.getSeq() + ", subject=" + getBoardFree.getSubject());
			}

			if (log.isDebugEnabled()) {
				log.debug("Delete(all) post. BoardFree = " + boardFree);
			}
			
			break;
		}
		
		searchService.deleteDocumentBoard(getBoardFree.getId());
		
		return HttpServletResponse.SC_OK;
	}
	*/

	/*
	public Integer setNotice(int seq, String type) {
		
		if (!commonService.isAdmin()) {
			return HttpServletResponse.SC_UNAUTHORIZED;
		}
		
		CommonPrincipal principal = userService.getCommonPrincipal();
		String accountId = principal.getId();
		String accountUsername = principal.getUsername();
		CommonConst.ACCOUNT_TYPE accountType = principal.getProviderId();

		CommonWriter writer = new CommonWriter(accountId, accountUsername, accountType);

		if (accountId == null) {
			return HttpServletResponse.SC_UNAUTHORIZED;
		}
		
		Optional<BoardFree> boardFree = boardFreeRepository.findOneBySeq(seq);
		if (!boardFree.isPresent())
			throw new ServiceException(ServiceError.POST_NOT_FOUND);

		BoardFree getBoardFree = boardFree.get();

		BoardStatus status = getBoardFree.getStatus();
		
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

		getBoardFree.setStatus(status);
		
		if (!noticeType.isEmpty()) {
			List<BoardHistory> historys = getBoardFree.getHistory();
			
			if (historys == null) {
				historys = new ArrayList<>();
			}
			
			BoardHistory history = new BoardHistory();
			history.setId(new ObjectId().toString());
			history.setType(noticeType);
			history.setWriter(writer);
			historys.add(history);
			getBoardFree.setHistory(historys);
		}
		
		boardFreeRepository.save(getBoardFree);
		
		if (log.isInfoEnabled()) {
			log.info("Set notice. post seq=" + getBoardFree.getSeq() + ", type=" + status.getNotice());
		}

		if (log.isDebugEnabled()) {
			log.debug("Set notice. BoardFree = " + getBoardFree);
		}
		
		return HttpServletResponse.SC_OK;
	}
	*/

	/**
	 * 자유게시판 주간 좋아요수 선두
	 * @return 게시물 목록
     */
	public List<BoardFreeOnBest> getFreeTopLikes() {
		LocalDate date = LocalDate.now().minusWeeks(1);
		Instant instant = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();

		return boardDAO.getBoardFreeCountOfLikeBest(new ObjectId(Date.from(instant)));
	}

	/**
	 * 자유게시판 주간 댓글수 선두
	 * @return 게시물 목록
	 */
	public List<BoardFreeOnBest> getFreeTopComments() {
		LocalDate date = LocalDate.now().minusWeeks(1);
		Instant instant = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();

		HashMap<String, Integer> boardFreeCommentCount = boardDAO.getBoardFreeCountOfCommentBest(new ObjectId(Date.from(instant)));

		ArrayList<ObjectId> commentIds = new ArrayList<>();

		Iterator<?> commentIterator = boardFreeCommentCount.entrySet().iterator();

		// 댓글 많은 글 id 뽑아내기
		while (commentIterator.hasNext()) {
			Entry<String, Integer> entry = (Entry<String, Integer>) commentIterator.next();
			ObjectId objId = new ObjectId(entry.getKey());
			commentIds.add(objId);
		}

		// commentIds를 파라미터로 다시 글을 가져온다.
		List<BoardFreeOnBest> posts = boardDAO.getBoardFreeListOfTop(commentIds);

		for (BoardFreeOnBest boardFree : posts) {
			String id = boardFree.getId();
			Integer count = boardFreeCommentCount.get(id);
			boardFree.setCount(count);
		}

		// sort and limit
		Comparator<BoardFreeOnBest> byCount = (b1, b2) -> b2.getCount() - b1.getCount();
		Comparator<BoardFreeOnBest> byView = (b1, b2) -> b2.getViews() - b1.getViews();

		posts = posts.stream()
				.sorted(byCount.thenComparing(byView))
				.limit(CommonConst.BOARD_TOP_LIMIT)
				.collect(Collectors.toList());

		return posts;
	}

	/**
	 * 자유게시판 댓글 목록
	 * @param page
	 * @param size
     * @return
     */
	public Page<BoardFreeComment> getBoardFreeComments(int page, int size) {

		Sort sort = new Sort(Sort.Direction.DESC, Collections.singletonList("_id"));
		Pageable pageable = new PageRequest(page - 1, size, sort);

		return boardFreeCommentRepository.findAll(pageable);
	}
}
