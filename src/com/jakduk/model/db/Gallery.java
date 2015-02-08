package com.jakduk.model.db;

import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.jakduk.model.embedded.BoardItem;
import com.jakduk.model.embedded.CommonFeelingUser;
import com.jakduk.model.embedded.CommonWriter;
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
	
	private List<BoardItem> posts;
	
	private CommonWriter writer;
	
	private long size;
	
	private String contentType;
	
	private GalleryStatus status;
	
	private int views = 0;
	
	private List<CommonFeelingUser> usersLiking;
	
	private List<CommonFeelingUser> usersDisliking;

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

	public List<BoardItem> getPosts() {
		return posts;
	}

	public void setPosts(List<BoardItem> posts) {
		this.posts = posts;
	}

	public CommonWriter getWriter() {
		return writer;
	}

	public void setWriter(CommonWriter writer) {
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

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
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

	@Override
	public String toString() {
		return "Gallery [id=" + id + ", name=" + name + ", fileName="
				+ fileName + ", posts=" + posts + ", writer=" + writer
				+ ", size=" + size + ", contentType=" + contentType
				+ ", status=" + status + ", views=" + views + ", usersLiking="
				+ usersLiking + ", usersDisliking=" + usersDisliking + "]";
	}
	

}
