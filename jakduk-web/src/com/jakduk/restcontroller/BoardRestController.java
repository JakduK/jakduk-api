package com.jakduk.restcontroller;

import com.jakduk.authentication.common.CommonPrincipal;
import com.jakduk.common.CommonConst;
import com.jakduk.exception.UnauthorizedAccessException;
import com.jakduk.model.db.BoardFree;
import com.jakduk.model.db.BoardFreeComment;
import com.jakduk.model.embedded.BoardItem;
import com.jakduk.model.simple.BoardFreeOfMinimum;
import com.jakduk.service.BoardFreeService;
import com.jakduk.service.CommonService;
import com.jakduk.service.UserService;
import com.jakduk.vo.BoardCommentRequest;
import com.jakduk.vo.BoardCommentsResponse;
import com.jakduk.vo.UserFeelingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by pyohwan on 16. 3. 26.
 */

@RestController
@RequestMapping("/api/board")
public class BoardRestController {

    @Resource
    private LocaleResolver localeResolver;

    @Autowired
    private BoardFreeService boardFreeService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommonService commonService;

    // 게시판 댓글 목록
    @RequestMapping(value = "/free/comments/{seq}", method = RequestMethod.GET)
    public BoardCommentsResponse freeComment(@PathVariable Integer seq,
                            @RequestParam(required = false) String commentId) {

        BoardFreeOfMinimum boardFreeOnComment = boardFreeService.findBoardFreeOfMinimumBySeq(seq);

        List<BoardFreeComment> comments = boardFreeService.getFreeComments(seq, commentId);

        BoardItem boardItem = new BoardItem(boardFreeOnComment.getId(), boardFreeOnComment.getSeq());

        Integer count = boardFreeService.countCommentsByBoardItem(boardItem);

        BoardCommentsResponse response = new BoardCommentsResponse();
        response.setComments(comments);
        response.setCount(count);

        return response;
    }

    // 게시판 댓글 달기
    @RequestMapping(value ="/free/comment", method = RequestMethod.POST)
    public BoardFreeComment commentWrite(@RequestBody BoardCommentRequest commentRequest,
                             HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);

        if (Objects.isNull(commentRequest)) {
            throw new IllegalArgumentException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.invalid.parameter"));
        }

        Integer seq = commentRequest.getSeq();
        String contents = commentRequest.getContents();

        if (Objects.isNull(seq) || Objects.isNull(contents)) {
            throw new IllegalArgumentException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.invalid.parameter"));
        }

        CommonPrincipal principal = userService.getCommonPrincipal();
        String accountId = principal.getId();

        // 인증되지 않은 회원
        if (Objects.isNull(accountId))
            throw new UnauthorizedAccessException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.access.denied"));

        Device device = DeviceUtils.getCurrentDevice(request);

        BoardFreeComment boardFreeComment = boardFreeService.addFreeComment(seq, contents, commonService.getDeviceInfo(device));

        return boardFreeComment;
    }

    // 글 감정 표현.
    @RequestMapping(value = "/free/{seq}/{feeling}", method = RequestMethod.POST)
    public UserFeelingResponse setFreeFeeling(@PathVariable Integer seq,
                               @PathVariable CommonConst.FEELING_TYPE feeling,
                               HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);

        CommonPrincipal principal = userService.getCommonPrincipal();
        String userId = principal.getId();

        // 인증되지 않은 회원
        if (Objects.isNull(userId))
            throw new UnauthorizedAccessException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.access.denied"));

        BoardFree boardFree = boardFreeService.setFreeFeelings(locale, seq, feeling);

        Integer numberOfLike = Objects.nonNull(boardFree.getUsersLiking()) ? boardFree.getUsersLiking().size() : 0;
        Integer numberOfDisLike = Objects.nonNull(boardFree.getUsersDisliking()) ? boardFree.getUsersDisliking().size() : 0;

        UserFeelingResponse response = new UserFeelingResponse();
        response.setFeeling(feeling);
        response.setNumberOfLike(numberOfLike);
        response.setNumberOfDislike(numberOfDisLike);

        return response;
    }

    // 댓글 감정 표현.
    @RequestMapping(value = "/comment/{commentId}/{feeling}", method = RequestMethod.POST)
    public UserFeelingResponse setFreeCommentFeeling(@PathVariable String commentId,
                                                     @PathVariable CommonConst.FEELING_TYPE feeling,
                                                     HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);

        CommonPrincipal principal = userService.getCommonPrincipal();
        String userId = principal.getId();

        // 인증되지 않은 회원
        if (Objects.isNull(userId))
            throw new UnauthorizedAccessException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.access.denied"));

        BoardFreeComment boardFreeComment = boardFreeService.setFreeCommentFeeling(locale, commentId, feeling);

        Integer numberOfLike = Objects.nonNull(boardFreeComment.getUsersLiking()) ? boardFreeComment.getUsersLiking().size() : 0;
        Integer numberOfDisLike = Objects.nonNull(boardFreeComment.getUsersDisliking()) ? boardFreeComment.getUsersDisliking().size() : 0;

        UserFeelingResponse response = new UserFeelingResponse();
        response.setFeeling(feeling);
        response.setNumberOfLike(numberOfLike);
        response.setNumberOfDislike(numberOfDisLike);

        return response;
    }
}
