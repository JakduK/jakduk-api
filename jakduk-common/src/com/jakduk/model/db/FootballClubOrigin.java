package com.jakduk.model.db;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import com.jakduk.common.CommonConst;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 9. 28.
 * @desc     :
 */

@Document
public class FootballClubOrigin {

	@Id  @GeneratedValue(strategy=GenerationType.AUTO)
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
		return "FootballClubOrigin{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", clubType=" + clubType +
				", age=" + age +
				", sex=" + sex +
				'}';
	}
}
