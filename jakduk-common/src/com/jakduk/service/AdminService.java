package com.jakduk.service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.mapping.PutMapping;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.jakduk.common.CommonConst;
import com.jakduk.dao.JakdukDAO;
import com.jakduk.model.db.AttendanceClub;
import com.jakduk.model.db.AttendanceLeague;
import com.jakduk.model.db.BoardCategory;
import com.jakduk.model.db.Competition;
import com.jakduk.model.db.Encyclopedia;
import com.jakduk.model.db.FootballClub;
import com.jakduk.model.db.FootballClubOrigin;
import com.jakduk.model.db.Gallery;
import com.jakduk.model.db.HomeDescription;
import com.jakduk.model.db.JakduSchedule;
import com.jakduk.model.db.JakduScheduleGroup;
import com.jakduk.model.elasticsearch.BoardFreeOnES;
import com.jakduk.model.elasticsearch.CommentOnES;
import com.jakduk.model.elasticsearch.GalleryOnES;
import com.jakduk.model.embedded.JakduScheduleScore;
import com.jakduk.model.embedded.LocalName;
import com.jakduk.model.web.AttendanceClubWrite;
import com.jakduk.model.web.BoardCategoryWrite;
import com.jakduk.model.web.CompetitionWrite;
import com.jakduk.model.web.FootballClubWrite;
import com.jakduk.model.web.ThumbnailSizeWrite;
import com.jakduk.model.web.jakdu.JakduScheduleGroupWrite;
import com.jakduk.model.web.jakdu.JakduScheduleWrite;
import com.jakduk.repository.AttendanceClubRepository;
import com.jakduk.repository.AttendanceLeagueRepository;
import com.jakduk.repository.BoardCategoryRepository;
import com.jakduk.repository.CompetitionRepository;
import com.jakduk.repository.EncyclopediaRepository;
import com.jakduk.repository.FootballClubOriginRepository;
import com.jakduk.repository.FootballClubRepository;
import com.jakduk.repository.GalleryRepository;
import com.jakduk.repository.HomeDescriptionRepository;
import com.jakduk.repository.jakdu.JakduScheduleGroupRepository;
import com.jakduk.repository.jakdu.JakduScheduleRepository;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 5. 1.
 * @desc     :
 */

@Service
@Slf4j
public class AdminService {
	
	@Value("${storage.image.path}")
	private String storageImagePath;
	
	@Value("${storage.thumbnail.path}")
	private String storageThumbnailPath;
	
	@Value("${elasticsearch.index.name}")
	private String elasticsearchIndexName;
	
	@Autowired
	private JestClient jestClient;
	
	@Autowired
	private JakdukDAO jakdukDAO;
	
	@Autowired
	private CommonService commonService;

	@Autowired
	private BoardCategoryRepository boardCategoryRepository;
	
	@Autowired
	private EncyclopediaRepository encyclopediaRepository;
	
	@Autowired
	private FootballClubRepository footballClubRepository;
	
	@Autowired
	private FootballClubOriginRepository footballClubOriginRepository;
	
	@Autowired
	private GalleryRepository galleryRepository;
	
	@Autowired
	private AttendanceLeagueRepository attendanceLeagueReposidory;
	
	@Autowired
	private HomeDescriptionRepository homeDescriptionReposotiry;
	
	@Autowired
	private AttendanceClubRepository attendanceClubRepository;

	@Autowired
	private JakduScheduleRepository jakduScheduleRepository;

	@Autowired
	private JakduScheduleGroupRepository jakduScheduleGroupRepository;

	@Autowired
	private CompetitionRepository competitionRepository;

	// 알림판 목록.
	public List<HomeDescription> findHomeDescriptions() {
		return homeDescriptionReposotiry.findAll();
	}

	// 알림판 하나.
	public HomeDescription findHomeDescriptionById(String id) {
		return homeDescriptionReposotiry.findOne(id);
	}

	// 알림판 저장.
	public void saveHomeDescription(HomeDescription homeDescription) {
		homeDescriptionReposotiry.save(homeDescription);
	}

	// 백과사전 목록.
	public List<Encyclopedia> findEncyclopedias() {
		return encyclopediaRepository.findAll();
	}

	// 백과사전 저장.
	public void saveEncyclopedia(Encyclopedia encyclopedia) {
		encyclopediaRepository.save(encyclopedia);
	}

	public Encyclopedia findEncyclopedia(String id) {
		return encyclopediaRepository.findOne(id);
	}

	// 알림판 삭제.
	public void deleteHomeDescriptionById(String id) {
		homeDescriptionReposotiry.delete(id);
	}

