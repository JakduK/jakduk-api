package com.jakduk.restcontroller;

import com.jakduk.model.db.HomeDescription;
import com.jakduk.service.AdminService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pyohwan on 16. 5. 8.
 */

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    @Autowired
    private AdminService adminService;

    // 알림판 목록.
    @RequestMapping(value = "/home/description", method = RequestMethod.GET)
    public Map<String, Object> dataHomeDescription() {

        List<HomeDescription> homeDescriptions = adminService.getHomeDescriptions();

        Map<String, Object> response = new HashMap();

        response.put("homeDescriptions", homeDescriptions);

        return response;
    }
}
