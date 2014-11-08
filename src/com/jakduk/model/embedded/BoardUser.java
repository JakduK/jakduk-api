package com.jakduk.model.embedded;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 6. 16.
 * @desc     : 게시판의 좋아요, 싫어요 등을 사용하는 사용자
 */
@Document
public class BoardUser {
	
	@Id  @GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	private String userid;
	
	@NotNull
	private String username;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "BoardUser [id=" + id + ", userid=" + userid + ", username="
				+ username + "]";
	}
}
