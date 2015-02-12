package com.jakduk.dao;

import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.jakduk.model.embedded.BoardItem;
import com.jakduk.model.embedded.CommonWriter;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 2. 12.
 * @desc     :
 */

@Document(collection = "gallery")
public class GalleryOnHome {
	
	@Id 
	private String id;
	
	private String name;
	
	private CommonWriter writer;

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

	public CommonWriter getWriter() {
		return writer;
	}

	public void setWriter(CommonWriter writer) {
		this.writer = writer;
	}

	@Override
	public String toString() {
		return "GalleryOnHome [id=" + id + ", name=" + name + ", writer="
				+ writer + "]";
	}

}
