package com.jakduk.core.service;

import com.jakduk.core.authentication.common.CommonPrincipal;
import com.jakduk.core.common.CommonConst;
import com.jakduk.core.dao.JakdukDAO;
import com.jakduk.core.exception.UnauthorizedAccessException;
import com.jakduk.core.exception.UserFeelingException;
import com.jakduk.core.model.db.*;
import com.jakduk.core.model.embedded.BoardCommentStatus;
import com.jakduk.core.model.embedded.LocalName;
import com.jakduk.core.model.web.jakdu.MyJakduRequest;
import com.jakduk.core.repository.jakdu.JakduRepository;
import com.jakduk.core.repository.jakdu.JakduScheduleRepository;
import com.jakduk.core.exception.RepositoryExistException;
import com.jakduk.core.model.elasticsearch.JakduCommentOnES;
import com.jakduk.core.model.embedded.CommonFeelingUser;
import com.jakduk.core.model.embedded.CommonWriter;
import com.jakduk.core.model.simple.JakduOnSchedule;
import com.jakduk.core.model.web.jakdu.JakduCommentWriteRequest;
import com.jakduk.core.model.web.jakdu.JakduCommentsResponse;
import com.jakduk.core.repository.jakdu.JakduCommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author pyohwan
 * 15. 12. 26 오후 11:04
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

    public JakduSchedule findScheduleById(String id) {
        return jakduScheduleRepository.findOne(id);
    }

    public Page<JakduSchedule> findAll(Pageable pageable) {
        return jakduScheduleRepository.findAll(pageable);
    }

    public Map getDataWrite(Locale locale) {

        Map<String, Object> result = new HashMap<>();

        CommonPrincipal principal = userService.getCommonPrincipal();
        String accountId = principal.getId();
        String accountUsername = principal.getUsername();
        CommonConst.ACCOUNT_TYPE accountType = principal.getProviderId();

        if (accountId == null)
            return result;

        CommonWriter writer = new CommonWriter(accountId, accountUsername, accountType);

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

        List<FootballClub> footballClubs = jakdukDAO.getFootballClubs(new ArrayList<>(fcIds), language, CommonConst.NAME_TYPE.fullName);
        List<Competition> competitions = jakdukDAO.getCompetitions(new ArrayList<>(competitionIds), language);

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

    // 작두 타기 입력
    public Jakdu setMyJakdu(Locale locale, MyJakduRequest myJakdu) {
        CommonPrincipal principal = userService.getCommonPrincipal();
        String accountId = principal.getId();

        CommonWriter writer = new CommonWriter(accountId, principal.getUsername(), principal.getProviderId());

        JakduSchedule jakduSchedule = jakduScheduleRepository.findOne(myJakdu.getJakduScheduleId());

        if (Objects.isNull(jakduSchedule))
            throw new NoSuchElementException(commonService.getResourceBundleMessage(locale, "messages.jakdu", "jakdu.msg.not.found.jakdu.schedule.exception"));

        JakduOnSchedule existJakdu = jakduRepository.findByUserIdAndWriter(accountId, new ObjectId(jakduSchedule.getId()));

        if (Objects.nonNull(existJakdu))
            throw new RepositoryExistException(commonService.getResourceBundleMessage(locale, "messages.jakdu", "jakdu.msg.already.join.jakdu.exception"));

        Jakdu jakdu = new Jakdu();
        jakdu.setSchedule(jakduSchedule);
        jakdu.setWriter(writer);
        jakdu.setHomeScore(myJakdu.getHomeScore());
        jakdu.setAwayScore(myJakdu.getAwayScore());

        jakduRepository.save(jakdu);

        return jakdu;
    }

    // 내 작두 타기 가져오기.
    public JakduOnSchedule getMyJakdu(Locale locale, String jakdukScheduleId) {
        CommonPrincipal principal = userService.getCommonPrincipal();
        String accountId = principal.getId();

        JakduSchedule jakduSchedule = jakduScheduleRepository.findOne(jakdukScheduleId);

        if (Objects.isNull(jakduSchedule))
            throw new NoSuchElementException(commonService.getResourceBundleMessage(locale, "messages.jakdu", "jakdu.msg.not.found.jakdu.schedule.exception"));

        JakduOnSchedule myJakdu = jakduRepository.findByUserIdAndWriter(accountId, new ObjectId(jakduSchedule.getId()));

        return myJakdu;
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
            throw new UnauthorizedAccessException(commonService.getResourceBundleMessage(locale, "messages.exception", "exception.access.denied"));

        CommonWriter writer = new CommonWriter(accountId, principal.getUsername(), principal.getProviderId());

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
    public JakduComment setJakduCommentFeeling(Locale locale, String commentId, CommonConst.FEELING_TYPE feeling) {

        CommonPrincipal principal = userService.getCommonPrincipal();
        String userId = principal.getId();
        String username = principal.getUsername();

        // 인증되지 않은 회원
        if (Objects.isNull(userId))
            throw new UnauthorizedAccessException(commonService.getResourceBundleMessage(locale, "messages.exception", "exception.access.denied"));

        JakduComment jakduComment = jakduCommentRepository.findOne(commentId);
        CommonWriter writer = jakduComment.getWriter();

        List<CommonFeelingUser> usersLiking = jakduComment.getUsersLiking();
        List<CommonFeelingUser> usersDisliking = jakduComment.getUsersDisliking();

        if (Objects.isNull(usersLiking)) usersLiking = new ArrayList<>();
        if (Objects.isNull(usersDisliking)) usersDisliking = new ArrayList<>();

        // 이 게시물의 작성자라서 감정 표현을 할 수 없음
        if (userId.equals(writer.getUserId())) {
            throw new UserFeelingException(CommonConst.USER_FEELING_ERROR_CODE.WRITER.toString()
                    , commonService.getResourceBundleMessage(locale, "messages.exception", "exception.you.are.writer"));
        }

        // 해당 회원이 좋아요를 이미 했는지 검사
        for (CommonFeelingUser feelingUser : usersLiking) {
            if (Objects.nonNull(feelingUser) && userId.equals(feelingUser.getUserId())) {
                throw new UserFeelingException(CommonConst.USER_FEELING_ERROR_CODE.ALREADY.toString()
                        , commonService.getResourceBundleMessage(locale, "messages.exception", "exception.select.already.like"));
            }
        }

        // 해당 회원이 싫어요를 이미 했는지 검사
        for (CommonFeelingUser feelingUser : usersDisliking) {
            if (Objects.nonNull(feelingUser) && userId.equals(feelingUser.getUserId())) {
                throw new UserFeelingException(CommonConst.USER_FEELING_ERROR_CODE.ALREADY.toString()
                        , commonService.getResourceBundleMessage(locale, "messages.exception", "exception.select.already.like"));
            }
        }

        CommonFeelingUser feelingUser = new CommonFeelingUser(new ObjectId().toString(), userId, username);

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

        return jakduComment;
    }
}
