package com.jakduk.api.model.web;

import com.jakduk.api.model.db.Jakdu;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * @author pyohwan
 * 16. 1. 2 오후 11:15
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
