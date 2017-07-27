package com.jakduk.api.model.db;

import com.jakduk.api.model.embedded.JakduScheduleScore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author pyohwan
 * 15. 12. 23 오후 11:07
 */

@Data
@Document
public class JakduSchedule {

    @Id
    private String id;

    @DBRef
    private JakduScheduleGroup group;

    private Date date;

    @DBRef
    private FootballClubOrigin home;

    @DBRef
    private FootballClubOrigin away;

    @DBRef
    private Competition competition;

    private JakduScheduleScore score;

    private boolean timeUp;
}
