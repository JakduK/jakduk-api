package com.jakduk.api.model.embedded;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 11.
 * @desc     :
 */

public class ArticleStatus {
	private Boolean notice;
	private Boolean delete;

	public ArticleStatus() {
	}

	public ArticleStatus(Boolean notice, Boolean delete) {
		this.notice = notice;
		this.delete = delete;
	}

	public Boolean getNotice() {
		return notice;
	}

	public void setNotice(Boolean notice) {
		this.notice = notice;
	}

	public Boolean getDelete() {
		return delete;
	}

	public void setDelete(Boolean delete) {
		this.delete = delete;
	}
}
