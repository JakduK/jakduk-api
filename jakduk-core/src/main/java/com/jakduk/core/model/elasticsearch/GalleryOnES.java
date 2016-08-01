package com.jakduk.core.model.elasticsearch;

import com.jakduk.core.model.embedded.CommonWriter;
import io.searchbox.annotations.JestId;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
* @company  : http://jakduk.com
* @date     : 2015. 8. 27.
* @desc     :
*/
public class GalleryOnES {

	@JestId
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
		return "GalleryOnES [id=" + id + ", name=" + name + ", writer=" + writer + "]";
	}
	
}
