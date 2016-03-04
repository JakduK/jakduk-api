package com.jakduk.controller.rest;

import com.jakduk.service.JakduService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

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
    @RequestMapping(value ="/go/jakdu", method = RequestMethod.POST)
    public Map goJakdu(@RequestBody Map<Object,Object> map) {

        Map result = new HashMap<>();

        if (Objects.isNull(map)) {
            result.put("result", Boolean.FALSE);
            return result;
        }



        log.debug("map" + map);

        Map<String, String> myJakdu = (Map<String, String>) map.get("myJakdu");
        String jakduScheduleId = (String) map.get("jakduScheduleId");

        if (Objects.isNull(myJakdu) || Objects.isNull(jakduScheduleId)) {
            result.put("result", Boolean.FALSE);
            return result;
        }

        boolean returnVal = jakduService.setMyJakdu(jakduScheduleId, myJakdu);

        if (!returnVal) {
            result.put("result", Boolean.FALSE);
            return result;
        }

        result.put("result", Boolean.TRUE);

        return result;
    }
}
