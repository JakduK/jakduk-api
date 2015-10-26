package com.jakduk.model.simple;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.jakduk.model.embedded.BoardItem;
import com.jakduk.model.embedded.CommonWriter;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 2. 27.
 * @desc     :
 */

@Document(collection = "boardFreeComment")
public class BoardFreeCommentOnHome {
	
	@Id
	private String id;
	
	private BoardItem boardItem;
	
	private CommonWriter writer;
	
	private String content;

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

	@Override
	public String toString() {
		return "BoardFreeCommentOnHome [id=" + id + ", boardItem=" + boardItem
				+ ", writer=" + writer + ", content=" + content + "]";
	}

}
