package com.jakduk.authentication.common;
/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 9. 10.
 * @desc     :
 */
public class CommonUserDetails {
	
	private String email;
	private String gender;
	private String birthday;
	private String link;
	private String locale;
	private String bio;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public String getBio() {
		return bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}
	
	@Override
	public String toString() {
		return "CommonUserDetails [email=" + email + ", gender=" + gender
				+ ", birthday=" + birthday + ", link=" + link + ", locale="
				+ locale + ", bio=" + bio + "]";
	}
}
