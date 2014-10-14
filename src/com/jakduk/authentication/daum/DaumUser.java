package com.jakduk.authentication.daum;

import java.io.Serializable;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 14.
 * @desc     :
 */
@JsonDeserialize(using = DaumUserDeserializer.class)
public class DaumUser implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7707925530696569852L;
	private String userid;
	private String id;
	private String nickname;
	private String imagePath;
	private String bigImagePath;
	private Boolean openProfile;
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getBigImagePath() {
		return bigImagePath;
	}
	public void setBigImagePath(String bigImagePath) {
		this.bigImagePath = bigImagePath;
	}
	public Boolean getOpenProfile() {
		return openProfile;
	}
	public void setOpenProfile(Boolean openProfile) {
		this.openProfile = openProfile;
	}
	
	@Override
	public String toString() {
		return "DaumUser [userid=" + userid + ", id=" + id + ", nickname="
				+ nickname + ", imagePath=" + imagePath + ", bigImagePath="
				+ bigImagePath + ", openProfile=" + openProfile + "]";
	}
}
