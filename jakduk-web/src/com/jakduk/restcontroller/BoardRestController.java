package com.jakduk.restcontroller;

import com.jakduk.common.CommonConst;
import com.jakduk.model.db.BoardFreeComment;
import com.jakduk.service.BoardFreeService;
import com.jakduk.vo.UserFeelingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by pyohwan on 16. 3. 26.
 */

@RestController
@RequestMapping("/api/board")
public class BoardRestController {

    @Resource
    LocaleResolver localeResolver;

    @Autowired
    BoardFreeService boardFreeService;

    // 댓글 감정 표현
    @RequestMapping(value = "/comment/{commentId}/{feeling}", method = RequestMethod.POST)
    public UserFeelingResponse setFreeCommentFeeling(@PathVariable String commentId,
                                                     @PathVariable CommonConst.FEELING_TYPE feeling,
                                                     HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);

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
