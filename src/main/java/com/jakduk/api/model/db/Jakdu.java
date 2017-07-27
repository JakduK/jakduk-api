package com.jakduk.api.model.db;

import com.jakduk.api.model.embedded.CommonWriter;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;


/**
 * @author pyohwan
 * 16. 1. 2 오후 11:07
 */

@Data
public class Jakdu {

    @Id
    private String id;

    private CommonWriter writer;

    @DBRef
    private JakduSchedule schedule;

    private int homeScore;

    private int awayScore;
}
