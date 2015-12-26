package com.jakduk.service;

import com.jakduk.model.db.JakduSchedule;
import com.jakduk.repository.JakduScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by pyohwan on 15. 12. 26.
 */

@Service
public class JakduService {

    @Autowired
    private CommonService commonService;

    @Autowired
    private JakduScheduleRepository jakduScheduleRepository;

    public void getSchedule(Model model, Locale locale) {

        model.addAttribute("dateTimeFormat", commonService.getDateTimeFormat(locale));
    }

    public void getDataScheduleList(Model model, int page, int size) {

        Sort sort = new Sort(Sort.Direction.DESC, Arrays.asList("_id"));
        Pageable pageable = new PageRequest(page - 1, size, sort);

        List<JakduSchedule> jakduSchedules = jakduScheduleRepository.findAll(pageable).getContent();

        model.addAttribute("jakduSchedules", jakduSchedules);
    }
}
