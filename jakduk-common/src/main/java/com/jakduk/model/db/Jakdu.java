package com.jakduk.model.db;

import com.jakduk.model.embedded.CommonWriter;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 * Created by pyohwan on 16. 1. 2.
 */

@Data
public class Jakdu {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private String id;

    private CommonWriter writer;

    @DBRef
    private JakduSchedule schedule;

    private int homeScore;

    private int awayScore;
}
