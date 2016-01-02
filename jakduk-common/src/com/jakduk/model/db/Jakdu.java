package com.jakduk.model.db;

import com.jakduk.model.embedded.CommonWriter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 * Created by pyohwan on 16. 1. 2.
 */
public class Jakdu {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private String id;

    private CommonWriter writer;

    @DBRef
    private JakduSchedule schedule;

    private int homeScore;

    private int awayScore;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CommonWriter getWriter() {
        return writer;
    }

    public void setWriter(CommonWriter writer) {
        this.writer = writer;
    }

    public JakduSchedule getSchedule() {
        return schedule;
    }

    public void setSchedule(JakduSchedule schedule) {
        this.schedule = schedule;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

    @Override
    public String toString() {
        return "Jakdu{" +
                "id='" + id + '\'' +
                ", writer=" + writer +
                ", schedule=" + schedule +
                ", homeScore=" + homeScore +
                ", awayScore=" + awayScore +
                '}';
    }
}
