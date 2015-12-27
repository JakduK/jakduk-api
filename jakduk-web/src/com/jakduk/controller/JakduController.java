package com.jakduk.controller;

import com.jakduk.service.CommonService;
import com.jakduk.service.JakduService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * Created by pyohwan on 15. 12. 23.
 */

@Controller
@RequestMapping("/jakdu")
public class JakduController {

    @Resource
    LocaleResolver localeResolver;

    @Autowired
    private CommonService commonService;

    @Autowired
    private JakduService jakduService;

    @RequestMapping
    public String root() {

        return "redirect:/jakdu/schedule";
    }

    @RequestMapping(value = "/schedule/refresh", method = RequestMethod.GET)
    public String scheduleRefresh() {

        return "redirect:/jakdu/schedule";
    }

    @RequestMapping(value = "/schedule", method = RequestMethod.GET)
    public String schedule(Model model
            , HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);
        jakduService.getSchedule(model, locale);

        return "jakdu/schedule";
    }

    @RequestMapping(value = "/data/schedule", method = RequestMethod.GET)
    public void dataAttendanceClub(Model model,
                                   HttpServletRequest request,
                                   @RequestParam(required = false, defaultValue = "1") int page,
                                   @RequestParam(required = false, defaultValue = "20") int size) {

        Locale locale = localeResolver.resolveLocale(request);
        String language = commonService.getLanguageCode(locale, null);

        jakduService.getDataScheduleList(model, language, page, size);
    }
}
