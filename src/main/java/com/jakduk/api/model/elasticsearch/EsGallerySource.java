package com.jakduk.api.model.elasticsearch;

import com.jakduk.api.model.embedded.CommonWriter;

import java.util.List;
import java.util.Map;

/**
 * Created by pyohwan on 16. 12. 5.
 */

public class EsGallerySource {

	private String id;
	private String name;
	private CommonWriter writer;
	private Float score;
	private Map<String, List<String>> highlight;

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

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public Map<String, List<String>> getHighlight() {
		return highlight;
	}

	public void setHighlight(Map<String, List<String>> highlight) {
		this.highlight = highlight;
	}
}
