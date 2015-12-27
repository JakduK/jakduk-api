package com.jakduk.model.web;

import org.springframework.data.annotation.Id;

/**
 * Created by pyohwan on 15. 12. 26.
 */
public class CompetitionWrite {

    @Id
    private String id;

    private String code;

    private String shortNameKr;

    private String fullNameKr;

    private String shortNameEn;

    private String fullNameEn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getShortNameKr() {
        return shortNameKr;
    }

    public void setShortNameKr(String shortNameKr) {
        this.shortNameKr = shortNameKr;
    }

    public String getFullNameKr() {
        return fullNameKr;
    }

    public void setFullNameKr(String fullNameKr) {
        this.fullNameKr = fullNameKr;
    }

    public String getShortNameEn() {
        return shortNameEn;
    }

    public void setShortNameEn(String shortNameEn) {
        this.shortNameEn = shortNameEn;
    }

    public String getFullNameEn() {
        return fullNameEn;
    }

    public void setFullNameEn(String fullNameEn) {
        this.fullNameEn = fullNameEn;
    }

    @Override
    public String toString() {
        return "CompetitionWrite{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", shortNameKr='" + shortNameKr + '\'' +
                ", fullNameKr='" + fullNameKr + '\'' +
                ", shortNameEn='" + shortNameEn + '\'' +
                ", fullNameEn='" + fullNameEn + '\'' +
                '}';
    }
}
