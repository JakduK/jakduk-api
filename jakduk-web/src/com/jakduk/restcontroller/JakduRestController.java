package com.jakduk.restcontroller;

import com.jakduk.common.CommonConst;
import com.jakduk.exception.UnauthorizedAccessException;
import com.jakduk.model.db.Jakdu;
import com.jakduk.model.db.JakduComment;
import com.jakduk.vo.UserFeelingResponse;
import com.jakduk.model.web.jakdu.JakduCommentWriteRequest;
import com.jakduk.model.web.jakdu.JakduCommentsResponse;
import com.jakduk.model.web.jakdu.JakduScheduleResponse;
import com.jakduk.service.CommonService;
import com.jakduk.service.JakduService;
import com.jakduk.model.web.jakdu.MyJakduRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by pyohwan on 16. 3. 4.
 */

@Slf4j
@RestController
@RequestMapping("/api/jakdu")
public class JakduRestController {

    @Resource
    LocaleResolver localeResolver;

    @Autowired
    private JakduService jakduService;

    @Autowired
    private CommonService commonService;

    // /jakdu/write에서 쓰이는데 안쓸것 같다.
    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public Map data(HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);
        Map<String, Object> result = jakduService.getDataWrite(locale);

        return result;
    }

    // 작두 일정 목록
    @RequestMapping(value = "/schedule", method = RequestMethod.GET)
    public JakduScheduleResponse dataSchedule(HttpServletRequest request,
                             @RequestParam(required = false, defaultValue = "1") int page,
                             @RequestParam(required = false, defaultValue = "20") int size) {

        Locale locale = localeResolver.resolveLocale(request);
        String language = commonService.getLanguageCode(locale, null);

        JakduScheduleResponse response = jakduService.getSchedules(language, page, size);

        return response;
    }

    // 작두 일정 데이터 하나 가져오기.
    @RequestMapping(value = "/schedule/{id}", method = RequestMethod.GET)
    public Map dataSchedule(@PathVariable String id) {

        Map<String, Object> result = jakduService.getDataSchedule(id);

        return result;
    }

    // 작두 타기
    @RequestMapping(value ="/myJakdu", method = RequestMethod.POST)
    public Jakdu myJakduWrite(@RequestBody MyJakduRequest myJakdu, HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);

        if (commonService.isUser() == false)
            throw new UnauthorizedAccessException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.access.denied"));

        if (Objects.isNull(myJakdu)) {
            throw new IllegalArgumentException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.invalid.parameter"));
        }

        Jakdu jakdu = jakduService.setMyJakdu(locale, myJakdu);

        return jakdu;
    }

    // 작두 댓글 달기
    @RequestMapping(value ="/schedule/comment", method = RequestMethod.POST)
    public JakduComment commentWrite(@RequestBody JakduCommentWriteRequest jakduCommentWriteRequest,
                                     HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);

        if (Objects.isNull(jakduCommentWriteRequest)) {
            throw new IllegalArgumentException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.invalid.parameter"));
        }

        Device device = DeviceUtils.getCurrentDevice(request);
        jakduCommentWriteRequest.setDevice(commonService.getDeviceInfo(device));

        JakduComment jakduComment = jakduService.setComment(locale, jakduCommentWriteRequest);

        return jakduComment;
    }

    // 작두 댓글 목록
    @RequestMapping(value = "/schedule/comments/{jakduScheduleId}", method = RequestMethod.GET)
    public JakduCommentsResponse getComments(@PathVariable String jakduScheduleId,
                                             @RequestParam(required = false) String commentId) {

        JakduCommentsResponse response = jakduService.getComments(jakduScheduleId, commentId);

        return response;
    }

    // 작두 댓글 좋아요 싫어요
    @RequestMapping(value = "/schedule/comment/{commentId}/{feeling}", method = RequestMethod.POST)
    public UserFeelingResponse setCommentFeeling(@PathVariable String commentId,
                                                 @PathVariable CommonConst.FEELING_TYPE feeling,
                                                 HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);

        JakduComment jakduComment = jakduService.setJakduCommentFeeling(locale, commentId, feeling);

        Integer numberOfLike = Objects.nonNull(jakduComment.getUsersLiking()) ? jakduComment.getUsersLiking().size() : 0;
        Integer numberOfDisLike = Objects.nonNull(jakduComment.getUsersDisliking()) ? jakduComment.getUsersDisliking().size() : 0;

        UserFeelingResponse response = new UserFeelingResponse();
        response.setFeeling(feeling);
        response.setNumberOfLike(numberOfLike);
        response.setNumberOfDislike(numberOfDisLike);

        return response;
    }
}
