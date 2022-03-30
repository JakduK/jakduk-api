package com.jakduk.api.model.db;

import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.model.embedded.GalleryStatus;
import com.jakduk.api.model.embedded.LinkedItem;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 18.
 * @desc     :
 */

@Document
public class Gallery {
	
	@Id
	private String id;
	private GalleryStatus status;
	private CommonWriter writer;
	private String contentType;
	private String name;
	private String fileName;
	private Long size;
	private Long fileSize;
	private String hash;
	private List<LinkedItem> linkedItems;
	private List<String> batch;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public GalleryStatus getStatus() {
		return status;
	}

	public void setStatus(GalleryStatus status) {
		this.status = status;
	}

	public CommonWriter getWriter() {
		return writer;
	}

	public void setWriter(CommonWriter writer) {
		this.writer = writer;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
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

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public List<LinkedItem> getLinkedItems() {
		return linkedItems;
	}

	public void setLinkedItems(List<LinkedItem> linkedItems) {
		this.linkedItems = linkedItems;
	}

	public List<String> getBatch() {
		return batch;
	}

	public void setBatch(List<String> batch) {
		this.batch = batch;
	}
}
