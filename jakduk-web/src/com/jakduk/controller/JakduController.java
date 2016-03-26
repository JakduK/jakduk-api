package com.jakduk.controller;

import com.jakduk.model.web.JakduWriteList;
import com.jakduk.service.CommonService;
import com.jakduk.service.JakduService;
import lombok.extern.slf4j.Slf4j;
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

/**
 * Created by pyohwan on 15. 12. 23.
 */

@Controller
@RequestMapping("/jakdu")
@Slf4j
public class JakduController {

    @Resource
    LocaleResolver localeResolver;

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

    @RequestMapping(value = "/schedule/{id}", method = RequestMethod.GET)
    public String schedule(@PathVariable String id,
                           Model model,
                           HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);
        jakduService.getView(locale, model, id);

        return "jakdu/scheduleView";
    }

    @RequestMapping(value = "/write", method = RequestMethod.GET)
    public String write() {

        return "jakdu/write";
    }

    @RequestMapping(value = "/write", method = RequestMethod.POST)
    public String write(
            @RequestParam(required = false) String content,JakduWriteList jakduWriteList, BindingResult result, SessionStatus sessionStatus) {

        if (result.hasErrors()) {
            if (log.isDebugEnabled()) {
                log.debug("result=" + result);
            }
            return "jakdu/write";
        }

        //Integer status = boardFreeService.write(request, boardFreeWrite);
        sessionStatus.setComplete();

        return "redirect:/jakdu/schedule";
    }


}
