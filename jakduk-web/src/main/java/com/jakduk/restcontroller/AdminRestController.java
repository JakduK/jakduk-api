package com.jakduk.restcontroller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jakduk.common.CommonConst;
import com.jakduk.model.db.Encyclopedia;
import com.jakduk.model.db.FootballClub;
import com.jakduk.model.db.FootballClubOrigin;
import com.jakduk.model.db.HomeDescription;
import com.jakduk.model.embedded.LocalName;
import com.jakduk.restcontroller.vo.FootballClubRequest;
import com.jakduk.restcontroller.vo.HomeDescriptionRequest;
import com.jakduk.service.AdminService;
import com.jakduk.service.CommonService;


/**
 * Created by pyohwan on 16. 5. 8.
 */

@Slf4j
@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    @Autowired
    private CommonService commonService;

    @Autowired
    private AdminService adminService;

    // 알림판 목록.
    @RequestMapping(value = "/home/descriptions", method = RequestMethod.GET)
    public Map<String, Object> getHomeDescriptions() {

        List<HomeDescription> homeDescriptions = adminService.findHomeDescriptions();

        Map<String, Object> response = new HashMap<>();

        response.put("homeDescriptions", homeDescriptions);

        return response;
    }

    // 알림판 하나.
    @RequestMapping(value = "/home/description/{id}", method = RequestMethod.GET)
    public Map<String, Object> getHomeDescription(@PathVariable String id) {

        HomeDescription homeDescription = adminService.findHomeDescriptionById(id);

        if (Objects.isNull(homeDescription)) {
            throw new IllegalArgumentException("id가 " + id + "에 해당하는 알림판이 존재하지 않습니다.");
        }

        Map<String, Object> response = new HashMap<>();

        response.put("homeDescription", homeDescription);

        return response;
    }

    // 새 알림판 저장.
    @RequestMapping(value = "/home/description", method = RequestMethod.POST)
    public Map<String, Object> addHomeDescription(@RequestBody HomeDescriptionRequest homeDescriptionRequest) {

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

    // 알림판 편집.
    @RequestMapping(value = "/home/description/{id}", method = RequestMethod.PUT)
    public Map<String, Object> editHomeDescription(@PathVariable String id,
                                                   @RequestBody HomeDescriptionRequest homeDescriptionRequest) {

        if (Objects.isNull(homeDescriptionRequest.getDesc()) || homeDescriptionRequest.getDesc().isEmpty() == true)
            throw new IllegalArgumentException("desc는 필수값입니다.");

        if (Objects.isNull(homeDescriptionRequest.getPriority()))
            throw new IllegalArgumentException("priority는 필수값입니다.");

        HomeDescription existHomeDescription = adminService.findHomeDescriptionById(id);

        if (Objects.isNull(existHomeDescription))
            throw new IllegalArgumentException("id가 " + id + "에 해당하는 알림판이 존재하지 않습니다.");

        HomeDescription homeDescription = HomeDescription.builder()
                .id(id)
                .desc(homeDescriptionRequest.getDesc())
                .priority(homeDescriptionRequest.getPriority())
                .build();

        adminService.saveHomeDescription(homeDescription);

        Map<String, Object> response = new HashMap<>();

        response.put("homeDescription", homeDescription);

        return response;
    }

    // 알림판 지움.
    @RequestMapping(value = "/home/description/{id}", method = RequestMethod.DELETE)
    public Map<String, Object> deleteHomeDescription(@PathVariable String id) {

        HomeDescription existHomeDescription = adminService.findHomeDescriptionById(id);

        if (Objects.isNull(existHomeDescription))
            throw new IllegalArgumentException("id가 " + id + "에 해당하는 알림판이 존재하지 않습니다.");

        adminService.deleteHomeDescriptionById(id);

        Map<String, Object> response = new HashMap<>();
        response.put("result", true);

        return response;
    }

    // 백과사전 목록.
    @RequestMapping(value = "/encyclopedias", method = RequestMethod.GET)
    public Map<String, Object> getEncyclopedias() {

        List<Encyclopedia> encyclopedias = adminService.findEncyclopedias();

        Map<String, Object> response = new HashMap<>();

        response.put("encyclopedias", encyclopedias);

        return response;
    }

    // 백과사전 하나.
    @RequestMapping(value = "/encyclopedia/{id}", method = RequestMethod.GET)
    public Map<String, Object> getEncyclopedia(@PathVariable String id) {

        Encyclopedia encyclopedia = adminService.findEncyclopediaById(id);

        if (Objects.isNull(encyclopedia))
            throw new IllegalArgumentException("id가 " + id + "에 해당하는 백과사전이 존재하지 않습니다.");

        Map<String, Object> response = new HashMap<>();
        response.put("encyclopedia", encyclopedia);
        return response;
    }

    // 새 백과사전 저장.
    @RequestMapping(value = "/encyclopedia", method = RequestMethod.POST)
    public Map<String, Object> addEncyclopedia(@RequestBody Encyclopedia encyclopedia) {

        // 신규로 만들기때문에 null로 설정.
        encyclopedia.setId(null);

        if (encyclopedia.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
            encyclopedia.setSeq(commonService.getNextSequence(CommonConst.ENCYCLOPEDIA_EN));
        } else if (encyclopedia.getLanguage().equals(Locale.KOREAN.getLanguage())) {
            encyclopedia.setSeq(commonService.getNextSequence(CommonConst.ENCYCLOPEDIA_KO));
        }

        adminService.saveEncyclopedia(encyclopedia);

        Map<String, Object> response = new HashMap<>();
        response.put("encyclopedia", encyclopedia);

        return response;
    }

    // 백과사전 편집.
    @RequestMapping(value = "/encyclopedia/{id}", method = RequestMethod.PUT)
    public Map<String, Object> editEncyclopedia(@PathVariable String id,
                                                @RequestBody Encyclopedia encyclopedia) {

        Encyclopedia existEncyclopedia = adminService.findEncyclopediaById(id);

        if (Objects.isNull(existEncyclopedia))
            throw new IllegalArgumentException("id가 " + id + "에 해당하는 백과사전이 존재하지 않습니다.");

        encyclopedia.setId(id);
        adminService.saveEncyclopedia(encyclopedia);

        Map<String, Object> response = new HashMap<>();
        response.put("encyclopedia", encyclopedia);

        return response;
    }

    // 백과사전 지움.
    @RequestMapping(value = "/encyclopedia/{id}", method = RequestMethod.DELETE)
    public Map<String, Object> deleteEncyclopedia(@PathVariable String id) {

        Encyclopedia encyclopedia = adminService.findEncyclopediaById(id);

        if (Objects.isNull(encyclopedia))
            throw new IllegalArgumentException("id가 " + id + "에 해당하는 백과사전이 존재하지 않습니다.");

        adminService.deleteEncyclopediaById(id);

        Map<String, Object> response = new HashMap<>();
        response.put("result", true);

        return response;
    }

    // 부모 축구단 목록.
    @RequestMapping(value = "/origin/football/clubs", method = RequestMethod.GET)
    public Map<String, Object> getOriginFootballClubs() {

        List<FootballClubOrigin> fcOrigins = adminService.findOriginFootballClubs();

        Map<String, Object> response = new HashMap<>();
        response.put("originFCs", fcOrigins);

        return response;
    }

    // 부모 축구단 하나.
    @RequestMapping(value = "/origin/football/club/{id}", method = RequestMethod.GET)
    public Map<String, Object> getOriginFootballClub(@PathVariable String id) {

        FootballClubOrigin footballClubOrigin = adminService.findOriginFootballClubById(id);

        if (Objects.isNull(footballClubOrigin))
            throw new IllegalArgumentException("id가 " + id + "에 해당하는 부모 축구단이 존재하지 않습니다.");

        Map<String, Object> response = new HashMap<>();
        response.put("originFC", footballClubOrigin);

        return response;
    }

    // 새 부모 축구단 하나 저장.
    @RequestMapping(value = "/origin/football/club", method = RequestMethod.POST)
    public Map<String, Object> addOriginFootballClub(@RequestBody FootballClubOrigin footballClubOrigin) {

        // 신규로 만들기때문에 null로 설정.
        footballClubOrigin.setId(null);

        adminService.saveOriginFootballClub(footballClubOrigin);

        Map<String, Object> response = new HashMap<>();
        response.put("originFC", footballClubOrigin);

        return response;
    }

    // 부모 축구단 하나 편집.
    @RequestMapping(value = "/origin/football/club/{id}", method = RequestMethod.PUT)
    public Map<String, Object> editOriginFootballClub(@PathVariable String id,
                                                      @RequestBody FootballClubOrigin footballClubOrigin) {

        FootballClubOrigin existFootballClubOrigin = adminService.findOriginFootballClubById(id);

        if (Objects.isNull(existFootballClubOrigin))
            throw new IllegalArgumentException("id가 " + id + "에 해당하는 부모 축구단이 존재하지 않습니다.");

        footballClubOrigin.setId(id);
        adminService.saveOriginFootballClub(footballClubOrigin);

        Map<String, Object> response = new HashMap<>();
        response.put("originFC", footballClubOrigin);

        return response;
    }

    // 부모 축구단 하나 지움.
    @RequestMapping(value = "/origin/football/club/{id}", method = RequestMethod.DELETE)
    public Map<String, Object> deleteOriginFootballClub(@PathVariable String id) {

        FootballClubOrigin existFootballClubOrigin = adminService.findOriginFootballClubById(id);

        if (Objects.isNull(existFootballClubOrigin))
            throw new IllegalArgumentException("id가 " + id + "에 해당하는 부모 축구단이 존재하지 않습니다.");

        adminService.deleteOriginFootballClub(id);

        Map<String, Object> response = new HashMap<>();
        response.put("result", true);

        return response;
    }

    // 축구단 목록.
    @RequestMapping(value = "/football/clubs", method = RequestMethod.GET)
    public Map<String, Object> getFootballClubs() {

        List<FootballClub> footballClubs = adminService.findFootballClubs();

        Map<String, Object> response = new HashMap<>();
        response.put("fcs", footballClubs);

        return response;
    }

    // 축구단 하나.
    @RequestMapping(value = "/football/club/{id}", method = RequestMethod.GET)
    public Map<String, Object> getFootballClub(@PathVariable String id) {

        FootballClub fc = adminService.findFootballClubById(id);

        if (Objects.isNull(fc))
            throw new IllegalArgumentException("id가 " + id + "에 해당하는 축구단이 존재하지 않습니다.");

        List<FootballClubOrigin> originFCs = adminService.findOriginFootballClubs();

        FootballClubRequest fcRequest = new FootballClubRequest();
        fcRequest.setId(fc.getId());
        fcRequest.setActive(fc.getActive());
        fcRequest.setOrigin(fc.getOrigin().getId());

        for (LocalName fcName : fc.getNames()) {
            if (fcName.getLanguage().equals(Locale.KOREAN.getLanguage())) {
                fcRequest.setFullNameKr(fcName.getFullName());
                fcRequest.setShortNameKr(fcName.getShortName());
            } else if (fcName.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
                fcRequest.setFullNameEn(fcName.getFullName());
                fcRequest.setShortNameEn(fcName.getShortName());
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("originFCs", originFCs);
        response.put("fcRequest", fcRequest);

        return response;
    }

    // 새 축구단 하나 저장.
    @RequestMapping(value = "/football/club", method = RequestMethod.POST)
    public Map<String, Object> addFootballClub(@RequestBody FootballClubRequest request) {

        if (Objects.isNull(request.getOrigin()) || request.getOrigin().isEmpty())
            throw new IllegalArgumentException("origin은 필수값입니다.");

        FootballClub footballClub = buildFootballClub(null, request);
        adminService.saveFootballClub(footballClub);

        Map<String, Object> response = new HashMap<>();
        response.put("fc", footballClub);

        return response;
    }

    @RequestMapping(value = "/football/club/{id}", method = RequestMethod.PUT)
    public Map<String, Object> editFootballClub(@PathVariable String id, @RequestBody FootballClubRequest request) {
        FootballClub footballClub = buildFootballClub(id, request);
        adminService.saveFootballClub(footballClub);

        Map<String, Object> response = new HashMap<>();
        response.put("fc", footballClub);

        return response;
    }

    @RequestMapping(value = "/board/category/init", method = RequestMethod.POST)
    public Map<String, Object> initBoardCategory() {
        return adminService.initBoardCategory();
    }

    @RequestMapping(value = "/search/index/init", method = RequestMethod.POST)
    public Map<String, Object> initSearchIndex() {
        return adminService.initSearchIndex();
    }

    @RequestMapping(value = "/search/type/init", method = RequestMethod.POST)
    public Map<String, Object> initSearchType() {
        return adminService.initSearchType();
    }

    @RequestMapping(value = "/search/data/init", method = RequestMethod.POST)
    public Map<String, Object> initSearchData() {
        return adminService.initSearchData();
    }

    private FootballClub buildFootballClub(String id, FootballClubRequest request) {
        FootballClubOrigin footballClubOrigin = adminService.findOriginFootballClubById(request.getOrigin());

        if (Objects.isNull(footballClubOrigin))
            throw new IllegalArgumentException("id가 " + request.getOrigin() + "에 해당하는 부모 축구단이 존재하지 않습니다.");

        LocalName footballClubNameKr = LocalName.builder()
                                         .language(Locale.KOREAN.getLanguage())
                                         .fullName(request.getFullNameKr())
                                         .shortName(request.getShortNameKr())
                                         .build();

        LocalName footballClubNameEn = LocalName.builder()
                                         .language(Locale.ENGLISH.getLanguage())
                                         .fullName(request.getFullNameEn())
                                         .shortName(request.getShortNameEn())
                                         .build();

        ArrayList<LocalName> names = new ArrayList<>();
        names.add(footballClubNameKr);
        names.add(footballClubNameEn);

        return FootballClub.builder()
                 .id(id)
                 .active(request.getActive())
                 .origin(footballClubOrigin)
                 .names(names)
                 .build();
    }
}
