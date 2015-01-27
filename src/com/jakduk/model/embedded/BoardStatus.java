package com.jakduk.model.embedded;
/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 11.
 * @desc     :
 */
public class BoardStatus {
	
	private String notice;
	
	private String delete;
	
	private String device;

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public String getDelete() {
		return delete;
	}

	public void setDelete(String delete) {
		this.delete = delete;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	@Override
	public String toString() {
		return "BoardStatus [notice=" + notice + ", delete=" + delete
				+ ", device=" + device + "]";
	}
	

}
