package com.jakduk.model.elasticsearch;

import com.jakduk.model.embedded.BoardItem;
import com.jakduk.model.embedded.CommonWriter;

import io.searchbox.annotations.JestId;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
* @company  : http://jakduk.com
* @date     : 2015. 8. 23.
* @desc     :
*/
public class CommentOnES {
	
	@JestId
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
		return "CommentOnES [id=" + id + ", boardItem=" + boardItem + ", writer=" + writer + ", content=" + content
				+ "]";
	}
}
