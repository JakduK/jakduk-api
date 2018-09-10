package com.jakduk.api.model.elasticsearch;

import com.jakduk.api.model.embedded.CommonWriter;

import java.util.List;
import java.util.Map;

/**
 * @author Jang, Pyohwan
 * @since 2016. 12. 2.
 */

public class EsCommentSource {

	private String id;
	private EsParentArticle article;
	private CommonWriter writer;
	private String content;
	private Float score;
	private Map<String, List<String>> highlight;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public EsParentArticle getArticle() {
		return article;
	}

	public void setArticle(EsParentArticle article) {
		this.article = article;
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