	public HashMap<String, Object> initBoardCategory() {

		HashMap<String, Object> result = new HashMap<>();
		
		if (boardCategoryRepository.count() == 0) {
			BoardCategory boardCategory01 = new BoardCategory();
			List<String> usingBoard = new ArrayList<String>();
			usingBoard.add(CommonConst.BOARD_NAME_FREE);
			boardCategory01.setName(CommonConst.BOARD_CATEGORY_FREE);
			boardCategory01.setResName("board.category.free");
			boardCategory01.setUsingBoard(usingBoard);
			boardCategoryRepository.save(boardCategory01);
			
			usingBoard.clear();
			
			BoardCategory boardCategory02 = new BoardCategory();
			boardCategory02.setResName("board.category.football");
			boardCategory02.setName(CommonConst.BOARD_CATEGORY_FOOTBALL);
			usingBoard.add(CommonConst.BOARD_NAME_FREE);
			boardCategory02.setUsingBoard(usingBoard);
			boardCategoryRepository.save(boardCategory02);
			
			usingBoard.clear();
			
			BoardCategory boardCategory03 = new BoardCategory();
			boardCategory03.setResName("board.category.develop");
			boardCategory03.setName(CommonConst.BOARD_CATEGORY_DEVELOP);
			usingBoard.add(CommonConst.BOARD_NAME_FREE);
			boardCategory03.setUsingBoard(usingBoard);
			boardCategoryRepository.save(boardCategory03);

			log.debug("input board category.");

			result.put("result", Boolean.TRUE);
			result.put("message", "success input board category data at DB");
		} else {
			result.put("result", Boolean.FALSE);
			result.put("message", "already exist board category at DB.");
		}
		
		return result;
	}
	
