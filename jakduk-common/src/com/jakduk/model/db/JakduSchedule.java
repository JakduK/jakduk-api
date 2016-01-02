package com.jakduk.model.db;

import com.jakduk.model.embedded.JakduScheduleScore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by pyohwan on 15. 12. 23.
 */

@Document
public class JakduSchedule {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private String id;

    @Temporal(TemporalType.DATE)
    private Date date;

    @DBRef
    private FootballClubOrigin home;

    @DBRef
    private FootballClubOrigin away;

    @DBRef
    private Competition competition;

    private JakduScheduleScore score;

    private boolean timeUp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public FootballClubOrigin getHome() {
        return home;
    }

    public void setHome(FootballClubOrigin home) {
        this.home = home;
    }

    public FootballClubOrigin getAway() {
        return away;
    }

    public void setAway(FootballClubOrigin away) {
        this.away = away;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public JakduScheduleScore getScore() {
        return score;
    }

    public void setScore(JakduScheduleScore score) {
        this.score = score;
    }

    public boolean isTimeUp() {
        return timeUp;
    }

    public void setTimeUp(boolean timeUp) {
        this.timeUp = timeUp;
    }

    @Override
    public String toString() {
        return "JakduSchedule{" +
                "id='" + id + '\'' +
                ", date=" + date +
                ", home=" + home +
                ", away=" + away +
                ", competition=" + competition +
                ", score=" + score +
                ", timeUp=" + timeUp +
                '}';
    }
}
