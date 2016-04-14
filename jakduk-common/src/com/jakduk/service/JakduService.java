package com.jakduk.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakduk.authentication.common.CommonPrincipal;
import com.jakduk.common.CommonConst;
import com.jakduk.dao.JakdukDAO;
import com.jakduk.exception.RepositoryExistException;
import com.jakduk.exception.UnauthorizedAccessException;
import com.jakduk.exception.UserFeelingException;
import com.jakduk.model.db.*;
import com.jakduk.model.elasticsearch.JakduCommentOnES;
import com.jakduk.model.embedded.*;
import com.jakduk.model.web.UserFeelingResponse;
import com.jakduk.model.web.jakdu.JakduCommentWriteRequest;
import com.jakduk.model.web.jakdu.JakduCommentsResponse;
import com.jakduk.model.web.jakdu.JakduScheduleResponse;
import com.jakduk.model.web.jakdu.MyJakduRequest;
import com.jakduk.repository.jakdu.JakduCommentRepository;
import com.jakduk.repository.jakdu.JakduRepository;
import com.jakduk.repository.jakdu.JakduScheduleRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;

/**
 * Created by pyohwan on 15. 12. 26.
 */

@Service
@Slf4j
public class JakduService {

    @Autowired
    private JakdukDAO jakdukDAO;

    @Autowired
    private CommonService commonService;

    @Autowired
    private UserService userService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private JakduRepository jakduRepository;

    @Autowired
    private JakduScheduleRepository jakduScheduleRepository;

    @Autowired
    private JakduCommentRepository jakduCommentRepository;

    public void getSchedule(Model model, Locale locale) {

        model.addAttribute("dateTimeFormat", commonService.getDateTimeFormat(locale));
    }

    public JakduScheduleResponse getSchedules(String language, int page, int size) {

        Sort sort = new Sort(Sort.Direction.ASC, Arrays.asList("group", "date"));
        Pageable pageable = new PageRequest(page - 1, size, sort);

        Set<ObjectId> fcIds = new HashSet<>();
        Set<ObjectId> competitionIds = new HashSet<>();
        List<JakduSchedule> jakduSchedules = jakduScheduleRepository.findAll(pageable).getContent();

        for (JakduSchedule jakduSchedule : jakduSchedules) {
            fcIds.add(new ObjectId(jakduSchedule.getHome().getId()));
            fcIds.add(new ObjectId(jakduSchedule.getAway().getId()));

            if (jakduSchedule.getCompetition() != null)
                competitionIds.add(new ObjectId(jakduSchedule.getCompetition().getId()));
        }

        Map<String, LocalName> fcNames = new HashMap<>();
        Map<String, LocalName> competitionNames = new HashMap<>();

        List<FootballClub> footballClubs = jakdukDAO.getFootballClubList(new ArrayList<>(fcIds), language, CommonConst.NAME_TYPE.fullName);
        List<Competition> competitions = jakdukDAO.getCompetitionList(new ArrayList<>(competitionIds), language);

        for (FootballClub fc : footballClubs) {
            fcNames.put(fc.getOrigin().getId(), fc.getNames().get(0));
        }

        for (Competition competition : competitions) {
            competitionNames.put(competition.getId(), competition.getNames().get(0));
        }

        JakduScheduleResponse response = new JakduScheduleResponse();
        response.setJakduSchedules(jakduSchedules);
        response.setFcNames(fcNames);
        response.setCompetitionNames(competitionNames);

        return response;
    }

    public Map getDataSchedule(String id) {

        Map<String, Object> result = new HashMap<>();

        JakduSchedule jakduSchedule = jakduScheduleRepository.findOne(id);

        result.put("jakduSchedule", jakduSchedule);

        return result;
    }

