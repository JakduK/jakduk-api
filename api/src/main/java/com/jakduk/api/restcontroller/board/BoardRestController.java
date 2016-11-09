package com.jakduk.api.restcontroller.board;

import com.jakduk.api.common.ApiConst;
import com.jakduk.api.common.util.ApiUtils;
import com.jakduk.api.common.util.UserUtils;
import com.jakduk.api.configuration.authentication.user.CommonPrincipal;
import com.jakduk.api.restcontroller.EmptyJsonResponse;
import com.jakduk.api.restcontroller.board.vo.*;
import com.jakduk.api.restcontroller.vo.UserFeelingResponse;
import com.jakduk.core.common.CommonConst;
import com.jakduk.core.common.util.CoreUtils;
import com.jakduk.core.dao.BoardDAO;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.model.db.BoardCategory;
import com.jakduk.core.model.db.BoardFree;
import com.jakduk.core.model.db.BoardFreeComment;
import com.jakduk.core.model.embedded.BoardItem;
import com.jakduk.core.model.embedded.CommonWriter;
import com.jakduk.core.model.etc.BoardFeelingCount;
import com.jakduk.core.model.etc.BoardFreeOnBest;
import com.jakduk.core.model.etc.GalleryOnBoard;
import com.jakduk.core.model.simple.BoardFreeOfMinimum;
import com.jakduk.core.model.simple.BoardFreeOnList;
import com.jakduk.core.model.simple.BoardFreeOnSearchComment;
import com.jakduk.core.model.simple.BoardFreeSimple;
import com.jakduk.core.model.web.board.BoardFreeDetail;
import com.jakduk.core.service.BoardCategoryService;
import com.jakduk.core.service.BoardFreeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.mobile.device.Device;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author pyohwan
 * 16. 3. 26 오후 11:05
 */

@Slf4j
@Api(tags = "BoardFree", description = "자유게시판 API")
@RestController
@RequestMapping("/api/board/free")
public class BoardRestController {

    @Autowired
    private BoardFreeService boardFreeService;

    @Autowired
    private BoardCategoryService boardCategoryService;

    @Autowired
    private BoardDAO boardDAO;

    @ApiOperation(value = "자유게시판 글 목록")
    @RequestMapping(value = "/posts", method = RequestMethod.GET)
    public FreePostsOnListResponse getFreePosts(@RequestParam(required = false, defaultValue = "1") Integer page,
                                            @RequestParam(required = false, defaultValue = "20") Integer size,
                                            @RequestParam(required = false, defaultValue = "ALL") CommonConst.BOARD_CATEGORY_TYPE category) {

        if (Objects.isNull(category))
            category = CommonConst.BOARD_CATEGORY_TYPE.ALL;

        Page<BoardFreeOnList> posts = boardFreeService.getFreePosts(category, page, size);
        Page<BoardFreeOnList> notices = boardFreeService.getFreeNotices();

        ArrayList<Integer> seqs = new ArrayList<>();
        ArrayList<ObjectId> ids = new ArrayList<>();

        // id와 seq 뽑아내기.
        Consumer<BoardFreeOnList> extractIdAndSeq = board -> {
            String tempId = board.getId();
            Integer tempSeq = board.getSeq();

            ObjectId objId = new ObjectId(tempId);

            seqs.add(tempSeq);
            ids.add(objId);
        };

        posts.getContent().forEach(extractIdAndSeq);
        notices.getContent().forEach(extractIdAndSeq);

        Map<String, Integer> commentCounts = boardDAO.getBoardFreeCommentCount(seqs);
        Map<String, BoardFeelingCount> feelingCounts = boardDAO.getBoardFreeUsersFeelingCount(ids);

        // 댓글수, 감정 표현수 합치기.
        Consumer<FreePostsOnList> applyCounts = board -> {
            String tempId = board.getId();

            Integer commentCount = commentCounts.get(tempId);

            if (Objects.nonNull(commentCount))
                board.setCommentCount(commentCount);

            BoardFeelingCount feelingCount = feelingCounts.get(tempId);

            if (Objects.nonNull(feelingCount)) {
                board.setLikingCount(feelingCount.getUsersLikingCount());
                board.setDislikingCount(feelingCount.getUsersDisLikingCount());
            }
        };

        List<FreePostsOnList> freePosts = posts.getContent().stream()
                .map(FreePostsOnList::new)
                .collect(Collectors.toList());

        freePosts.forEach(applyCounts);

        List<FreePostsOnList> freeNotices = notices.getContent().stream()
                .map(FreePostsOnList::new)
                .collect(Collectors.toList());

        freeNotices.forEach(applyCounts);

        List<BoardCategory> categories = boardFreeService.getFreeCategories();
        Map<String, String> categoriesMap = categories.stream().collect(Collectors.toMap(BoardCategory::getCode, boardCategory -> boardCategory.getNames().get(0).getName()));
        categoriesMap.put("ALL", CoreUtils.getResourceBundleMessage("messages.board", "board.category.all"));

        return FreePostsOnListResponse.builder()
                .categories(categoriesMap)
                .posts(freePosts)
                .notices(freeNotices)
                .first(posts.isFirst())
                .last(posts.isLast())
                .totalPages(posts.getTotalPages())
                .totalElements(posts.getTotalElements())
                .numberOfElements(posts.getNumberOfElements())
                .size(posts.getSize())
                .number(posts.getNumber())
                .build();
    }

