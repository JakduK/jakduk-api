package com.jakduk.api.restcontroller;

import com.jakduk.api.common.Constants;
import com.jakduk.api.common.annotation.SecuredUser;
import com.jakduk.api.common.util.JakdukUtils;
import com.jakduk.api.common.util.AuthUtils;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.db.*;
import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.model.embedded.LocalName;
import com.jakduk.api.restcontroller.vo.admin.JakduCommentWriteRequest;
import com.jakduk.api.restcontroller.vo.admin.JakduCommentsResponse;
import com.jakduk.api.restcontroller.vo.admin.MyJakduRequest;
import com.jakduk.api.service.FootballService;
import com.jakduk.api.service.JakduService;
import com.jakduk.api.restcontroller.vo.user.AuthUserProfile;
import com.jakduk.api.restcontroller.vo.UserFeelingResponse;
import com.jakduk.api.restcontroller.vo.jakdu.JakduScheduleResponse;
import io.swagger.annotations.Api;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author pyohwan
 * 16. 3. 4 오후 11:27
 */

@Api(tags = "Jakduk", description = "작두 API")
@RestController
@RequestMapping("/api/jakdu")
public class JakduRestController {

    @Resource
    LocaleResolver localeResolver;

    @Autowired
    private JakduService jakduService;

    @Autowired
    private FootballService footballService;

    // 작두 일정 목록
    @RequestMapping(value = "/schedule", method = RequestMethod.GET)
    public JakduScheduleResponse dataSchedule(@RequestParam(required = false, defaultValue = "1") int page,
                                              @RequestParam(required = false, defaultValue = "20") int size) {

        String language = JakdukUtils.getLanguageCode();

        Sort sort = new Sort(Sort.Direction.ASC, Arrays.asList("group", "date"));
        Pageable pageable = new PageRequest(page - 1, size, sort);

        Set<ObjectId> fcIds = new HashSet<>();
        Set<ObjectId> competitionIds = new HashSet<>();
        List<JakduSchedule> jakduSchedules = jakduService.findAll(pageable).getContent();

        for (JakduSchedule jakduSchedule : jakduSchedules) {
            fcIds.add(new ObjectId(jakduSchedule.getHome().getId()));
            fcIds.add(new ObjectId(jakduSchedule.getAway().getId()));

            if (Objects.nonNull(jakduSchedule.getCompetition()))
                competitionIds.add(new ObjectId(jakduSchedule.getCompetition().getId()));
        }

        List<FootballClub> footballClubs = footballService.getFootballClubs(new ArrayList<>(fcIds), language, Constants.NAME_TYPE.fullName);
        List<Competition> competitions = footballService.getCompetitions(new ArrayList<>(competitionIds), language);

        Map<String, LocalName> fcNames = footballClubs.stream()
                .collect(Collectors.toMap(footballClub -> footballClub.getOrigin().getId(), footballClub -> footballClub.getNames().get(0)));

        Map<String, LocalName> competitionNames = competitions.stream()
                .collect(Collectors.toMap(Competition::getId, competition -> competition.getNames().get(0)));

        JakduScheduleResponse response = new JakduScheduleResponse();
        response.setJakduSchedules(jakduSchedules);
        response.setFcNames(fcNames);
        response.setCompetitionNames(competitionNames);

        return response;
    }

    // 작두 일정 데이터 하나 가져오기.
    @RequestMapping(value = "/schedule/{id}", method = RequestMethod.GET)
    public Map<String, Object> dataSchedule(@PathVariable String id,
                                            HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);

        Map<String, Object> result = new HashMap<>();

        JakduSchedule jakduSchedule = jakduService.findScheduleById(id);
        result.put("jakduSchedule", jakduSchedule);

        AuthUserProfile authUserProfile = AuthUtils.getAuthUserProfile();

        if (AuthUtils.isUser()) {
            result.put("myJakdu", jakduService.getMyJakdu(authUserProfile.getId(), id));
        }

        return result;
    }

    // 작두 타기
    @SecuredUser
    @RequestMapping(value ="/myJakdu", method = RequestMethod.POST)
    public Jakdu myJakduWrite(@RequestBody MyJakduRequest myJakdu, HttpServletRequest request) {

        if (Objects.isNull(myJakdu)) {
            throw new IllegalArgumentException(JakdukUtils.getMessageSource("exception.invalid.parameter"));
        }

        CommonWriter commonWriter = AuthUtils.getCommonWriter();

        return jakduService.setMyJakdu(commonWriter, myJakdu);
    }

    // 작두 댓글 달기
    @RequestMapping(value ="/schedule/comment", method = RequestMethod.POST)
    public JakduComment commentWrite(@RequestBody JakduCommentWriteRequest jakduCommentWriteRequest,
                                     Device device) {

        if (Objects.isNull(jakduCommentWriteRequest))
            throw new IllegalArgumentException(JakdukUtils.getMessageSource("exception.invalid.parameter"));

        if (! AuthUtils.isUser())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

        jakduCommentWriteRequest.setDevice(JakdukUtils.getDeviceInfo(device));

        CommonWriter commonWriter = AuthUtils.getCommonWriter();

        JakduComment jakduComment = jakduService.setComment(commonWriter, jakduCommentWriteRequest);

        return jakduComment;
    }

    // 작두 댓글 목록
    @RequestMapping(value = "/schedule/comments/{jakduScheduleId}", method = RequestMethod.GET)
    public JakduCommentsResponse getComments(@PathVariable String jakduScheduleId,
                                             @RequestParam(required = false) String commentId) {

        JakduCommentsResponse response = jakduService.getComments(jakduScheduleId, commentId);

        return response;
    }

    // 작두 댓글 좋아요 싫어요
    @RequestMapping(value = "/schedule/comment/{commentId}/{feeling}", method = RequestMethod.POST)
    public UserFeelingResponse setCommentFeeling(@PathVariable String commentId,
                                                 @PathVariable Constants.FEELING_TYPE feeling) {

        if (! AuthUtils.isUser())
            throw new ServiceException(ServiceError.UNAUTHORIZED_ACCESS);

        CommonWriter commonWriter = AuthUtils.getCommonWriter();

        JakduComment jakduComment = jakduService.setJakduCommentFeeling(commonWriter, commentId, feeling);

        Integer numberOfLike = Objects.nonNull(jakduComment.getUsersLiking()) ? jakduComment.getUsersLiking().size() : 0;
        Integer numberOfDisLike = Objects.nonNull(jakduComment.getUsersDisliking()) ? jakduComment.getUsersDisliking().size() : 0;

        return UserFeelingResponse.builder()
                .myFeeling(feeling)
                .numberOfLike(numberOfLike)
                .numberOfDislike(numberOfDisLike)
                .build();
    }
}
