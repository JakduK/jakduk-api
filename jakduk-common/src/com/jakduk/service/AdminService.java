package com.jakduk.service;

import java.awt.Graphics2D;
import java.awt.Image;
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
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;

import com.jakduk.model.db.*;
import com.jakduk.model.embedded.JakduScore;
import com.jakduk.model.web.*;
import com.jakduk.repository.*;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.jakduk.common.CommonConst;
import com.jakduk.dao.JakdukDAO;
import com.jakduk.model.elasticsearch.BoardFreeOnES;
import com.jakduk.model.elasticsearch.CommentOnES;
import com.jakduk.model.elasticsearch.GalleryOnES;
import com.jakduk.model.embedded.FootballClubName;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.mapping.PutMapping;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 5. 1.
 * @desc     :
 */

@Service
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
	private BoardFreeRepository boardFreeRepository;
	
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

	private Logger logger = Logger.getLogger(this.getClass());
	
	public String initBoardCategory() {
		
		String result = "";
		
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
			
			logger.debug("input board category.");
			result = "success input board category data at DB";
		} else {
			result = "already exist board category at DB.";
		}
		
		return result;
	}
	
	public String initSearchIndex() {
		
		String result = "";
		
		// 인덱스 초기화.
		ImmutableSettings.Builder settingsBuilder = ImmutableSettings.settingsBuilder();
		//settingsBuilder.put("number_of_shards", 5);
		//settingsBuilder.put("number_of_replicas", 1);
		settingsBuilder.put("index.analysis.analyzer.korean.type", "custom");
		settingsBuilder.put("index.analysis.analyzer.korean.tokenizer", "mecab_ko_standard_tokenizer");
		
		try {
			JestResult jestResult = jestClient.execute(new CreateIndex.Builder(elasticsearchIndexName).settings(settingsBuilder.build().getAsMap()).build());
			
			if (!jestResult.isSucceeded()) {
				logger.debug(jestResult.getErrorMessage());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
public String initSearchType() {
		
		String result = "";
	
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
				logger.debug(jestResult.getErrorMessage());
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
				logger.debug(jestResult.getErrorMessage());
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
				logger.debug(jestResult.getErrorMessage());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

public String initSearchData() {
	
	String result = "";
	
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
				logger.debug(jestResult.getErrorMessage());
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
				logger.debug(jestResult.getErrorMessage());
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
				logger.debug(jestResult.getErrorMessage());
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
		
		if (encyclopedia.getId().isEmpty()) {
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
	
	public Model getFootballClubWrite(Model model) {
		
		List<FootballClubOrigin> footballClubs = footballClubOriginRepository.findAll();
		
		model.addAttribute("footballClubs", footballClubs);
		model.addAttribute("footballClubWrite", new FootballClubWrite());
		
		return model;
	}
	
	public Model getFootballClubWrite(Model model, String id) {
		
		List<FootballClubOrigin> footballClubs = footballClubOriginRepository.findAll();
		FootballClub fc = footballClubRepository.findOne(id);
		FootballClubWrite fcWrite = new FootballClubWrite();
		fcWrite.setId(fc.getId());
		fcWrite.setActive(fc.getActive());
		fcWrite.setOrigin(fc.getOrigin().getName());
		
		for (FootballClubName fcName : fc.getNames()) {
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
	
	public void footballClubWrite(FootballClubWrite footballClubWrite) {
		FootballClub footballClub = new FootballClub();
		
		FootballClubOrigin footballClubOrigin = footballClubOriginRepository.findOne(footballClubWrite.getOrigin());
		
		footballClub.setOrigin(footballClubOrigin);
		footballClub.setActive(footballClubWrite.getActive());
		
		if (footballClubWrite.getId().isEmpty()) {
			footballClub.setId(null);
		} else {
			footballClub.setId(footballClubWrite.getId());
		}
		
		ArrayList<FootballClubName> names = new ArrayList<FootballClubName>();
		FootballClubName footballClubNameKr = new FootballClubName();
		FootballClubName footballClubNameEn = new FootballClubName();
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
		
		if (logger.isDebugEnabled()) {
			logger.debug("boardCategory=" + boardCategory);
		}
		
		boardCategoryRepository.save(boardCategory);
	}
	
	public Model getEncyclopediaList(Model model) {
		List<Encyclopedia> encyclopedias = encyclopediaRepository.findAll();
		
		model.addAttribute("encyclopedias", encyclopedias);
		
		return model;
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

	public Model dataFootballClubOriginList(Model model) {
		List<FootballClubOrigin> fcOrigins = footballClubOriginRepository.findAll();

		model.addAttribute("fcOrigins", fcOrigins);

		return model;
	}

	public Model getFootballClubList(Model model) {
		List<FootballClub> fcs = footballClubRepository.findAll();
		
		model.addAttribute("fcs", fcs);
		
		return model;
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
					
					logger.debug("gallery=" + gallery);
					
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
		
		if (logger.isDebugEnabled()) {
			logger.debug("attendanceLeague=" + attendanceLeague);
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
	
	public Model getHomeDescription(Model model, String id) {
		HomeDescription homeDescription = homeDescriptionReposotiry.findOne(id);
		
		model.addAttribute("homeDescription", homeDescription);
		
		return model;
	}
	
	public void homeDescriptionWrite(HomeDescription homeDescription) {
		
		if (homeDescription.getId().isEmpty()) {
			homeDescription.setId(null);
		} 
		
		if (logger.isDebugEnabled()) {
			logger.debug("homeDescription=" + homeDescription);
		}
		
		homeDescriptionReposotiry.save(homeDescription);
	}	
	
	public Model getHomeDescriptionList(Model model) {
		List<HomeDescription> homeDescriptions = homeDescriptionReposotiry.findAll();
		
		model.addAttribute("homeDescriptions", homeDescriptions);
		
		return model;
	}	
	
	public void getAttendanceClubWrite(Model model) {
		List<FootballClubOrigin> footballClubs = footballClubOriginRepository.findAll();
		
		model.addAttribute("footballClubs", footballClubs);
		model.addAttribute("attendanceClubWrite", new AttendanceClubWrite());
	}
	
	public void getAttendanceClubWrite(Model model, String id) {
		AttendanceClub attendanceClub = attendanceClubRepository.findOne(id);
		logger.debug("attendanceClub=" + attendanceClub);
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

		model.addAttribute("footballClubs", footballClubs);
		model.addAttribute("jakduScheduleWrite", new JakduScheduleWrite());
	}

	public void getJakduScheduleWrite(Model model, String id) {
		JakduSchedule jakduSchedule = jakduScheduleRepository.findOne(id);
		logger.debug("jakduSchedule=" + jakduSchedule);
		JakduScore jakduScore = jakduSchedule.getScore();
		List<FootballClubOrigin> footballClubs = footballClubOriginRepository.findAll();

		JakduScheduleWrite jakduScheduleWrite = new JakduScheduleWrite();
		jakduScheduleWrite.setDate(jakduSchedule.getDate());
		jakduScheduleWrite.setId(jakduSchedule.getId());
		jakduScheduleWrite.setHome(jakduSchedule.getHome().getId());
		jakduScheduleWrite.setAway(jakduSchedule.getAway().getId());
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

		model.addAttribute("footballClubs", footballClubs);
		model.addAttribute("jakduScheduleWrite", jakduScheduleWrite);
	}

	public void writeJakduSchedule(JakduScheduleWrite jakduScheduleWrite) {
		JakduSchedule jakduSchedule = new JakduSchedule();

		FootballClubOrigin home = footballClubOriginRepository.findOne(jakduScheduleWrite.getHome());
		FootballClubOrigin away = footballClubOriginRepository.findOne(jakduScheduleWrite.getAway());

		if (jakduScheduleWrite.isTimeUp()) {
			JakduScore jakduScore = new JakduScore();

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

		if (jakduScheduleWrite.getId().isEmpty()) {
			jakduSchedule.setId(null);
		} else {
			jakduSchedule.setId(jakduScheduleWrite.getId());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("jakduSchedule=" + jakduSchedule);
		}

		jakduScheduleRepository.save(jakduSchedule);
	}

	public boolean deleteAttendanceLeague(String id) {

		if (!id.isEmpty()) {
			JakduSchedule jakduSchedule = jakduScheduleRepository.findOne(id);

			if (jakduSchedule != null) {
				jakduScheduleRepository.delete(jakduSchedule);
				logger.debug("delete JakduSchedule=" + jakduSchedule);
				return true;
			}
		}

		return false;
	}

	public Model getJakduScheduleList(Model model) {
		List<JakduSchedule> jakduSchedules = jakduScheduleRepository.findAll();

		model.addAttribute("jakduSchedules", jakduSchedules);

		return model;
	}

}
