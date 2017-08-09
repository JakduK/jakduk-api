package com.jakduk.api.service;


import com.jakduk.api.common.Constants;
import com.jakduk.api.common.util.JakdukUtils;
import com.jakduk.api.dao.JakdukDAO;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.db.Jakdu;
import com.jakduk.api.model.db.JakduComment;
import com.jakduk.api.model.db.JakduSchedule;
import com.jakduk.api.model.elasticsearch.EsJakduComment;
import com.jakduk.api.model.embedded.BoardCommentStatus;
import com.jakduk.api.model.embedded.CommonFeelingUser;
import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.model.simple.JakduOnSchedule;
import com.jakduk.api.restcontroller.vo.admin.JakduCommentWriteRequest;
import com.jakduk.api.restcontroller.vo.admin.JakduCommentsResponse;
import com.jakduk.api.restcontroller.vo.admin.MyJakduRequest;
import com.jakduk.api.repository.jakdu.JakduCommentRepository;
import com.jakduk.api.repository.jakdu.JakduRepository;
import com.jakduk.api.repository.jakdu.JakduScheduleRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * @author pyohwan
 * 15. 12. 26 오후 11:04
 */

@Service
@Slf4j
public class JakduService {

    @Autowired private JakduRepository jakduRepository;
    @Autowired private JakduScheduleRepository jakduScheduleRepository;
    @Autowired private JakduCommentRepository jakduCommentRepository;
    @Autowired private JakdukDAO jakdukDAO;
    @Autowired private SearchService searchService;

    public JakduSchedule findScheduleById(String id) {
        return jakduScheduleRepository.findOne(id);
    }

    public Page<JakduSchedule> findAll(Pageable pageable) {
        return jakduScheduleRepository.findAll(pageable);
    }

    // 작두 타기 입력
    public Jakdu setMyJakdu(CommonWriter writer, MyJakduRequest myJakdu) {
        JakduSchedule jakduSchedule = jakduScheduleRepository.findOne(myJakdu.getJakduScheduleId());

        if (Objects.isNull(jakduSchedule))
            throw new NoSuchElementException(JakdukUtils.getResourceBundleMessage("messages.jakdu", "jakdu.msg.not.found.jakdu.schedule.exception"));

        JakduOnSchedule existJakdu = jakduRepository.findByUserIdAndWriter(writer.getUserId(), new ObjectId(jakduSchedule.getId()));

        if (Objects.nonNull(existJakdu))
            throw new ServiceException(ServiceError.INTERNAL_SERVER_ERROR, JakdukUtils.getResourceBundleMessage("messages.jakdu", "jakdu.msg.already.join.jakdu.exception"));

        Jakdu jakdu = new Jakdu();
        jakdu.setSchedule(jakduSchedule);
        jakdu.setWriter(writer);
        jakdu.setHomeScore(myJakdu.getHomeScore());
        jakdu.setAwayScore(myJakdu.getAwayScore());

        jakduRepository.save(jakdu);

        return jakdu;
    }

    // 내 작두 타기 가져오기.
    public JakduOnSchedule getMyJakdu(String userId, String jakdukScheduleId) {
        JakduSchedule jakduSchedule = jakduScheduleRepository.findOne(jakdukScheduleId);

        if (Objects.isNull(jakduSchedule))
            throw new NoSuchElementException(JakdukUtils.getResourceBundleMessage("messages.jakdu", "jakdu.msg.not.found.jakdu.schedule.exception"));

        return jakduRepository.findByUserIdAndWriter(userId, new ObjectId(jakduSchedule.getId()));
    }

    /**
     * 댓글 작성.
     */
    public JakduComment setComment(CommonWriter writer, JakduCommentWriteRequest request) {
        JakduSchedule jakduSchedule = jakduScheduleRepository.findOne(request.getId());

        if (Objects.isNull(jakduSchedule)) {
            throw new NoSuchElementException(JakdukUtils.getResourceBundleMessage("messages.jakdu", "jakdu.msg.not.found.jakdu.schedule.exception"));
        }

        BoardCommentStatus status = new BoardCommentStatus(request.getDevice());

        JakduComment jakduComment = new JakduComment();

        jakduComment.setWriter(writer);
        jakduComment.setContents(request.getContents());
        jakduComment.setJakduScheduleId(request.getId());
        jakduComment.setStatus(status);

        jakduCommentRepository.save(jakduComment);

        // 엘라스틱 서치 도큐먼트 생성을 위한 객체.
        EsJakduComment EsJakduComment = new EsJakduComment();
        EsJakduComment.setId(jakduComment.getId());
        EsJakduComment.setWriter(jakduComment.getWriter());
        EsJakduComment.setJakduScheduleId(jakduComment.getJakduScheduleId());
        EsJakduComment.setContents(jakduComment.getContents()
                .replaceAll("<(/)?([a-zA-Z0-9]*)(\\s[a-zA-Z0-9]*=[^>]*)?(\\s)*(/)?>","")
                .replaceAll("\r|\n|&nbsp;",""));

        searchService.createDocumentJakduComment(EsJakduComment);

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
     */
    public JakduComment setJakduCommentFeeling(CommonWriter writer, String commentId, Constants.FEELING_TYPE feeling) {

        String userId = writer.getUserId();
        String username = writer.getUsername();

        JakduComment jakduComment = jakduCommentRepository.findOne(commentId);
        CommonWriter jakdukWriter = jakduComment.getWriter();

        List<CommonFeelingUser> usersLiking = jakduComment.getUsersLiking();
        List<CommonFeelingUser> usersDisliking = jakduComment.getUsersDisliking();

        if (Objects.isNull(usersLiking)) usersLiking = new ArrayList<>();
        if (Objects.isNull(usersDisliking)) usersDisliking = new ArrayList<>();

        // 이 게시물의 작성자라서 감정 표현을 할 수 없음
        if (userId.equals(jakdukWriter.getUserId()))
            throw new ServiceException(ServiceError.FEELING_YOU_ARE_WRITER);

        // 해당 회원이 좋아요를 이미 했는지 검사
        for (CommonFeelingUser feelingUser : usersLiking) {
            if (Objects.nonNull(feelingUser) && userId.equals(feelingUser.getUserId()))
                throw new ServiceException(ServiceError.FEELING_SELECT_ALREADY_LIKE);
        }

        // 해당 회원이 싫어요를 이미 했는지 검사
        for (CommonFeelingUser feelingUser : usersDisliking) {
            if (Objects.nonNull(feelingUser) && userId.equals(feelingUser.getUserId()))
                throw new ServiceException(ServiceError.FEELING_SELECT_ALREADY_LIKE);
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
