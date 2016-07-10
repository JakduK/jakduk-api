package com.jakduk.model.web.jakdu;

import com.jakduk.common.CommonConst;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by pyohwan on 16. 1. 10.
 */
public class JakduScheduleGroupWrite {

    @Id
    private String id;

    private int seq;

    private CommonConst.JAKDU_GROUP_STATE state;

    @Temporal(TemporalType.DATE)
    private Date openDate;

    private boolean nextSeq;

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

    public boolean isNextSeq() {
        return nextSeq;
    }

    public void setNextSeq(boolean nextSeq) {
        this.nextSeq = nextSeq;
    }

    @Override
    public String toString() {
        return "JakduScheduleGroupWrite{" +
                "id='" + id + '\'' +
                ", seq=" + seq +
                ", state=" + state +
                ", openDate=" + openDate +
                ", nextSeq=" + nextSeq +
                '}';
    }
}