    public Map getDataWrite(Locale locale) {

        Map<String, Object> result = new HashMap<>();

        CommonPrincipal principal = userService.getCommonPrincipal();
        String accountId = principal.getId();
        String accountUsername = principal.getUsername();
        CommonConst.ACCOUNT_TYPE accountType = principal.getType();

        if (accountId == null)
            return result;

        CommonWriter writer = new CommonWriter();
        writer.setUserId(accountId);
        writer.setUsername(accountUsername);
        writer.setType(accountType.name());

        String language = commonService.getLanguageCode(locale, null);

        Set<ObjectId> fcIds = new HashSet<>();
        List<Jakdu> jakdus = new ArrayList<>();
        Set<ObjectId> competitionIds = new HashSet<>();
        List<JakduSchedule> schedules = jakduScheduleRepository.findByTimeUpOrderByDateAsc(false);

        for (JakduSchedule jakduSchedule : schedules) {
            Jakdu jakdu = new Jakdu();
            jakdu.setSchedule(jakduSchedule);
            jakdu.setWriter(writer);
            jakdus.add(jakdu);

            fcIds.add(new ObjectId(jakduSchedule.getHome().getId()));
            fcIds.add(new ObjectId(jakduSchedule.getAway().getId()));
            if (jakduSchedule.getCompetition() != null)
                competitionIds.add(new ObjectId(jakduSchedule.getCompetition().getId()));
        }

        Map<String, LocalName> fcNames = new HashMap<>();
        Map<String, LocalName> competitionNames = new HashMap<>();

        List<FootballClub> footballClubs = jakdukDAO.getFootballClubList(new ArrayList<>(fcIds), language, CommonConst.NAME_TYPE.fullName);
        List<Competition> competitions = jakdukDAO.getCompetitionList(new ArrayList<>(competitionIds), language);

        for (FootballClub fc : footballClubs) {
            fcNames.put(fc.getOrigin().getId(), fc.getNames().get(0));
        }

        for (Competition competition : competitions) {
            competitionNames.put(competition.getId(), competition.getNames().get(0));
        }

        result.put("dateTimeFormat", commonService.getDateTimeFormat(locale));
        result.put("jakdus", jakdus);
        result.put("fcNames", fcNames);
        result.put("competitionNames", competitionNames);

        return result;
    }

    public void getView(Locale locale, Model model, String id) {

        model.addAttribute("id", id);
        try {
            model.addAttribute("dateTimeFormat", new ObjectMapper().writeValueAsString(commonService.getDateTimeFormat(locale)));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.parsing.or.generating"));
        }
    }

    /**
     * 작두 타기 입력
     * @param locale
     * @param myJakdu
     */
    public Jakdu setMyJakdu(Locale locale, MyJakduRequest myJakdu) {
        CommonPrincipal principal = userService.getCommonPrincipal();
        String accountId = principal.getId();

        // 인증되지 않은 회원
        if (Objects.isNull(accountId))
            throw new UnauthorizedAccessException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.access.denied"));

        CommonWriter writer = new CommonWriter(accountId, principal.getUsername(), principal.getType().name());

        JakduSchedule jakduSchedule = jakduScheduleRepository.findOne(myJakdu.getJakduScheduleId());

        if (Objects.isNull(jakduSchedule)) {
            throw new NoSuchElementException(commonService.getResourceBundleMessage(locale, "messages.jakdu", "jakdu.msg.not.found.jakdu.schedule.exception"));
        }

        Jakdu existJakdu = jakduRepository.findByScheduleAndWriter(jakduSchedule, writer);

        if (!Objects.isNull(existJakdu)) {
            throw new RepositoryExistException(commonService.getResourceBundleMessage(locale, "messages.jakdu", "jakdu.msg.already.join.jakdu.exception"));
        }

        Jakdu jakdu = new Jakdu();
        jakdu.setSchedule(jakduSchedule);
        jakdu.setWriter(writer);
        jakdu.setHomeScore(myJakdu.getHomeScore());
        jakdu.setAwayScore(myJakdu.getAwayScore());

        jakduRepository.save(jakdu);

        return jakdu;
    }

