package com.jakduk.model.web;

import java.util.List;

import com.jakduk.model.embedded.FootballClubName;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 9. 17.
 * @desc     :
 */
public class FootballClubWrite {
	
	private String origin;

	private Integer active;
	
	private String shortNameKr;
	
	private String fullNameKr;
	
	private String shortNameEn;
	
	private String fullNameEn;


	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
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
		return "FootballClubWrite [origin=" + origin + ", active=" + active
				+ ", shortNameKr=" + shortNameKr + ", fullNameKr=" + fullNameKr
				+ ", shortNameEn=" + shortNameEn + ", fullNameEn=" + fullNameEn
				+ "]";
	}

}
