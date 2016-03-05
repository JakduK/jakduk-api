package com.jakduk.restcontroller;

import com.jakduk.model.db.Jakdu;
import com.jakduk.service.JakduService;
import com.jakduk.model.web.MyJakduRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Slf4j
public class JakduRestController {

    @Resource
    LocaleResolver localeResolver;

    @Autowired
    private JakduService jakduService;

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
    public Jakdu goJakdu(@RequestBody MyJakduRequest myJakdu, HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);

        if (Objects.isNull(myJakdu)) {
            ResourceBundle bundle = ResourceBundle.getBundle("messages.common", locale);
            throw new IllegalArgumentException(bundle.getString("common.msg.invalid.parameter.exception"));
        }

        Jakdu jakdu = jakduService.setMyJakdu(locale, myJakdu);

        return jakdu;
    }
}