	public HashMap<String, Object> initSearchIndex() {

		HashMap<String, Object> result = new HashMap<>();
		
		// 인덱스 초기화.
		ImmutableSettings.Builder settingsBuilder = ImmutableSettings.settingsBuilder();
		//settingsBuilder.put("number_of_shards", 5);
		//settingsBuilder.put("number_of_replicas", 1);
		settingsBuilder.put("index.analysis.analyzer.korean.type", "custom");
		settingsBuilder.put("index.analysis.analyzer.korean.tokenizer", "mecab_ko_standard_tokenizer");
		
		try {
			JestResult jestResult = jestClient.execute(new CreateIndex.Builder(elasticsearchIndexName).settings(settingsBuilder.build().getAsMap()).build());
			
			if (!jestResult.isSucceeded()) {
				log.debug(jestResult.getErrorMessage());
			} else {
				result.put("result", Boolean.TRUE);
			}
		} catch (IOException e) {
			result.put("result", Boolean.FALSE);
			log.error(e.getMessage(), e);
		}
		
		return result;
	}
	
public HashMap<String, Object> initSearchType() {

		HashMap<String, Object> result = new HashMap<>();
	
		// 매핑 초기화.
        PutMapping putMapping1 = new PutMapping.Builder(
        		elasticsearchIndexName,
        		CommonConst.ELASTICSEARCH_TYPE_BOARD,
                "{ \"properties\" : { \"subject\" : {\"type\" : \"string\", \"analyzer\" : \"korean\"}"
                + ", \"content\" : {\"type\" : \"string\", \"analyzer\" : \"korean\"} }"
                + "}"
        ).build();

		try {
			JestResult jestResult = jestClient.execute(putMapping1);
			
			if (!jestResult.isSucceeded()) {
				log.debug(jestResult.getErrorMessage());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 매핑 초기화.
		PutMapping putMapping2 = new PutMapping.Builder(
        		elasticsearchIndexName,
        		CommonConst.ELASTICSEARCH_TYPE_COMMENT,
              "{ \"properties\" : { " +
              "\"content\" : {\"type\" : \"string\", \"analyzer\" : \"korean\"} " +
              	"}" +
                "}"
        ).build();

		try {
			JestResult jestResult = jestClient.execute(putMapping2);
			
			if (!jestResult.isSucceeded()) {
				log.debug(jestResult.getErrorMessage());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 매핑 초기화.
		PutMapping putMapping3 = new PutMapping.Builder(
        		elasticsearchIndexName,
        		CommonConst.ELASTICSEARCH_TYPE_GALLERY,
              "{ \"properties\" : { " +
              "\"name\" : {\"type\" : \"string\", \"analyzer\" : \"korean\"} " +
              	"}" +
                "}"
        ).build();

		try {
			JestResult jestResult = jestClient.execute(putMapping3);
			
			if (!jestResult.isSucceeded()) {
				log.debug(jestResult.getErrorMessage());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

public HashMap<String, Object> initSearchData() {

	HashMap<String, Object> result = new HashMap<>();
	
	// 게시물을 엘라스틱 서치에 모두 넣기.
	List<BoardFreeOnES> posts = jakdukDAO.getBoardFreeOnES(null);
	BoardFreeOnES lastPost = posts.get(posts.size() - 1);
	
	while (posts.size() > 0) {
		List<Index> idxList = new ArrayList<>();
		
		for (BoardFreeOnES post : posts) {
			idxList.add(new Index.Builder(post).build());
		}
		
		Bulk bulk = new Bulk.Builder()
				.defaultIndex(elasticsearchIndexName)
				.defaultType(CommonConst.ELASTICSEARCH_TYPE_BOARD)
				.addAction(idxList)
				.build();
		
		try {
			JestResult jestResult = jestClient.execute(bulk);
			
			if (!jestResult.isSucceeded()) {
				log.debug(jestResult.getErrorMessage());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		posts = jakdukDAO.getBoardFreeOnES(new ObjectId(lastPost.getId()));
		if (posts.size() > 0) {
			lastPost = posts.get(posts.size() - 1);
		}
	}
	
	// 게시물을 엘라스틱 서치에 모두 넣기.
	List<CommentOnES> comments = jakdukDAO.getCommentOnES(null);
	CommentOnES lastComment = comments.get(comments.size() - 1);
	
	while (comments.size() > 0) {
		List<Index> idxList = new ArrayList<>();
		
		for (CommentOnES comment : comments) {
			idxList.add(new Index.Builder(comment).build());
		}
		
		Bulk bulk = new Bulk.Builder()
				.defaultIndex(elasticsearchIndexName)
				.defaultType(CommonConst.ELASTICSEARCH_TYPE_COMMENT)
				.addAction(idxList)
				.build();
		
		try {
			JestResult jestResult = jestClient.execute(bulk);
			
			if (!jestResult.isSucceeded()) {
				log.debug(jestResult.getErrorMessage());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		comments = jakdukDAO.getCommentOnES(new ObjectId(lastComment.getId()));
		if (comments.size() > 0) {
			lastComment = comments.get(comments.size() - 1);
		}
	}
	
	// 사진첩을 엘라스틱 서치에 모두 넣기.
	List<GalleryOnES> galleries = jakdukDAO.getGalleryOnES(null);
	GalleryOnES lastGallery = galleries.get(galleries.size() - 1);
	
	while (galleries.size() > 0) {
		List<Index> idxList = new ArrayList<>();
		
		for (GalleryOnES gallery : galleries) {
			idxList.add(new Index.Builder(gallery).build());
		}
		
		Bulk bulk = new Bulk.Builder()
				.defaultIndex(elasticsearchIndexName)
				.defaultType(CommonConst.ELASTICSEARCH_TYPE_GALLERY)
				.addAction(idxList)
				.build();
		
		try {
			JestResult jestResult = jestClient.execute(bulk);
			
			if (!jestResult.isSucceeded()) {
				log.debug(jestResult.getErrorMessage());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		galleries = jakdukDAO.getGalleryOnES(new ObjectId(lastGallery.getId()));
		if (galleries.size() > 0) {
			lastGallery = galleries.get(galleries.size() - 1);
		}
	}
	
	return result;
}
	
	public void encyclopediaWrite(Encyclopedia encyclopedia) {
		
		if (encyclopedia.getId() == null || encyclopedia.getId().isEmpty()) {
			encyclopedia.setId(null);
			if (encyclopedia.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
				encyclopedia.setSeq(commonService.getNextSequence(CommonConst.ENCYCLOPEDIA_EN));			
			} else if (encyclopedia.getLanguage().equals(Locale.KOREAN.getLanguage())) {
				encyclopedia.setSeq(commonService.getNextSequence(CommonConst.ENCYCLOPEDIA_KO));
			}
		} else {
			encyclopedia.setId(encyclopedia.getId());
		}
		
		encyclopediaRepository.save(encyclopedia);
	}
	
	public Model getFootballClub(Model model) {
		
		List<FootballClubOrigin> footballClubs = footballClubOriginRepository.findAll();
		
		model.addAttribute("footballClubs", footballClubs);
		model.addAttribute("footballClubWrite", new FootballClubWrite());
		
		return model;
	}
	
	public Model getFootballClub(Model model, String id) {
		
		List<FootballClubOrigin> footballClubs = footballClubOriginRepository.findAll();
		FootballClub fc = footballClubRepository.findOne(id);
		FootballClubWrite fcWrite = new FootballClubWrite();
		fcWrite.setId(fc.getId());
		fcWrite.setActive(fc.getActive());
		fcWrite.setOrigin(fc.getOrigin().getId());
		
		for (LocalName fcName : fc.getNames()) {
			if (fcName.getLanguage().equals(Locale.KOREAN.getLanguage())) {
				fcWrite.setFullNameKr(fcName.getFullName());
				fcWrite.setShortNameKr(fcName.getShortName());
			} else if (fcName.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
				fcWrite.setFullNameEn(fcName.getFullName());
				fcWrite.setShortNameEn(fcName.getShortName());
			}
		}
		
		model.addAttribute("footballClubs", footballClubs);
		model.addAttribute("footballClubWrite", fcWrite);
		
		return model;
	}
	
	public void writeFootballClub(FootballClubWrite footballClubWrite) {
		FootballClub footballClub = new FootballClub();
		
		FootballClubOrigin footballClubOrigin = footballClubOriginRepository.findOne(footballClubWrite.getOrigin());
		
		footballClub.setOrigin(footballClubOrigin);
		footballClub.setActive(footballClubWrite.getActive());
		
		if (footballClubWrite.getId().isEmpty()) {
			footballClub.setId(null);
		} else {
			footballClub.setId(footballClubWrite.getId());
		}
		
		ArrayList<LocalName> names = new ArrayList<LocalName>();
		LocalName footballClubNameKr = new LocalName();
		LocalName footballClubNameEn = new LocalName();
		footballClubNameKr.setLanguage(Locale.KOREAN.getLanguage());
		footballClubNameKr.setShortName(footballClubWrite.getShortNameKr());
		footballClubNameKr.setFullName(footballClubWrite.getFullNameKr());
		footballClubNameEn.setLanguage(Locale.ENGLISH.getLanguage());
		footballClubNameEn.setShortName(footballClubWrite.getShortNameEn());
		footballClubNameEn.setFullName(footballClubWrite.getFullNameEn());
		names.add(footballClubNameKr);
		names.add(footballClubNameEn);
		footballClub.setNames(names);
		
		footballClubRepository.save(footballClub);
	}

	public void boardCategoryWrite(BoardCategoryWrite boardCategoryWrite) {
		BoardCategory boardCategory = new BoardCategory();
		
		if (boardCategoryWrite.getId() != null) {
			boardCategory.setId(boardCategoryWrite.getId());
		}
		
		boardCategory.setName(boardCategoryWrite.getName());
		boardCategory.setResName(boardCategoryWrite.getResName());
		
		String[] originUsingBoard = boardCategoryWrite.getUsingBoard();

		if (originUsingBoard != null) {
			ArrayList<String> usingBoard = new ArrayList<String>(Arrays.asList(originUsingBoard));
			boardCategory.setUsingBoard(usingBoard);			
		}
		
		if (log.isDebugEnabled()) {
			log.debug("boardCategory=" + boardCategory);
		}
		
		boardCategoryRepository.save(boardCategory);
	}

	public Model getEncyclopedia(Model model, int seq, String language) {
		Encyclopedia encyclopedia = encyclopediaRepository.findOneBySeqAndLanguage(seq, language);
		
		model.addAttribute("encyclopedia", encyclopedia);
		
		return model;
	}

	public Model getFootballClubOrigin(Model model) {
		model.addAttribute("footballClubOrigin", new FootballClubOrigin());
		return model;
	}

	public Model getFootballClubOrigin(Model model, String id) {
		FootballClubOrigin fcOrigin = footballClubOriginRepository.findOne(id);

		model.addAttribute("footballClubOrigin", fcOrigin);

		return model;
	}

	public void writeFootballClubOrigin(FootballClubOrigin footballClubOrigin) {

		if (footballClubOrigin.getId().isEmpty()) {
			footballClubOrigin.setId(null);
		}

		footballClubOriginRepository.save(footballClubOrigin);
	}

	public void dataFootballClubOriginList(Model model) {
		List<FootballClubOrigin> fcOrigins = footballClubOriginRepository.findAll();

		model.addAttribute("fcOrigins", fcOrigins);
	}

	public void getFootballClubList(Model model) {
		List<FootballClub> fcs = footballClubRepository.findAll();
		
		model.addAttribute("fcs", fcs);
	}
	
	public Model getBoardCategoryList(Model model) {
		List<BoardCategory> boardCategorys = boardCategoryRepository.findAll();
		
		model.addAttribute("boardCategorys", boardCategorys);
		
		return model;
	}
	
	public Model getAttendanceLeagueList(Model model, String league) {
		
		List<AttendanceLeague> attendanceLeagues;
		Sort sort = new Sort(Sort.Direction.ASC, Arrays.asList("_id"));
		
		if (league == null) {
			attendanceLeagues = attendanceLeagueReposidory.findAll(sort);
		} else {
			attendanceLeagues = attendanceLeagueReposidory.findByLeague(league, sort);
		}
		
		model.addAttribute("attendanceLeagues", attendanceLeagues);
		
		return model;
	}
	
	public Model getAttendanceClubList(Model model) {
		
		List<AttendanceClub> attendanceClubs;
		Sort sort = new Sort(Sort.Direction.ASC, Arrays.asList("_id"));
		
		attendanceClubs = attendanceClubRepository.findAll(sort);
		
		model.addAttribute("attendanceClubs", attendanceClubs);
		
		return model;
	}	
	
	public Model getBoardCategory(Model model, String id) {
		BoardCategory boardCategory = boardCategoryRepository.findOne(id);
		
		BoardCategoryWrite boardCategoryWrite = new BoardCategoryWrite();
		boardCategoryWrite.setId(boardCategory.getId());
		boardCategoryWrite.setName(boardCategory.getName());
		boardCategoryWrite.setResName(boardCategory.getResName());
		String[] usingBoard = boardCategory.getUsingBoard().toArray(new String[boardCategory.getUsingBoard().size()]);
		boardCategoryWrite.setUsingBoard(usingBoard);
		
		model.addAttribute("boardCategoryWrite", boardCategoryWrite);
		
		return model;
	}
	
	public void thumbnailSizeWrite(ThumbnailSizeWrite thumbnailSizeWrite) {
		
		List<Gallery> galleries;
		
		if (thumbnailSizeWrite.getGalleryId() != null && !thumbnailSizeWrite.getGalleryId().isEmpty()) {
			galleries = new ArrayList<Gallery>();
			galleries.add(galleryRepository.findOne(thumbnailSizeWrite.getGalleryId()));
		} else {
			galleries = galleryRepository.findAll();
		}
		
		for (Gallery gallery : galleries) {
			ObjectId objId = new ObjectId(gallery.getId());
			Instant instant = Instant.ofEpochMilli(objId.getDate().getTime());
			LocalDateTime timePoint = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
			
			Path imageDirPath = Paths.get(storageImagePath, String.valueOf(timePoint.getYear()), 
					String.valueOf(timePoint.getMonthValue()), String.valueOf(timePoint.getDayOfMonth()));
			
			Path thumbDirPath = Paths.get(storageThumbnailPath, String.valueOf(timePoint.getYear()), 
					String.valueOf(timePoint.getMonthValue()), String.valueOf(timePoint.getDayOfMonth()));
			
			// 사진 경로.
			Path imageFilePath = imageDirPath.resolve(gallery.getId());
			Path thumbFilePath = thumbDirPath.resolve(gallery.getId());
			
			// 사진 포맷.
			String formatName = "jpg";
			
			String splitContentType[] = gallery.getContentType().split("/");
			if (splitContentType != null && !splitContentType[1].equals("octet-stream")) {
				formatName = splitContentType[1];
			}
			
			if (Files.exists(imageFilePath, LinkOption.NOFOLLOW_LINKS)) {
				
				try {
					
					if (Files.exists(thumbFilePath, LinkOption.NOFOLLOW_LINKS)) {
						Files.delete(thumbFilePath);
					}

					log.debug("gallery=" + gallery);
					
					BufferedInputStream in = new BufferedInputStream(new FileInputStream(imageFilePath.toString()));
					
					BufferedImage bi = ImageIO.read(in);
					BufferedImage bufferIm = new BufferedImage(thumbnailSizeWrite.getWidth(), thumbnailSizeWrite.getHeight(), BufferedImage.TYPE_INT_RGB);
					Image tempImg = bi.getScaledInstance(thumbnailSizeWrite.getWidth(), thumbnailSizeWrite.getHeight(), Image.SCALE_AREA_AVERAGING);
					Graphics2D g2 = bufferIm.createGraphics();
					g2.drawImage(tempImg, 0, 0, thumbnailSizeWrite.getWidth(), thumbnailSizeWrite.getHeight(), null);
					
					ImageIO.write(bufferIm, formatName, thumbFilePath.toFile());				
					
					in.close();					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}				
		}
	}
	
	public Model getAttendanceLeague(Model model, String id) {
		AttendanceLeague attendanceLeague = attendanceLeagueReposidory.findOne(id);
		
		model.addAttribute("attendanceLeague", attendanceLeague);
		
		return model;
	}
	
	public void attendanceLeagueWrite(AttendanceLeague attendanceLeague) {
		
		if (attendanceLeague.getId().isEmpty()) {
			attendanceLeague.setId(null);
		} 
		
		if (log.isDebugEnabled()) {
			log.debug("attendanceLeague=" + attendanceLeague);
		}
		
		attendanceLeagueReposidory.save(attendanceLeague);
	}
	
	public boolean attendanceLeagueDelete(String id) {
		
		if (!id.isEmpty()) {
			AttendanceLeague attendanceLeague = attendanceLeagueReposidory.findOne(id);
			
			if (attendanceLeague != null) {
				attendanceLeagueReposidory.delete(attendanceLeague);
				return true;
			}
		}
		
		return false;
	}

	public void homeDescriptionWrite(HomeDescription homeDescription) {
		
		if (homeDescription.getId().isEmpty()) {
			homeDescription.setId(null);
		} 
		
		if (log.isDebugEnabled()) {
			log.debug("homeDescription=" + homeDescription);
		}
		
		homeDescriptionReposotiry.save(homeDescription);
	}	

	public void getAttendanceClubWrite(Model model) {
		List<FootballClubOrigin> footballClubs = footballClubOriginRepository.findAll();
		
		model.addAttribute("footballClubs", footballClubs);
		model.addAttribute("attendanceClubWrite", new AttendanceClubWrite());
	}
	
	public void getAttendanceClubWrite(Model model, String id) {
		AttendanceClub attendanceClub = attendanceClubRepository.findOne(id);
		log.debug("attendanceClub=" + attendanceClub);
		List<FootballClubOrigin> footballClubs = footballClubOriginRepository.findAll();
		
		AttendanceClubWrite attendanceClubWrite = new AttendanceClubWrite();
		attendanceClubWrite.setId(attendanceClub.getId());
		attendanceClubWrite.setOrigin(attendanceClub.getClub().getId());
		attendanceClubWrite.setSeason(attendanceClub.getSeason());
		attendanceClubWrite.setGames(attendanceClub.getGames());
		attendanceClubWrite.setTotal(attendanceClub.getTotal());
		attendanceClubWrite.setAverage(attendanceClub.getAverage());
		
		model.addAttribute("footballClubs", footballClubs);
		model.addAttribute("attendanceClubWrite", attendanceClubWrite);
	}	
	
	public void attendanceClubWrite(AttendanceClubWrite attendanceClubWrite) {
		AttendanceClub attendanceClub = new AttendanceClub();
		
		FootballClubOrigin footballClubOrigin = footballClubOriginRepository.findOne(attendanceClubWrite.getOrigin());
		
		attendanceClub.setClub(footballClubOrigin);
		
		if (attendanceClubWrite.getId().isEmpty()) {
			attendanceClub.setId(null);
		} else {
			attendanceClub.setId(attendanceClubWrite.getId());
		}
		
		attendanceClub.setSeason(attendanceClubWrite.getSeason());
		attendanceClub.setLeague(attendanceClubWrite.getLeague());
		attendanceClub.setGames(attendanceClubWrite.getGames());
		attendanceClub.setTotal(attendanceClubWrite.getTotal());
		attendanceClub.setAverage(attendanceClubWrite.getAverage());
		
		attendanceClubRepository.save(attendanceClub);
	}

	public void getJakduScheduleWrite(Model model) {
		List<FootballClubOrigin> footballClubs = footballClubOriginRepository.findAll();
		List<Competition> competitions = competitionRepository.findAll();
		JakduScheduleGroup jakduScheduleGroup = jakdukDAO.getJakduScheduleGroupOrderBySeq();

		JakduScheduleWrite jakduScheduleWrite = new JakduScheduleWrite();
		jakduScheduleWrite.setGroupSeq(jakduScheduleGroup.getSeq());

		model.addAttribute("competitions", competitions);
		model.addAttribute("footballClubs", footballClubs);
		model.addAttribute("jakduScheduleWrite", jakduScheduleWrite);
	}

	public void getJakduScheduleWrite(Model model, String id) {
		JakduSchedule jakduSchedule = jakduScheduleRepository.findOne(id);
		JakduScheduleScore jakduScore = jakduSchedule.getScore();

		JakduScheduleWrite jakduScheduleWrite = new JakduScheduleWrite();
		jakduScheduleWrite.setDate(jakduSchedule.getDate());
		jakduScheduleWrite.setId(jakduSchedule.getId());
		jakduScheduleWrite.setHome(jakduSchedule.getHome().getId());
		jakduScheduleWrite.setAway(jakduSchedule.getAway().getId());
		if (jakduSchedule.getGroup() != null)
			jakduScheduleWrite.setGroupSeq(jakduSchedule.getGroup().getSeq());
		if (jakduSchedule.getCompetition() != null)
			jakduScheduleWrite.setCompetition(jakduSchedule.getCompetition().getCode());
		jakduScheduleWrite.setTimeUp(jakduSchedule.isTimeUp());

		if (jakduScore != null) {
			if (jakduScore.getHomeFullTime() != null)
				jakduScheduleWrite.setHomeFullTime(jakduScore.getHomeFullTime());
			if (jakduScore.getAwayFullTime() != null)
				jakduScheduleWrite.setAwayFullTime(jakduScore.getAwayFullTime());
			if (jakduScore.getHomeOverTime() != null)
				jakduScheduleWrite.setHomeOverTime(jakduScore.getHomeOverTime());
			if (jakduScore.getAwayOverTime() != null)
				jakduScheduleWrite.setAwayOverTime(jakduScore.getAwayOverTime());
			if (jakduScore.getHomePenaltyShootout() != null)
				jakduScheduleWrite.setHomePenaltyShootout(jakduScore.getHomePenaltyShootout());
			if (jakduScore.getAwayPenaltyShootout() != null)
				jakduScheduleWrite.setAwayPenaltyShootout(jakduScore.getAwayPenaltyShootout());

		}

		List<FootballClubOrigin> footballClubs = footballClubOriginRepository.findAll();
		List<Competition> competitions = competitionRepository.findAll();

		model.addAttribute("competitions", competitions);
		model.addAttribute("footballClubs", footballClubs);
		model.addAttribute("jakduScheduleWrite", jakduScheduleWrite);
	}

	public void writeJakduSchedule(JakduScheduleWrite jakduScheduleWrite) {
		JakduSchedule jakduSchedule = new JakduSchedule();

		FootballClubOrigin home = footballClubOriginRepository.findOne(jakduScheduleWrite.getHome());
		FootballClubOrigin away = footballClubOriginRepository.findOne(jakduScheduleWrite.getAway());
		Competition competition = competitionRepository.findOne(jakduScheduleWrite.getCompetition());
		JakduScheduleGroup jakduScheduleGroup = jakduScheduleGroupRepository.findBySeq(jakduScheduleWrite.getGroupSeq());

		if (jakduScheduleWrite.isTimeUp()) {
			JakduScheduleScore jakduScore = new JakduScheduleScore();

			if (jakduScheduleWrite.getHomeFullTime() != null && jakduScheduleWrite.getAwayFullTime() != null) {
				jakduScore.setHomeFullTime(jakduScheduleWrite.getHomeFullTime());
				jakduScore.setAwayFullTime(jakduScheduleWrite.getAwayFullTime());
			}

			if (jakduScheduleWrite.getHomeOverTime() != null && jakduScheduleWrite.getAwayOverTime() != null) {
				jakduScore.setHomeOverTime(jakduScheduleWrite.getHomeOverTime());
				jakduScore.setAwayOverTime(jakduScheduleWrite.getAwayOverTime());
			}

			if (jakduScheduleWrite.getHomePenaltyShootout() != null && jakduScheduleWrite.getAwayPenaltyShootout() != null) {
				jakduScore.setHomePenaltyShootout(jakduScheduleWrite.getHomePenaltyShootout());
				jakduScore.setAwayPenaltyShootout(jakduScheduleWrite.getAwayPenaltyShootout());
			}

			jakduSchedule.setScore(jakduScore);
		}

		jakduSchedule.setTimeUp(jakduScheduleWrite.isTimeUp());
		jakduSchedule.setDate(jakduScheduleWrite.getDate());
		jakduSchedule.setHome(home);
		jakduSchedule.setAway(away);
		jakduSchedule.setCompetition(competition);
		jakduSchedule.setGroup(jakduScheduleGroup);

		if (jakduScheduleWrite.getId().isEmpty()) {
			jakduSchedule.setId(null);
		} else {
			jakduSchedule.setId(jakduScheduleWrite.getId());
		}

		if (log.isDebugEnabled()) {
			log.debug("jakduSchedule=" + jakduSchedule);
		}

		jakduScheduleRepository.save(jakduSchedule);
	}

	public boolean deleteJakduSchedule(String id) {

		if (!id.isEmpty()) {
			JakduSchedule jakduSchedule = jakduScheduleRepository.findOne(id);

			if (jakduSchedule != null) {
				jakduScheduleRepository.delete(jakduSchedule);
				log.debug("delete JakduSchedule=" + jakduSchedule);
				return true;
			}
		}

		return false;
	}

	public Model getDataJakduScheduleList(Model model) {
		Sort sort = new Sort(Sort.Direction.DESC, Arrays.asList("_id"));
		List<JakduSchedule> jakduSchedules = jakduScheduleRepository.findAll(sort);

		model.addAttribute("jakduSchedules", jakduSchedules);

		return model;
	}

	public void getJakduScheduleGroupWrite(Model model) {
		model.addAttribute("jakduScheduleGroupWrite", new JakduScheduleGroupWrite());
	}

	public void getJakduScheduleGroupWrite(Model model, String id) {
		JakduScheduleGroup jakduScheduleGroup = jakduScheduleGroupRepository.findOne(id);

		JakduScheduleGroupWrite jakduScheduleGroupWrite = new JakduScheduleGroupWrite();
		jakduScheduleGroupWrite.setId(jakduScheduleGroup.getId());
		jakduScheduleGroupWrite.setSeq(jakduScheduleGroup.getSeq());
		jakduScheduleGroupWrite.setState(jakduScheduleGroup.getState());
		jakduScheduleGroupWrite.setOpenDate(jakduScheduleGroup.getOpenDate());
		jakduScheduleGroupWrite.setNextSeq(false);

		model.addAttribute("jakduScheduleGroupWrite", jakduScheduleGroupWrite);
	}

	public void writeJakduScheduleGroup(JakduScheduleGroupWrite jakduScheduleGroupWrite) {
		JakduScheduleGroup jakduScheduleGroup = new JakduScheduleGroup();

		jakduScheduleGroup.setOpenDate(jakduScheduleGroupWrite.getOpenDate());
		jakduScheduleGroup.setState(jakduScheduleGroupWrite.getState());

		if (jakduScheduleGroupWrite.isNextSeq()) {
			jakduScheduleGroup.setSeq(commonService.getNextSequence(CommonConst.SEQ_JAKDU_SCHEDULE_GROUP));
		} else {
			jakduScheduleGroup.setSeq(jakduScheduleGroupWrite.getSeq());
		}

		if (jakduScheduleGroupWrite.getId().isEmpty()) {
			jakduScheduleGroup.setId(null);
		} else {
			jakduScheduleGroup.setId(jakduScheduleGroupWrite.getId());
		}

		if (log.isDebugEnabled()) {
			log.debug("jakduScheduleGroup=" + jakduScheduleGroup);
		}

		jakduScheduleGroupRepository.save(jakduScheduleGroup);
	}

	public boolean deleteJakduScheduleGroup(String id) {

		if (!id.isEmpty()) {
			JakduScheduleGroup jakduScheduleGroup = jakduScheduleGroupRepository.findOne(id);

			if (jakduScheduleGroup != null) {
				jakduScheduleGroupRepository.delete(jakduScheduleGroup);
				log.debug("delete jakduScheduleGroup=" + jakduScheduleGroup);
				return true;
			}
		}

		return false;
	}

	public Model getDataJakduScheduleGroupList(Model model) {
		List<JakduScheduleGroup> jakduScheduleGroups = jakduScheduleGroupRepository.findAll();

		model.addAttribute("jakduScheduleGroups", jakduScheduleGroups);

		return model;
	}

	public void getCompetition(Model model) {
		model.addAttribute("competitionWrite", new CompetitionWrite());
	}

	public void getCompetition(Model model, String id) {
		Competition competition = competitionRepository.findOne(id);
		CompetitionWrite competitionWrite = new CompetitionWrite();
		competitionWrite.setId(competition.getId());
		competitionWrite.setCode(competition.getCode());

		for (LocalName name : competition.getNames()) {
			if (name.getLanguage().equals(Locale.KOREAN.getLanguage())) {
				competitionWrite.setFullNameKr(name.getFullName());
				competitionWrite.setShortNameKr(name.getShortName());
			} else if (name.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
				competitionWrite.setFullNameEn(name.getFullName());
				competitionWrite.setShortNameEn(name.getShortName());
			}
		}

		model.addAttribute("competitionWrite", competitionWrite);
	}

	public void writeCompetition(CompetitionWrite competitionWrite) {
		Competition competition = new Competition();

		if (competitionWrite.getId().isEmpty()) {
			competition.setId(null);
		} else {
			competition.setId(competitionWrite.getId());
		}

		competition.setCode(competitionWrite.getCode());

		ArrayList<LocalName> names = new ArrayList<LocalName>();
		LocalName nameKr = new LocalName();
		LocalName nameEn = new LocalName();
		nameKr.setLanguage(Locale.KOREAN.getLanguage());
		nameKr.setShortName(competitionWrite.getShortNameKr());
		nameKr.setFullName(competitionWrite.getFullNameKr());
		nameEn.setLanguage(Locale.ENGLISH.getLanguage());
		nameEn.setShortName(competitionWrite.getShortNameEn());
		nameEn.setFullName(competitionWrite.getFullNameEn());
		names.add(nameKr);
		names.add(nameEn);
		competition.setNames(names);

		competitionRepository.save(competition);
	}

	public void getDataCompetitionList(Model model) {
		List<Competition> competitions = competitionRepository.findAll();

		model.addAttribute("competitions", competitions);
	}

}
