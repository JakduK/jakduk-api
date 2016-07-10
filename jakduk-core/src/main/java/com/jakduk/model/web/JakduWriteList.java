package com.jakduk.model.web;

import com.jakduk.model.db.Jakdu;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * Created by pyohwan on 16. 1. 2.
 */
public class JakduWriteList {

    @NotEmpty
    private List<Jakdu> jakdus;

    public List<Jakdu> getJakdus() {
        return jakdus;
    }

    public void setJakdus(List<Jakdu> jakdus) {
        this.jakdus = jakdus;
    }

    @Override
    public String toString() {
        return "JakduWriteList{" +
                "jakdus=" + jakdus +
                '}';
    }
}
