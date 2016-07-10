package com.jakduk.restcontroller.board;

import com.jakduk.authentication.common.CommonPrincipal;
import com.jakduk.common.CommonConst;
import com.jakduk.exception.UnauthorizedAccessException;
import com.jakduk.model.db.BoardFree;
import com.jakduk.model.db.BoardFreeComment;
import com.jakduk.model.embedded.BoardItem;
import com.jakduk.model.simple.BoardFreeOfMinimum;
import com.jakduk.model.simple.BoardFreeOnList;
import com.jakduk.model.web.BoardListInfo;
import com.jakduk.restcontroller.board.vo.BoardCommentRequest;
import com.jakduk.restcontroller.board.vo.BoardCommentsResponse;
import com.jakduk.restcontroller.board.vo.PostsResponse;
import com.jakduk.restcontroller.vo.UserFeelingResponse;
import com.jakduk.service.BoardFreeService;
import com.jakduk.service.CommonService;
import com.jakduk.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author pyohwan
 * 16. 3. 26 오후 11:05
 */

@Api(tags = "게시판", description = "게시판 관련")
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

    @ApiOperation(value = "게시물 목록", produces = "application/json")
    @RequestMapping(value = "/free/posts", method = RequestMethod.GET)
    public Map<String, Object> freeList(@RequestParam(required = false) String page,
                                        @RequestParam(required = false) String size,
                                        @RequestParam(required = false) String category,
                                        HttpServletRequest request) {

        BoardListInfo paging = new BoardListInfo();
        paging.setPage(Objects.isNull(page) ? 1 : Integer.parseInt(page));
        paging.setSize(Objects.isNull(size) ? CommonConst.BOARD_MAX_LIMIT : Integer.parseInt(size));
        paging.setCategory(Objects.isNull(category) ? CommonConst.BOARD_CATEGORY_ALL : category);
        Locale locale = localeResolver.resolveLocale(request);
        return boardFreeService.getFreePostsList(locale, paging);
    }

    @ApiOperation(value = "게시물 목록", produces = "application/json", response = PostsResponse.class)
    @RequestMapping(value = "/free/posts2", method = RequestMethod.GET)
    public PostsResponse getPosts(@RequestParam(required = false) Integer page,
                                        @RequestParam(required = false) Integer size,
                                        @RequestParam(required = false, defaultValue = "ALL") CommonConst.BOARD_CATEGORY_TYPE category,
                                        HttpServletRequest request) {

        if (Objects.isNull(page))
            page = 1;

        if (Objects.isNull(size))
            size = CommonConst.BOARD_MAX_LIMIT;

        if (Objects.isNull(category))
            category = CommonConst.BOARD_CATEGORY_TYPE.ALL;

        Page<BoardFreeOnList> posts = boardFreeService.getPosts(category, page, size);


        PostsResponse response = PostsResponse.builder()
                .posts(posts.getContent())
                .first(posts.isFirst())
                .last(posts.isLast())
                .totalPages(posts.getTotalPages())
                .totalElements(posts.getTotalElements())
                .numberOfElements(posts.getNumberOfElements())
                .size(posts.getSize())
                .number(posts.getNumber())
                .build();

        return response;
    }

    @RequestMapping(value = "/free/tops", method = RequestMethod.GET)
    public void dataFreeTopList(Model model) {

        Integer status = boardFreeService.getDataFreeTopList(model);
    }

    @RequestMapping(value = "/data/free/comments", method = RequestMethod.GET)
    public void dataFreeCommentsList(Model model,
                                     @RequestParam(required = false, defaultValue = "1") int page,
                                     @RequestParam(required = false, defaultValue = "20") int size) {

        Integer status = boardFreeService.getDataFreeCommentsList(model, page, size);
    }

    @ApiOperation(value = "게시판 댓글 목록")
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

    @ApiOperation(value = "게시판 댓글 달기")
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

    @ApiOperation(value = "글 감정 표현")
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

    @ApiOperation(value = "댓글 감정 표현")
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
