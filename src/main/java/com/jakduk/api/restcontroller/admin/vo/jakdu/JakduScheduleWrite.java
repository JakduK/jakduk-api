package com.jakduk.api.restcontroller.admin.vo.jakdu;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * Created by pyohwan on 15. 12. 24.
 */
public class JakduScheduleWrite {

    @Id
    private String id;

    private Date date;

    @NotEmpty
    private String home;

    @NotEmpty
    private String away;

    private Integer homeFullTime;

    private Integer awayFullTime;

    private Integer homeOverTime;

    private Integer awayOverTime;

    private Integer homePenaltyShootout;

    private Integer awayPenaltyShootout;

    @NotEmpty
    private String competition;

    private int groupSeq;

    private boolean timeUp;

    public Integer getAwayOverTime() {
        return awayOverTime;
    }

    public void setAwayOverTime(Integer awayOverTime) {
        this.awayOverTime = awayOverTime;
    }

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

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getAway() {
        return away;
    }

    public void setAway(String away) {
        this.away = away;
    }

    public Integer getHomeFullTime() {
        return homeFullTime;
    }

    public void setHomeFullTime(Integer homeFullTime) {
        this.homeFullTime = homeFullTime;
    }

    public Integer getAwayFullTime() {
        return awayFullTime;
    }

    public void setAwayFullTime(Integer awayFullTime) {
        this.awayFullTime = awayFullTime;
    }

    public Integer getHomeOverTime() {
        return homeOverTime;
    }

    public void setHomeOverTime(Integer homeOverTime) {
        this.homeOverTime = homeOverTime;
    }

    public Integer getHomePenaltyShootout() {
        return homePenaltyShootout;
    }

    public void setHomePenaltyShootout(Integer homePenaltyShootout) {
        this.homePenaltyShootout = homePenaltyShootout;
    }

    public Integer getAwayPenaltyShootout() {
        return awayPenaltyShootout;
    }

    public void setAwayPenaltyShootout(Integer awayPenaltyShootout) {
        this.awayPenaltyShootout = awayPenaltyShootout;
    }

    public String getCompetition() {
        return competition;
    }

    public void setCompetition(String competition) {
        this.competition = competition;
    }

    public int getGroupSeq() {
        return groupSeq;
    }

    public void setGroupSeq(int groupSeq) {
        this.groupSeq = groupSeq;
    }

    public boolean isTimeUp() {
        return timeUp;
    }

    public void setTimeUp(boolean timeUp) {
        this.timeUp = timeUp;
    }

    @Override
    public String toString() {
        return "JakduScheduleWrite{" +
                "id='" + id + '\'' +
                ", date=" + date +
                ", home='" + home + '\'' +
                ", away='" + away + '\'' +
                ", homeFullTime=" + homeFullTime +
                ", awayFullTime=" + awayFullTime +
                ", homeOverTime=" + homeOverTime +
                ", awayOverTime=" + awayOverTime +
                ", homePenaltyShootout=" + homePenaltyShootout +
                ", awayPenaltyShootout=" + awayPenaltyShootout +
                ", competition='" + competition + '\'' +
                ", groupSeq=" + groupSeq +
                ", timeUp=" + timeUp +
                '}';
    }

}