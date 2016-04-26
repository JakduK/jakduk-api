package com.jakduk.restcontroller;

import com.jakduk.common.CommonConst;
import com.jakduk.model.web.user.UserFeelingResponse;
import com.jakduk.service.BoardFreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

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

    @RequestMapping(value = "/comment/{commentId}/{feeling}", method = RequestMethod.POST)
    public UserFeelingResponse setFreeCommentFeeling(@PathVariable String commentId,
                                                     @PathVariable CommonConst.FEELING_TYPE feeling,
                                                     HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);

        UserFeelingResponse response = boardFreeService.setFreeCommentFeeling(locale, commentId, feeling);

        return response;
    }
}
