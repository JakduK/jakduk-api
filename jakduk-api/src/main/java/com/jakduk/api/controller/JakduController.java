package com.jakduk.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakduk.core.model.web.JakduWriteList;
import com.jakduk.core.service.CommonService;
import com.jakduk.core.service.JakduService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * @author pyohwan
 * 15. 12. 23 오후 11:24
 */

@Slf4j
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

    // 작두 일정 페이지.
    @RequestMapping(value = "/schedule", method = RequestMethod.GET)
    public String schedule(HttpServletRequest request,
                           Model model) {

        Locale locale = localeResolver.resolveLocale(request);

        model.addAttribute("dateTimeFormat", commonService.getDateTimeFormat(locale));

        return "jakdu/schedule";
    }

    // 작두 페이지.
    @RequestMapping(value = "/schedule/{id}", method = RequestMethod.GET)
    public String schedule(@PathVariable String id,
                           HttpServletRequest request,
                           Model model) {

        Locale locale = localeResolver.resolveLocale(request);

        model.addAttribute("id", id);

        try {
            model.addAttribute("dateTimeFormat", new ObjectMapper().writeValueAsString(commonService.getDateTimeFormat(locale)));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.parsing.or.generating"));
        }

        return "jakdu/scheduleView";
    }

    @RequestMapping(value = "/write", method = RequestMethod.GET)
    public String write() {

        return "jakdu/write";
    }

    @RequestMapping(value = "/write", method = RequestMethod.POST)
    public String write(
            @RequestParam(required = false) String content, JakduWriteList jakduWriteList, BindingResult result, SessionStatus sessionStatus) {

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
