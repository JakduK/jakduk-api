package com.jakduk.restcontroller;

import com.jakduk.model.db.HomeDescription;
import com.jakduk.service.AdminService;
import com.jakduk.service.CommonService;
import com.jakduk.vo.HomeDescriptionRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * Created by pyohwan on 16. 5. 8.
 */

@Slf4j
@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    @Resource
    private LocaleResolver localeResolver;

    @Autowired
    private CommonService commonService;

    @Autowired
    private AdminService adminService;

    // 알림판 목록.
    @RequestMapping(value = "/home/description", method = RequestMethod.GET)
    public Map<String, Object> dataHomeDescription() {

        List<HomeDescription> homeDescriptions = adminService.findHomeDescriptions();

        Map<String, Object> response = new HashMap();

        response.put("homeDescriptions", homeDescriptions);

        return response;
    }

    // 알림판 하나.
    @RequestMapping(value = "/home/description/{id}", method = RequestMethod.GET)
    public Map<String, Object> homeDescriptionWrite(@PathVariable String id,
                                                    HttpServletRequest request) {

        HomeDescription homeDescription = adminService.findHomeDescriptionById(id);

        Locale locale = localeResolver.resolveLocale(request);

        if (Objects.isNull(homeDescription)) {
            throw new IllegalArgumentException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.invalid.parameter"));
        }

        Map<String, Object> response = new HashMap();

        response.put("homeDescription", homeDescription);

        return response;
    }

    // 새 알림판 저장.
    @RequestMapping(value = "/home/description/{id}", method = RequestMethod.POST)
    public Map<String, Object> homeDescriptionWrite(@RequestBody HomeDescriptionRequest homeDescriptionRequest) {

        if (Objects.isNull(homeDescriptionRequest.getDesc()) || homeDescriptionRequest.getDesc().isEmpty() == true)
            throw new IllegalArgumentException("desc는 필수값입니다.");

        if (Objects.isNull(homeDescriptionRequest.getPriority()))
            throw new IllegalArgumentException("priority는 필수값입니다.");

        HomeDescription homeDescription = HomeDescription.builder()
                .desc(homeDescriptionRequest.getDesc())
                .priority(homeDescriptionRequest.getPriority())
                .build();

        adminService.saveHomeDescription(homeDescription);

        Map<String, Object> response = new HashMap();

        response.put("homeDescription", homeDescription);

        return response;
    }
}