    /**
     * 댓글 작성.
     * @param locale
     * @param request
     */
    public JakduComment setComment(Locale locale, JakduCommentWriteRequest request) {

        CommonPrincipal principal = userService.getCommonPrincipal();
        String accountId = principal.getId();

        // 인증되지 않은 회원
        if (Objects.isNull(accountId))
            throw new UnauthorizedAccessException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.access.denied"));

        CommonWriter writer = new CommonWriter(accountId, principal.getUsername(), principal.getType().name());

        JakduSchedule jakduSchedule = jakduScheduleRepository.findOne(request.getId());

        if (Objects.isNull(jakduSchedule)) {
            throw new NoSuchElementException(commonService.getResourceBundleMessage(locale, "messages.jakdu", "jakdu.msg.not.found.jakdu.schedule.exception"));
        }

        BoardCommentStatus status = new BoardCommentStatus();
        status.setDevice(request.getDevice());

        JakduComment jakduComment = new JakduComment();

        jakduComment.setWriter(writer);
        jakduComment.setContents(request.getContents());
        jakduComment.setJakduScheduleId(request.getId());
        jakduComment.setStatus(status);

        jakduCommentRepository.save(jakduComment);

        // 엘라스틱 서치 도큐먼트 생성을 위한 객체.
        JakduCommentOnES jakduCommentOnES = new JakduCommentOnES();
        jakduCommentOnES.setId(jakduComment.getId());
        jakduCommentOnES.setWriter(jakduComment.getWriter());
        jakduCommentOnES.setJakduScheduleId(jakduComment.getJakduScheduleId());
        jakduCommentOnES.setContents(jakduComment.getContents()
                .replaceAll("<(/)?([a-zA-Z0-9]*)(\\s[a-zA-Z0-9]*=[^>]*)?(\\s)*(/)?>","")
                .replaceAll("\r|\n|&nbsp;",""));

        searchService.createDocumentJakduComment(jakduCommentOnES);

        return jakduComment;
    }

    /**
     * 작두 댓글 목록.
     * @param jakduScheduleId
     * @param commentId
     * @return
     */
    public JakduCommentsResponse getComments(String jakduScheduleId, String commentId) {

        List<JakduComment> comments;

        if (Objects.nonNull(commentId) && !commentId.isEmpty()) {
            comments  = jakdukDAO.getJakduComments(jakduScheduleId, new ObjectId(commentId));
        } else {
            comments  = jakdukDAO.getJakduComments(jakduScheduleId, null);
        }

        Integer count = jakduCommentRepository.countByJakduScheduleId(jakduScheduleId);

        JakduCommentsResponse response = new JakduCommentsResponse();
        response.setComments(comments);
        response.setCount(count);

        return response;
    }

    /**
     * 작두 댓글 감정 표현
     * @param locale
     * @param commentId
     * @param feeling
     * @return
     */
    public UserFeelingResponse setJakduCommentFeeling(Locale locale, String commentId, CommonConst.FEELING_TYPE feeling) {

        CommonPrincipal principal = userService.getCommonPrincipal();
        String userId = principal.getId();
        String username = principal.getUsername();

        // 인증되지 않은 회원
        if (Objects.isNull(userId))
            throw new UnauthorizedAccessException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.access.denied"));

        JakduComment jakduComment = jakduCommentRepository.findOne(commentId);
        CommonWriter writer = jakduComment.getWriter();

        List<CommonFeelingUser> usersLiking = jakduComment.getUsersLiking();
        List<CommonFeelingUser> usersDisliking = jakduComment.getUsersDisliking();

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

        UserFeelingResponse response = new UserFeelingResponse();

        CommonFeelingUser feelingUser = new CommonFeelingUser();
        feelingUser.setUserId(userId);
        feelingUser.setUsername(username);
        feelingUser.setId(new ObjectId().toString());

        switch (feeling) {
            case LIKE:
                usersLiking.add(feelingUser);
                jakduComment.setUsersLiking(usersLiking);
                break;
            case DISLIKE:
                usersDisliking.add(feelingUser);
                jakduComment.setUsersDisliking(usersDisliking);
                break;
            default:
                break;
        }

        jakduCommentRepository.save(jakduComment);

        response.setFeeling(feeling.toString());
        response.setNumberOfLike(usersLiking.size());
        response.setNumberOfDislike(usersDisliking.size());

        return response;
    }
}
