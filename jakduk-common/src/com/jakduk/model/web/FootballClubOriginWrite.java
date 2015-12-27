package com.jakduk.model.web;

import com.jakduk.common.CommonConst;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 * Created by pyohwan on 15. 12. 26.
 */
public class FootballClubOriginWrite {

    @Id
    private String id;

    private String name;

    private CommonConst.CLUB_TYPE clubType;

    private CommonConst.CLUB_AGE age;

    private CommonConst.CLUB_SEX sex;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CommonConst.CLUB_TYPE getClubType() {
        return clubType;
    }

    public void setClubType(CommonConst.CLUB_TYPE clubType) {
        this.clubType = clubType;
    }

    public CommonConst.CLUB_AGE getAge() {
        return age;
    }

    public void setAge(CommonConst.CLUB_AGE age) {
        this.age = age;
    }

    public CommonConst.CLUB_SEX getSex() {
        return sex;
    }

    public void setSex(CommonConst.CLUB_SEX sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "FootballClubOriginWrite{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", clubType=" + clubType +
                ", age=" + age +
                ", sex=" + sex +
                '}';
    }
}
