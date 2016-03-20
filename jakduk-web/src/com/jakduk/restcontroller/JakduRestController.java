package com.jakduk.restcontroller;

import com.jakduk.model.db.Jakdu;
import com.jakduk.model.db.JakduComment;
import com.jakduk.model.web.jakdu.JakduCommentWriteRequest;
import com.jakduk.service.CommonService;
import com.jakduk.service.JakduService;
import com.jakduk.model.web.jakdu.MyJakduRequest;
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

@RestController
@RequestMapping("/jakdu")
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

    // 작두 일정 데이터 하나 가져오기.
   @RequestMapping(value = "/data/schedule/{id}", method = RequestMethod.GET)
    public Map dataSchedule(@PathVariable String id) {

        Map<String, Object> result = jakduService.getDataSchedule(id);

        return result;
    }

    // 작두 타기
    @RequestMapping(value ="/myJakdu", method = RequestMethod.POST)
    public Jakdu myJakduWrite(@RequestBody MyJakduRequest myJakdu, HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);

        if (Objects.isNull(myJakdu)) {
            throw new IllegalArgumentException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.invalid.parameter"));
        }

        Jakdu jakdu = jakduService.setMyJakdu(locale, myJakdu);

        return jakdu;
    }

    // 작두 댓글 달기
    @RequestMapping(value ="/schedule/comment", method = RequestMethod.POST)
    public JakduComment commentWrite(@RequestBody JakduCommentWriteRequest jakduCommentWriteRequest, HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);

        if (Objects.isNull(jakduCommentWriteRequest)) {
            throw new IllegalArgumentException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.invalid.parameter"));
        }

        Device device = DeviceUtils.getCurrentDevice(request);
        jakduCommentWriteRequest.setDevice(commonService.getDeviceInfo(device));

        JakduComment jakduComment = jakduService.setComment(locale, jakduCommentWriteRequest);

        return jakduComment;
    }
}
