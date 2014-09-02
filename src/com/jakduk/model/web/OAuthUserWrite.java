package com.jakduk.model.web;
/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 9. 2.
 * @desc     :
 */
public class OAuthUserWrite {

	private String about;
	private String supportFC;
	
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public String getSupportFC() {
		return supportFC;
	}
	public void setSupportFC(String supportFC) {
		this.supportFC = supportFC;
	}
	
	@Override
	public String toString() {
		return "OAuthUserWrite [about=" + about + ", supportFC=" + supportFC
				+ "]";
	}
}
