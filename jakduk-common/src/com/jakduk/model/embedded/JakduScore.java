package com.jakduk.model.embedded;

/**
 * Created by pyohwan on 15. 12. 25.
 */
public class JakduScore {

    private Integer homeFullTime;

    private Integer awayFullTime;

    private Integer homeOverTime;

    private Integer awayOverTime;

    private Integer homePenaltyShootout;

    private Integer awayPenaltyShootout;

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

    public Integer getAwayOverTime() {
        return awayOverTime;
    }

    public void setAwayOverTime(Integer awayOverTime) {
        this.awayOverTime = awayOverTime;
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

    @Override
    public String toString() {
        return "JakduScore{" +
                "homeFullTime=" + homeFullTime +
                ", awayFullTime=" + awayFullTime +
                ", homeOverTime=" + homeOverTime +
                ", awayOverTime=" + awayOverTime +
                ", homePenaltyShootout=" + homePenaltyShootout +
                ", awayPenaltyShootout=" + awayPenaltyShootout +
                '}';
    }
}
