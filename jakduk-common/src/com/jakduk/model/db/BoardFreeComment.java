package com.jakduk.model.db;

import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.jakduk.model.embedded.BoardCommentStatus;
import com.jakduk.model.embedded.BoardItem;
import com.jakduk.model.embedded.CommonFeelingUser;
import com.jakduk.model.embedded.CommonWriter;

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
	
	private CommonWriter writer;
	
	@NotEmpty
	private String content;
	
	private List<CommonFeelingUser> usersLiking;
	
	private List<CommonFeelingUser> usersDisliking;
	
	private BoardCommentStatus status;

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

	public CommonWriter getWriter() {
		return writer;
	}

	public void setWriter(CommonWriter writer) {
		this.writer = writer;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<CommonFeelingUser> getUsersLiking() {
		return usersLiking;
	}

	public void setUsersLiking(List<CommonFeelingUser> usersLiking) {
		this.usersLiking = usersLiking;
	}

	public List<CommonFeelingUser> getUsersDisliking() {
		return usersDisliking;
	}

	public void setUsersDisliking(List<CommonFeelingUser> usersDisliking) {
		this.usersDisliking = usersDisliking;
	}

	public BoardCommentStatus getStatus() {
		return status;
	}

	public void setStatus(BoardCommentStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "BoardFreeComment [id=" + id + ", boardItem=" + boardItem
				+ ", writer=" + writer + ", content=" + content
				+ ", usersLiking=" + usersLiking + ", usersDisliking="
				+ usersDisliking + ", status=" + status + "]";
	}
	
	
}
