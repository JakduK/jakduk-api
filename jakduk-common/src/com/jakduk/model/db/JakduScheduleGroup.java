package com.jakduk.model.db;

import com.jakduk.common.CommonConst;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by pyohwan on 16. 1. 10.
 */

@Document
public class JakduScheduleGroup {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private String id;

    private int seq;

    private CommonConst.JAKDU_GROUP_STATE state;

    @Temporal(TemporalType.DATE)
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

    public CommonConst.JAKDU_GROUP_STATE getState() {
        return state;
    }

    public void setState(CommonConst.JAKDU_GROUP_STATE state) {
        this.state = state;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    @Override
    public String toString() {
        return "JakduScheduleGroup{" +
                "id='" + id + '\'' +
                ", seq=" + seq +
                ", state=" + state +
                ", openDate=" + openDate +
                '}';
    }
}
