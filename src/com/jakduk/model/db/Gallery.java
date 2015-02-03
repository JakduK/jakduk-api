package com.jakduk.model.db;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.jakduk.model.embedded.BoardItem;
import com.jakduk.model.embedded.BoardWriter;
import com.jakduk.model.embedded.GalleryStatus;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 18.
 * @desc     :
 */

@Document
public class Gallery {
	
	@Id  @GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	private String name;
	
	private String fileName;
	
	private BoardItem boardItem;
	
	private BoardWriter writer;
	
	private long size;
	
	private String contentType;
	
	private GalleryStatus status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
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

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public GalleryStatus getStatus() {
		return status;
	}

	public void setStatus(GalleryStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Gallery [id=" + id + ", name=" + name + ", fileName="
				+ fileName + ", boardItem=" + boardItem + ", writer=" + writer
				+ ", size=" + size + ", contentType=" + contentType
				+ ", status=" + status + "]";
	}
	

}
