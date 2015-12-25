package com.jakduk.model.embedded;

/**
 * Created by pyohwan on 15. 12. 25.
 */
public class JakduScore {

    private Integer homeFirstHalf;

    private Integer awayFirstHalf;

    private Integer homeSecondHalf;

    private Integer awaySecondHalf;

    private Integer homeOvertime;

    private Integer awayOvertime;

    private Integer homePenaltyShootOut;

    private Integer awayPenaltyShootOut;

    public Integer getHomeFirstHalf() {
        return homeFirstHalf;
    }

    public void setHomeFirstHalf(Integer homeFirstHalf) {
        this.homeFirstHalf = homeFirstHalf;
    }

    public Integer getAwayFirstHalf() {
        return awayFirstHalf;
    }

    public void setAwayFirstHalf(Integer awayFirstHalf) {
        this.awayFirstHalf = awayFirstHalf;
    }

    public Integer getHomeSecondHalf() {
        return homeSecondHalf;
    }

    public void setHomeSecondHalf(Integer homeSecondHalf) {
        this.homeSecondHalf = homeSecondHalf;
    }

    public Integer getAwaySecondHalf() {
        return awaySecondHalf;
    }

    public void setAwaySecondHalf(Integer awaySecondHalf) {
        this.awaySecondHalf = awaySecondHalf;
    }

    public Integer getHomeOvertime() {
        return homeOvertime;
    }

    public void setHomeOvertime(Integer homeOvertime) {
        this.homeOvertime = homeOvertime;
    }

    public Integer getAwayOvertime() {
        return awayOvertime;
    }

    public void setAwayOvertime(Integer awayOvertime) {
        this.awayOvertime = awayOvertime;
    }

    public Integer getHomePenaltyShootOut() {
        return homePenaltyShootOut;
    }

    public void setHomePenaltyShootOut(Integer homePenaltyShootOut) {
        this.homePenaltyShootOut = homePenaltyShootOut;
    }

    public Integer getAwayPenaltyShootOut() {
        return awayPenaltyShootOut;
    }

    public void setAwayPenaltyShootOut(Integer awayPenaltyShootOut) {
        this.awayPenaltyShootOut = awayPenaltyShootOut;
    }

    @Override
    public String toString() {
        return "JakduScore{" +
                "homeFirstHalf=" + homeFirstHalf +
                ", awayFirstHalf=" + awayFirstHalf +
                ", homeSecondHalf=" + homeSecondHalf +
                ", awaySecondHalf=" + awaySecondHalf +
                ", homeOvertime=" + homeOvertime +
                ", awayOvertime=" + awayOvertime +
                ", homePenaltyShootOut=" + homePenaltyShootOut +
                ", awayPenaltyShootOut=" + awayPenaltyShootOut +
                '}';
    }
}
