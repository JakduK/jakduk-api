package com.jakduk.model.web;

import com.jakduk.model.db.FootballClub;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.sql.Timestamp;

/**
 * Created by pyohwan on 15. 12. 24.
 */
public class JakduScheduleWrite {

    @Id
    private String id;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp date;

    @NotEmpty
    private String home;

    @NotEmpty
    private String away;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
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

    @Override
    public String toString() {
        return "JakduScheduleWrite{" +
                "id='" + id + '\'' +
                ", date=" + date +
                ", home='" + home + '\'' +
                ", away='" + away + '\'' +
                '}';
    }
}
