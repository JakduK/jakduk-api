package com.jakduk.api.restcontroller.vo;

import com.jakduk.api.common.Constants;

/**
 * 감정표현
 *
 * @author pyohwan
 * 16. 3. 26 오후 11:57
 */

public class UserFeelingResponse {
    private Constants.FEELING_TYPE myFeeling; // 나의 감정 표현 종류
    private Integer numberOfLike; // 좋아요 수
    private Integer numberOfDislike; // 싫어요 수

    public Constants.FEELING_TYPE getMyFeeling() {
        return myFeeling;
    }

    public void setMyFeeling(Constants.FEELING_TYPE myFeeling) {
        this.myFeeling = myFeeling;
    }

    public Integer getNumberOfLike() {
        return numberOfLike;
    }

    public void setNumberOfLike(Integer numberOfLike) {
        this.numberOfLike = numberOfLike;
    }

    public Integer getNumberOfDislike() {
        return numberOfDislike;
    }

    public void setNumberOfDislike(Integer numberOfDislike) {
        this.numberOfDislike = numberOfDislike;
    }
}
