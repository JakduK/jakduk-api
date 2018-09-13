package com.jakduk.api.model.db;

import com.jakduk.api.common.Constants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author pyohwan
 * 16. 1. 10 오후 11:07
 */

@Document
public class JakduScheduleGroup {

    @Id
    private String id;
    private int seq;
    private Constants.JAKDU_GROUP_STATE state;
    private Date openDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public Constants.JAKDU_GROUP_STATE getState() {
        return state;
    }

    public void setState(Constants.JAKDU_GROUP_STATE state) {
        this.state = state;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }
}
