package com.jakduk.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.jakduk.model.db.JakduSchedule;
import com.jakduk.model.embedded.LocalName;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by pyohwan on 16. 3. 26.
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
