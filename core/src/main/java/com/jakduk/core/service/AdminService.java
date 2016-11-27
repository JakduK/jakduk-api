package com.jakduk.core.service;

import com.jakduk.core.common.CommonConst;
import com.jakduk.core.dao.JakdukDAO;
import com.jakduk.core.model.db.*;
import com.jakduk.core.model.embedded.JakduScheduleScore;
import com.jakduk.core.model.embedded.LocalName;
import com.jakduk.core.model.embedded.LocalSimpleName;
import com.jakduk.core.model.web.AttendanceClubWrite;
import com.jakduk.core.model.web.board.BoardCategoryWrite;
import com.jakduk.core.model.web.CompetitionWrite;
import com.jakduk.core.model.web.ThumbnailSizeWrite;
import com.jakduk.core.model.web.jakdu.JakduScheduleGroupWrite;
import com.jakduk.core.model.web.jakdu.JakduScheduleWrite;
import com.jakduk.core.repository.*;
import com.jakduk.core.repository.board.category.BoardCategoryRepository;
import com.jakduk.core.repository.jakdu.JakduScheduleGroupRepository;
import com.jakduk.core.repository.jakdu.JakduScheduleRepository;
import io.searchbox.client.JestClient;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
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
import java.util.*;
import java.util.List;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
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
	private HomeDescriptionRepository homeDescriptionReposotiry;
	
	@Autowired
	private AttendanceClubRepository attendanceClubRepository;

	@Autowired
	private JakduScheduleRepository jakduScheduleRepository;

	@Autowired
	private JakduScheduleGroupRepository jakduScheduleGroupRepository;

	// 리팩토링 할때 없애자.
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

	// 알림판 삭제.
	public void deleteHomeDescriptionById(String id) {
		homeDescriptionReposotiry.delete(id);
	}

	// 백과사전 하나.
	public Encyclopedia findEncyclopediaById(String id) {
		return encyclopediaRepository.findOne(id);
	}

	// 백과사전 목록.
	public List<Encyclopedia> findEncyclopedias() {
		return encyclopediaRepository.findAll();
	}

	// 백과사전 저장.
	public void saveEncyclopedia(Encyclopedia encyclopedia) {
		encyclopediaRepository.save(encyclopedia);
	}

	// 백과사전 삭제.
	public void deleteEncyclopediaById(String id) {
		encyclopediaRepository.delete(id);
	}

	// 부모 추구단 목록.
	public List<FootballClubOrigin> findOriginFootballClubs() {
		return footballClubOriginRepository.findAll();
	}

	// 부모 축구단 하나.
	public FootballClubOrigin findOriginFootballClubById(String id) {
		return footballClubOriginRepository.findOne(id);
	}

	// 새 부모 축구단 저장.
	public void saveOriginFootballClub(FootballClubOrigin footballClubOrigin) {
		footballClubOriginRepository.save(footballClubOrigin);
	}

	// 부모 축구단 하나 지움.
	public void deleteOriginFootballClub(String id) {
		footballClubOriginRepository.delete(id);
	}

	// 축구단 목록.
	public List<FootballClub> findFootballClubs() {
		return footballClubRepository.findAll();
	}

	// 축구단 하나.
	public FootballClub findFootballClubById(String id) {
		return footballClubRepository.findOne(id);
	}

	// 새 부모 축구단 저장.
	public void saveFootballClub(FootballClub footballClub) {
		footballClubRepository.save(footballClub);
	}

	public HashMap<String, Object> initBoardCategory() {

		HashMap<String, Object> result = new HashMap<>();
		
		if (boardCategoryRepository.count() == 0) {
			BoardCategory boardCategory01 = new BoardCategory();
			ArrayList<LocalSimpleName> names01 = new ArrayList<>();
			names01.add(new LocalSimpleName(Locale.KOREAN.getLanguage(), "자유"));
			names01.add(new LocalSimpleName(Locale.ENGLISH.getLanguage(), "FREE"));
			boardCategory01.setCode("FREE");
			boardCategory01.setNames(names01);
			boardCategoryRepository.save(boardCategory01);

			BoardCategory boardCategory02 = new BoardCategory();
			ArrayList<LocalSimpleName> names02 = new ArrayList<>();
			names02.add(new LocalSimpleName(Locale.KOREAN.getLanguage(), "축구"));
			names02.add(new LocalSimpleName(Locale.ENGLISH.getLanguage(), "FOOTBALL"));
			boardCategory02.setCode("FOOTBALL");
			boardCategory02.setNames(names02);
			boardCategoryRepository.save(boardCategory02);

			log.debug("input board category.");

			result.put("result", Boolean.TRUE);
			result.put("message", "success input board category data at DB");
		} else {
			result.put("result", Boolean.FALSE);
			result.put("message", "already exist board category at DB.");
		}
		
		return result;
	}

	public BoardCategory boardCategoryWrite(BoardCategoryWrite boardCategoryWrite) {
		BoardCategory boardCategory = new BoardCategory();
		
		if (boardCategoryWrite.getId() != null) {
			boardCategory.setId(boardCategoryWrite.getId());
		}
		
		boardCategory.setCode(boardCategoryWrite.getCode());

		ArrayList<LocalSimpleName> names = new ArrayList<>();
		names.add(new LocalSimpleName(Locale.KOREAN.getLanguage(), boardCategoryWrite.getNameKr()));
		names.add(new LocalSimpleName(Locale.ENGLISH.getLanguage(), boardCategoryWrite.getNameEn()));

		boardCategory.setNames(names);

		if (log.isDebugEnabled()) {
			log.debug("boardCategory=" + boardCategory);
		}
		
		return boardCategoryRepository.save(boardCategory);
	}

	public List<BoardCategory> getBoardCategoryList() {
		return boardCategoryRepository.findAll();
	}

	public List<AttendanceClub> getAttendanceClubList() {
		
		List<AttendanceClub> attendanceClubs;
		Sort sort = new Sort(Sort.Direction.ASC, Collections.singletonList("_id"));
		attendanceClubs = attendanceClubRepository.findAll(sort);
		
		return attendanceClubs;
	}

	public BoardCategoryWrite getBoardCategory(String id) {
		BoardCategory boardCategory = boardCategoryRepository.findOne(id);
		
		BoardCategoryWrite boardCategoryWrite = new BoardCategoryWrite();
		boardCategoryWrite.setId(boardCategory.getId());
		boardCategoryWrite.setCode(boardCategory.getCode());

		for (LocalSimpleName fcName : boardCategory.getNames()) {
			if (fcName.getLanguage().equals(Locale.KOREAN.getLanguage())) {
				boardCategoryWrite.setNameKr(fcName.getName());
			} else if (fcName.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
				boardCategoryWrite.setNameEn(fcName.getName());
			}
		}

		return boardCategoryWrite;
	}
	
	public void thumbnailSizeWrite(ThumbnailSizeWrite thumbnailSizeWrite) {
		
		List<Gallery> galleries;
		
		if (thumbnailSizeWrite.getGalleryId() != null && !thumbnailSizeWrite.getGalleryId().isEmpty()) {
			galleries = new ArrayList<>();
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
			if (!splitContentType[1].equals("octet-stream")) {
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
					log.warn(e.getMessage(), e);
				}
			}				
		}
	}

	public Map<String, Object> getAttendanceClubWrite(String id) {
		Map<String, Object> data = new HashMap<>();

		AttendanceClub attendanceClub = attendanceClubRepository.findOne(id);
		log.debug("attendanceClub=" + attendanceClub);
		
		AttendanceClubWrite attendanceClubWrite = new AttendanceClubWrite();
		attendanceClubWrite.setId(attendanceClub.getId());
		attendanceClubWrite.setOrigin(attendanceClub.getClub().getId());
		attendanceClubWrite.setLeague(attendanceClub.getLeague());;
		attendanceClubWrite.setSeason(attendanceClub.getSeason());
		attendanceClubWrite.setGames(attendanceClub.getGames());
		attendanceClubWrite.setTotal(attendanceClub.getTotal());
		attendanceClubWrite.setAverage(attendanceClub.getAverage());

		data.put("attendanceClubWrite", attendanceClubWrite);
		return data;
	}	
	
	public void attendanceClubWrite(String id, AttendanceClubWrite attendanceClubWrite) {
		FootballClubOrigin footballClubOrigin = footballClubOriginRepository.findOne(attendanceClubWrite.getOrigin());

		AttendanceClub attendanceClub = Objects.isNull(id) ? new AttendanceClub() : attendanceClubRepository.findOne(id);
		attendanceClub.setClub(footballClubOrigin);
		attendanceClub.setSeason(attendanceClubWrite.getSeason());
		attendanceClub.setLeague(attendanceClubWrite.getLeague());
		attendanceClub.setGames(attendanceClubWrite.getGames());
		attendanceClub.setTotal(attendanceClubWrite.getTotal());
		attendanceClub.setAverage(attendanceClubWrite.getAverage());
		
		attendanceClubRepository.save(attendanceClub);
	}

	public JakduScheduleWrite getJakduScheduleWrite(String id) {
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
			jakduScheduleWrite.setCompetition(jakduSchedule.getCompetition().getId());
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

		return jakduScheduleWrite;
	}

	public JakduSchedule writeJakduSchedule(String id, JakduScheduleWrite jakduScheduleWrite) {
		JakduSchedule jakduSchedule = Objects.isNull(id) ? new JakduSchedule() : jakduScheduleRepository.findOne(id);

		FootballClubOrigin home = footballClubOriginRepository.findOne(jakduScheduleWrite.getHome());
		FootballClubOrigin away = footballClubOriginRepository.findOne(jakduScheduleWrite.getAway());
		Competition competition = competitionRepository.findOne(jakduScheduleWrite.getCompetition());
		JakduScheduleGroup jakduScheduleGroup = Objects.isNull(id) ? jakdukDAO.getJakduScheduleGroupOrderBySeq() : jakduScheduleGroupRepository.findBySeq(jakduScheduleWrite.getGroupSeq());

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

		if (Objects.nonNull(id)) {
			jakduSchedule.setId(id);
		}

		if (log.isDebugEnabled()) {
			log.debug("jakduSchedule=" + jakduSchedule);
		}

		return jakduScheduleRepository.save(jakduSchedule);
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

	public List<JakduSchedule> getDataJakduScheduleList() {
		Sort sort = new Sort(Sort.Direction.DESC, Arrays.asList("_id"));
		return jakduScheduleRepository.findAll(sort);
	}

	public JakduScheduleGroupWrite getJakduScheduleGroupWrite(String id) {
		JakduScheduleGroup jakduScheduleGroup = jakduScheduleGroupRepository.findOne(id);

		JakduScheduleGroupWrite jakduScheduleGroupWrite = new JakduScheduleGroupWrite();
		jakduScheduleGroupWrite.setId(jakduScheduleGroup.getId());
		jakduScheduleGroupWrite.setSeq(jakduScheduleGroup.getSeq());
		jakduScheduleGroupWrite.setState(jakduScheduleGroup.getState());
		jakduScheduleGroupWrite.setOpenDate(jakduScheduleGroup.getOpenDate());
		jakduScheduleGroupWrite.setNextSeq(false);

		return jakduScheduleGroupWrite;
	}

	public JakduScheduleGroup writeJakduScheduleGroup(String id, JakduScheduleGroupWrite jakduScheduleGroupWrite) {
		JakduScheduleGroup jakduScheduleGroup = Objects.isNull(id) ? new JakduScheduleGroup() : jakduScheduleGroupRepository.findOne(id);

		jakduScheduleGroup.setOpenDate(jakduScheduleGroupWrite.getOpenDate());
		jakduScheduleGroup.setState(jakduScheduleGroupWrite.getState());

		if (jakduScheduleGroupWrite.isNextSeq()) {
			jakduScheduleGroup.setSeq(commonService.getNextSequence(CommonConst.SEQ_JAKDU_SCHEDULE_GROUP));
		} else {
			jakduScheduleGroup.setSeq(jakduScheduleGroupWrite.getSeq());
		}

		if (Objects.nonNull(id)) {
			jakduScheduleGroup.setId(id);
		}

		if (log.isDebugEnabled()) {
			log.debug("jakduScheduleGroup=" + jakduScheduleGroup);
		}

		return jakduScheduleGroupRepository.save(jakduScheduleGroup);
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

	public List<JakduScheduleGroup> getDataJakduScheduleGroupList() {
		return jakduScheduleGroupRepository.findAll();
	}

	public CompetitionWrite getCompetition(String id) {
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

		return competitionWrite;
	}

	public Competition writeCompetition(String id, CompetitionWrite competitionWrite) {
		Competition competition = new Competition();

		if (Objects.nonNull(id)) {
			competition.setId(id);
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

		return competitionRepository.save(competition);
	}

	public List<Competition> getCompetitions() {
		return competitionRepository.findAll();
	}

	public void deleteCompetition(String id) {
		competitionRepository.delete(id);
	}

}
