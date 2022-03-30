package com.jakduk.api.model.elasticsearch;

import com.jakduk.api.model.embedded.ArticleItem;
import com.jakduk.api.model.embedded.CommonWriter;

import java.util.List;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 8. 23.
 * @desc     :
 */

public class EsComment {

	private String id;
	private ArticleItem article;
	private CommonWriter writer;
	private String content;
	private List<String> galleries;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ArticleItem getArticle() {
		return article;
	}

	public void setArticle(ArticleItem article) {
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

	public List<String> getGalleries() {
		return galleries;
	}

	public void setGalleries(List<String> galleries) {
		this.galleries = galleries;
	}
}
