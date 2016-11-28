package com.jakduk.core.model.db;

import com.jakduk.core.common.CoreConst;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @author pyohwan
 * 16. 1. 10 오후 11:07
 */

@Document
public class JakduScheduleGroup {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private String id;

    private int seq;

    private CoreConst.JAKDU_GROUP_STATE state;

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

    public CoreConst.JAKDU_GROUP_STATE getState() {
        return state;
    }

    public void setState(CoreConst.JAKDU_GROUP_STATE state) {
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