    @ApiOperation(value = "자유게시판 주간 선두 글", response = FreeTopsResponse.class)
    @RequestMapping(value = "/tops", method = RequestMethod.GET)
    public FreeTopsResponse getFreePostsTops() {

        List<BoardFreeOnBest> topLikes = boardFreeService.getFreeTopLikes();
        List<BoardFreeOnBest> topComments = boardFreeService.getFreeTopComments();

        return FreeTopsResponse.builder()
                .topLikes(topLikes)
                .topComments(topComments)
                .build();
    }

    @ApiOperation(value = "자유게시판 댓글 목록")
    @RequestMapping(value = "/comments", method = RequestMethod.GET)
    public FreeCommentsOnListResponse getFreeComments(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "20") int size) {

        Page<BoardFreeComment> comments = boardFreeService.getBoardFreeComments(page, size);

        // id 뽑아내기.
        List<ObjectId> boardIds = comments.getContent().stream()
                .map(comment -> {
                    String tempId = comment.getBoardItem().getId();
                    return new ObjectId(tempId);
                })
                .distinct()
                .collect(Collectors.toList());

        Map<String, BoardFreeOnSearchComment> postsHavingComments = boardDAO.getBoardFreeOnSearchComment(boardIds);

        List<FreeCommentsOnList> freeComments = comments.getContent().stream()
                .map(comment -> {
                            FreeCommentsOnList newComment = new FreeCommentsOnList();
                            BeanUtils.copyProperties(comment, newComment);
                            newComment.setBoardItem(
                                    Optional.ofNullable(postsHavingComments.get(comment.getBoardItem().getId()))
                                            .orElse(new BoardFreeOnSearchComment())
                            );
                            return newComment;
                        }
                )
                .collect(Collectors.toList());

        return FreeCommentsOnListResponse.builder()
                .comments(freeComments)
                .first(comments.isFirst())
                .last(comments.isLast())
                .totalPages(comments.getTotalPages())
                .totalElements(comments.getTotalElements())
                .numberOfElements(comments.getNumberOfElements())
                .size(comments.getSize())
                .number(comments.getNumber())
                .build();
    }

    @ApiOperation(value = "자유게시판 글 상세")
    @RequestMapping(value = "/{seq}", method = RequestMethod.GET)
    public FreePostOnDetailResponse getFreeView(@PathVariable Integer seq,
                                                Locale locale,
                                                HttpServletRequest request,
                                                HttpServletResponse response) {

        Boolean isAddCookie = ApiUtils.addViewsCookie(request, response, ApiConst.VIEWS_COOKIE_TYPE.FREE_BOARD, String.valueOf(seq));

        BoardFreeDetail boardFreeDetail = boardFreeService.getPost(seq, CoreUtils.getLanguageCode(locale, null), isAddCookie);

        CommonConst.BOARD_CATEGORY_TYPE categoryType = CommonConst.BOARD_CATEGORY_TYPE.valueOf(boardFreeDetail.getCategory().getCode());

        BoardFreeOfMinimum prevPost = boardDAO.getBoardFreeById(new ObjectId(boardFreeDetail.getId())
                , categoryType, Sort.Direction.ASC);
        BoardFreeOfMinimum nextPost = boardDAO.getBoardFreeById(new ObjectId(boardFreeDetail.getId())
                , categoryType, Sort.Direction.DESC);

        List<BoardFreeSimple> latestPostsByWriter = null;

        if (ObjectUtils.isEmpty(boardFreeDetail.getStatus()) || BooleanUtils.isNotTrue(boardFreeDetail.getStatus().getDelete()))
            latestPostsByWriter = boardFreeService.findByUserId(boardFreeDetail.getId(), boardFreeDetail.getWriter().getUserId(), 3);

        return FreePostOnDetailResponse.builder()
                .post(boardFreeDetail)
                .prevPost(prevPost)
                .nextPost(nextPost)
                .latestPostsByWriter(latestPostsByWriter)
                .build();
    }

    @ApiOperation(value = "자유게시판 말머리 목록")
    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public FreeCategoriesResponse getFreeCategories() {

        List<BoardCategory> categories = boardFreeService.getFreeCategories();

        return FreeCategoriesResponse.builder()
                .categories(categories)
                .build();
    }

    @ApiOperation(value = "자유게시판 글쓰기")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public FreePostOnWriteResponse addFreePost(@Valid @RequestBody FreePostForm form,
                                               Device device) {

        if (! UserUtils.isUser())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

        Optional<BoardCategory> boardCategory = boardCategoryService.findOneByCode(form.getCategoryCode().name());

        if (!boardCategory.isPresent())
            throw new ServiceException(ServiceError.CATEGORY_NOT_FOUND);

        List<GalleryOnBoard> galleries = new ArrayList<>();

        if (Objects.nonNull(form.getGalleries())) {
            galleries = form.getGalleries().stream()
                    .map(gallery -> new GalleryOnBoard(gallery.getId(), gallery.getName(), gallery.getFileName(), gallery.getSize()))
                    .collect(Collectors.toList());
        }

        CommonPrincipal principal = UserUtils.getCommonPrincipal();
        CommonWriter writer = new CommonWriter(principal.getId(), principal.getUsername(), principal.getProviderId());

        Integer boardSeq = boardFreeService.insertFreePost(writer, form.getSubject().trim(), form.getContent().trim(),
                form.getCategoryCode(), galleries, ApiUtils.getDeviceInfo(device));

        return FreePostOnWriteResponse.builder()
                .seq(boardSeq)
                .build();
    }

    @ApiOperation(value = "자유게시판 글 고치기")
    @RequestMapping(value = "/{seq}", method = RequestMethod.PUT)
    public FreePostOnWriteResponse editFreePost(@PathVariable Integer seq,
                                                @Valid @RequestBody FreePostForm form,
                                                Device device) {

        if (! UserUtils.isUser())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

        Optional<BoardCategory> boardCategory = boardCategoryService.findOneByCode(form.getCategoryCode().name());

        if (!boardCategory.isPresent())
            throw new ServiceException(ServiceError.CATEGORY_NOT_FOUND);

        List<GalleryOnBoard> galleries = new ArrayList<>();

        if (Objects.nonNull(form.getGalleries())) {
            galleries = form.getGalleries().stream()
                    .map(gallery -> new GalleryOnBoard(gallery.getId(), gallery.getName(), gallery.getFileName(), gallery.getSize()))
                    .collect(Collectors.toList());
        }

        CommonPrincipal principal = UserUtils.getCommonPrincipal();
        CommonWriter writer = new CommonWriter(principal.getId(), principal.getUsername(), principal.getProviderId());

        Integer boardSeq = boardFreeService.updateFreePost(writer, seq, form.getSubject().trim(), form.getContent().trim(),
                form.getCategoryCode(), galleries, ApiUtils.getDeviceInfo(device));

        return FreePostOnWriteResponse.builder()
                .seq(boardSeq)
                .build();
    }

    @ApiOperation(value = "자유게시판 글 지움")
    @RequestMapping(value = "/{seq}", method = RequestMethod.DELETE)
    public FreePostOnDeleteResponse deleteFree(@PathVariable Integer seq) {

        if (! UserUtils.isUser())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

        CommonPrincipal principal = UserUtils.getCommonPrincipal();
        CommonWriter writer = new CommonWriter(principal.getId(), principal.getUsername(), principal.getProviderId());

		CommonConst.BOARD_DELETE_TYPE deleteType = boardFreeService.deleteFreePost(writer, seq);

        return FreePostOnDeleteResponse.builder().result(deleteType).build();
    }

    @ApiOperation(value = "자유게시판 글의 댓글 목록")
    @RequestMapping(value = "/comments/{seq}", method = RequestMethod.GET)
    public BoardCommentsResponse getFreeComments(@PathVariable Integer seq,
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

    @ApiOperation(value = "자유게시판 글의 댓글 달기")
    @RequestMapping(value ="/comment", method = RequestMethod.POST)
    public BoardFreeComment addFreeComment(@Valid @RequestBody BoardCommentForm commentRequest,
                                           Device device) {

          if (! UserUtils.isUser())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

        CommonPrincipal principal = UserUtils.getCommonPrincipal();
        CommonWriter writer = new CommonWriter(principal.getId(), principal.getUsername(), principal.getProviderId());

        return boardFreeService.addFreeComment(writer, commentRequest.getSeq(), commentRequest.getContent().trim(), ApiUtils.getDeviceInfo(device));
    }

    @ApiOperation(value = "자유게시판 글 감정 표현")
    @RequestMapping(value = "/{seq}/{feeling}", method = RequestMethod.POST)
    public UserFeelingResponse addFreeFeeling(@PathVariable Integer seq,
                                              @PathVariable CommonConst.FEELING_TYPE feeling) {

        if (! UserUtils.isUser())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

        CommonPrincipal principal = UserUtils.getCommonPrincipal();
        CommonWriter writer = new CommonWriter(principal.getId(), principal.getUsername(), principal.getProviderId());

        BoardFree boardFree = boardFreeService.setFreeFeelings(writer, seq, feeling);

        Integer numberOfLike = Objects.nonNull(boardFree.getUsersLiking()) ? boardFree.getUsersLiking().size() : 0;
        Integer numberOfDisLike = Objects.nonNull(boardFree.getUsersDisliking()) ? boardFree.getUsersDisliking().size() : 0;

        return UserFeelingResponse.builder()
                .feeling(feeling)
                .numberOfLike(numberOfLike)
                .numberOfDislike(numberOfDisLike)
                .build();
    }

    @ApiOperation(value = "자유게시판 댓글 감정 표현")
    @RequestMapping(value = "/comment/{commentId}/{feeling}", method = RequestMethod.POST)
    public UserFeelingResponse addFreeCommentFeeling(@PathVariable String commentId,
                                                     @PathVariable CommonConst.FEELING_TYPE feeling) {

        if (! UserUtils.isUser())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

        CommonPrincipal principal = UserUtils.getCommonPrincipal();
        CommonWriter writer = new CommonWriter(principal.getId(), principal.getUsername(), principal.getProviderId());

        BoardFreeComment boardFreeComment = boardFreeService.setFreeCommentFeeling(writer, commentId, feeling);

        Integer numberOfLike = Objects.nonNull(boardFreeComment.getUsersLiking()) ? boardFreeComment.getUsersLiking().size() : 0;
        Integer numberOfDisLike = Objects.nonNull(boardFreeComment.getUsersDisliking()) ? boardFreeComment.getUsersDisliking().size() : 0;

        return UserFeelingResponse.builder()
                .feeling(feeling)
                .numberOfLike(numberOfLike)
                .numberOfDislike(numberOfDisLike)
                .build();
    }

    @ApiOperation(value = "자유게시판 글의 공지 활성화")
    @RequestMapping(value = "/{seq}/notice", method = RequestMethod.POST)
    public EmptyJsonResponse enableFreeNotice(@PathVariable int seq) {

        if (! UserUtils.isAdmin())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

        CommonPrincipal principal = UserUtils.getCommonPrincipal();
        CommonWriter writer = new CommonWriter(principal.getId(), principal.getUsername(), principal.getProviderId());

		boardFreeService.setFreeNotice(writer, seq, true);

        return EmptyJsonResponse.newInstance();
    }

    @ApiOperation(value = "자유게시판 글의 공지 비활성화")
    @RequestMapping(value = "/{seq}/notice", method = RequestMethod.DELETE)
    public EmptyJsonResponse disableFreeNotice(@PathVariable int seq) {

        if (! UserUtils.isAdmin())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

        CommonPrincipal principal = UserUtils.getCommonPrincipal();
        CommonWriter writer = new CommonWriter(principal.getId(), principal.getUsername(), principal.getProviderId());

        boardFreeService.setFreeNotice(writer, seq, false);

        return EmptyJsonResponse.newInstance();
    }
}
