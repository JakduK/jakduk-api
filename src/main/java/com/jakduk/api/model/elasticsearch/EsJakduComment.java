package com.jakduk.api.model.elasticsearch;

import com.jakduk.api.model.embedded.CommonWriter;

/**
 * @author pyohwan
 * 16. 3. 13 오후 10:56
 */

public class EsJakduComment {

	private String id;
	private String jakduScheduleId;
	private CommonWriter writer;
	private String contents;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJakduScheduleId() {
		return jakduScheduleId;
	}

	public void setJakduScheduleId(String jakduScheduleId) {
		this.jakduScheduleId = jakduScheduleId;
	}

	public CommonWriter getWriter() {
		return writer;
	}

	public void setWriter(CommonWriter writer) {
		this.writer = writer;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}
}
