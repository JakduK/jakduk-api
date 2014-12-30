package com.jakduk.model.db;

import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.jakduk.model.embedded.BoardItem;
import com.jakduk.model.embedded.BoardUser;
import com.jakduk.model.embedded.BoardWriter;
import com.jakduk.model.simple.BoardFreeOnComment;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 11. 16.
 * @desc     :
 */

@Document
public class BoardFreeComment {

	@Id  @GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	private BoardItem boardItem;
	
	private BoardWriter writer;
	
	@NotEmpty
	private String content;
	
	private List<BoardUser> usersLiking;
	
	private List<BoardUser> usersDisliking;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BoardItem getBoardItem() {
		return boardItem;
	}

	public void setBoardItem(BoardItem boardItem) {
		this.boardItem = boardItem;
	}

	public BoardWriter getWriter() {
		return writer;
	}

	public void setWriter(BoardWriter writer) {
		this.writer = writer;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<BoardUser> getUsersLiking() {
		return usersLiking;
	}

	public void setUsersLiking(List<BoardUser> usersLiking) {
		this.usersLiking = usersLiking;
	}

	public List<BoardUser> getUsersDisliking() {
		return usersDisliking;
	}

	public void setUsersDisliking(List<BoardUser> usersDisliking) {
		this.usersDisliking = usersDisliking;
	}

	@Override
	public String toString() {
		return "BoardFreeComment [id=" + id + ", boardItem=" + boardItem
				+ ", writer=" + writer + ", content=" + content
				+ ", usersLiking=" + usersLiking + ", usersDisliking="
				+ usersDisliking + "]";
	}
	
}
