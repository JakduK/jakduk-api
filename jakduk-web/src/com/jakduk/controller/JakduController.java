package com.jakduk.controller;

import com.jakduk.model.web.JakduWriteList;
import com.jakduk.service.CommonService;
import com.jakduk.service.JakduService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

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

    private Logger logger = Logger.getLogger(this.getClass());

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
    public void dataSchedule(Model model,
                                   HttpServletRequest request,
                                   @RequestParam(required = false, defaultValue = "1") int page,
                                   @RequestParam(required = false, defaultValue = "20") int size) {

        Locale locale = localeResolver.resolveLocale(request);
        String language = commonService.getLanguageCode(locale, null);

        jakduService.getDataScheduleList(model, language, page, size);
    }

    @RequestMapping(value = "/schedule/{id}", method = RequestMethod.GET)
    public String schedule(@PathVariable String id, Model model) throws IOException {
        jakduService.getView(model, id);

        return "jakdu/ScheduleView";
    }

    @RequestMapping(value = "/data/schedule/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Map dataSchedule(@PathVariable String id) {

        Map<String, Object> result = jakduService.getDataSchedule(id);

        return result;
    }

    @RequestMapping(value = "/write", method = RequestMethod.GET)
    public String write() {

        return "jakdu/write";
    }

    @RequestMapping(value = "/data", method = RequestMethod.GET)
    @ResponseBody
    public Map data(HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);
        Map<String, Object> result = jakduService.getDataWrite(locale);

        return result;
    }

    @RequestMapping(value = "/write", method = RequestMethod.POST)
    public String write(
            @RequestParam(required = false) String content,JakduWriteList jakduWriteList, BindingResult result, SessionStatus sessionStatus) {

        logger.debug("jakduWriteList=" + jakduWriteList);

        if (result.hasErrors()) {
            if (logger.isDebugEnabled()) {
                logger.debug("result=" + result);
            }
            return "jakdu/write";
        }

        //Integer status = boardFreeService.write(request, boardFreeWrite);
        sessionStatus.setComplete();

        return "redirect:/jakdu/schedule";
    }
}
