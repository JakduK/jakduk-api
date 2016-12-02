package com.jakduk.api.restcontroller.jakdu;

import com.jakduk.api.common.util.ApiUtils;
import com.jakduk.api.common.util.UserUtils;
import com.jakduk.api.configuration.authentication.user.CommonPrincipal;
import com.jakduk.api.restcontroller.vo.JakduScheduleResponse;
import com.jakduk.api.restcontroller.vo.UserFeelingResponse;
import com.jakduk.core.common.CoreConst;
import com.jakduk.core.common.util.CoreUtils;
import com.jakduk.core.exception.ServiceExceptionCode;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.exception.UnauthorizedAccessException;
import com.jakduk.core.model.db.*;
import com.jakduk.core.model.embedded.CommonWriter;
import com.jakduk.core.model.embedded.LocalName;
import com.jakduk.core.model.web.jakdu.JakduCommentWriteRequest;
import com.jakduk.core.model.web.jakdu.JakduCommentsResponse;
import com.jakduk.core.model.web.jakdu.MyJakduRequest;
import com.jakduk.core.service.FootballService;
import com.jakduk.core.service.JakduService;
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
                                              @RequestParam(required = false, defaultValue = "20") int size,
                                              HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);
        String language = CoreUtils.getLanguageCode(locale, null);

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

        List<FootballClub> footballClubs = footballService.getFootballClubs(new ArrayList<>(fcIds), language, CoreConst.NAME_TYPE.fullName);
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

        CommonPrincipal principal = UserUtils.getCommonPrincipal();

        if (UserUtils.isUser()) {
            result.put("myJakdu", jakduService.getMyJakdu(principal.getId(), id));
        }

        return result;
    }

    // 작두 타기
    @RequestMapping(value ="/myJakdu", method = RequestMethod.POST)
    public Jakdu myJakduWrite(@RequestBody MyJakduRequest myJakdu, HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);

        if (!UserUtils.isUser())
            throw new UnauthorizedAccessException(CoreUtils.getExceptionMessage("exception.access.denied"));

        if (Objects.isNull(myJakdu)) {
            throw new IllegalArgumentException(CoreUtils.getExceptionMessage("exception.invalid.parameter"));
        }

        CommonPrincipal principal = UserUtils.getCommonPrincipal();
        CommonWriter writer = new CommonWriter(principal.getId(), principal.getUsername(), principal.getProviderId());

        Jakdu jakdu = jakduService.setMyJakdu(writer, myJakdu);

        return jakdu;
    }

    // 작두 댓글 달기
    @RequestMapping(value ="/schedule/comment", method = RequestMethod.POST)
    public JakduComment commentWrite(@RequestBody JakduCommentWriteRequest jakduCommentWriteRequest,
                                     Device device) {

        if (Objects.isNull(jakduCommentWriteRequest))
            throw new IllegalArgumentException(CoreUtils.getExceptionMessage("exception.invalid.parameter"));

        if (! UserUtils.isUser())
            throw new ServiceException(ServiceExceptionCode.UNAUTHORIZED_ACCESS);

        jakduCommentWriteRequest.setDevice(ApiUtils.getDeviceInfo(device));

        CommonPrincipal principal = UserUtils.getCommonPrincipal();
        CommonWriter writer = new CommonWriter(principal.getId(), principal.getUsername(), principal.getProviderId());

        JakduComment jakduComment = jakduService.setComment(writer, jakduCommentWriteRequest);

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
                                                 @PathVariable CoreConst.FEELING_TYPE feeling) {

        if (! UserUtils.isUser())
            throw new ServiceException(ServiceExceptionCode.UNAUTHORIZED_ACCESS);

        CommonPrincipal principal = UserUtils.getCommonPrincipal();
        CommonWriter writer = new CommonWriter(principal.getId(), principal.getUsername(), principal.getProviderId());

        JakduComment jakduComment = jakduService.setJakduCommentFeeling(writer, commentId, feeling);

        Integer numberOfLike = Objects.nonNull(jakduComment.getUsersLiking()) ? jakduComment.getUsersLiking().size() : 0;
        Integer numberOfDisLike = Objects.nonNull(jakduComment.getUsersDisliking()) ? jakduComment.getUsersDisliking().size() : 0;

        return UserFeelingResponse.builder()
                .feeling(feeling)
                .numberOfLike(numberOfLike)
                .numberOfDislike(numberOfDisLike)
                .build();
    }
}
