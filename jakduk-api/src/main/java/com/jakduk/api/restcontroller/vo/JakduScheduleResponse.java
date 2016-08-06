package com.jakduk.api.restcontroller.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.jakduk.core.model.embedded.LocalName;
import com.jakduk.core.model.db.JakduSchedule;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author pyohwan
 * 16. 3. 26 오후 11:28
 */

@Data
@JsonTypeName(value = "response")
@JsonTypeInfo(use=JsonTypeInfo.Id.NONE, include= JsonTypeInfo.As.WRAPPER_OBJECT)
public class JakduScheduleResponse {
    private Map<String, LocalName> fcNames;
    private Map<String, LocalName> competitionNames;

    @JsonProperty(value = "schedules")
    private List<JakduSchedule> jakduSchedules;
}
