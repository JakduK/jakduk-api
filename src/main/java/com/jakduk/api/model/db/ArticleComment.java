package com.jakduk.api.model.db;

import com.jakduk.api.model.embedded.ArticleItem;
import com.jakduk.api.model.embedded.BoardLog;
import com.jakduk.api.model.embedded.CommonFeelingUser;
import com.jakduk.api.model.embedded.CommonWriter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 11. 16.
 * @desc     :
 */

@Document
public class ArticleComment implements UsersFeeling {

	@Id
	private String id;
	private ArticleItem article;
	private CommonWriter writer;
	private String content;
	private List<CommonFeelingUser> usersLiking;
	private List<CommonFeelingUser> usersDisliking;
	private Boolean linkedGallery;
	private List<BoardLog> logs;
    private List<String> batch;

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

	@Override
	public List<CommonFeelingUser> getUsersLiking() {
		return usersLiking;
	}

	@Override
	public void setUsersLiking(List<CommonFeelingUser> usersLiking) {
		this.usersLiking = usersLiking;
	}

	@Override
	public List<CommonFeelingUser> getUsersDisliking() {
		return usersDisliking;
	}

	@Override
	public void setUsersDisliking(List<CommonFeelingUser> usersDisliking) {
		this.usersDisliking = usersDisliking;
	}

	public Boolean getLinkedGallery() {
		return linkedGallery;
	}

	public void setLinkedGallery(Boolean linkedGallery) {
		this.linkedGallery = linkedGallery;
	}

	public List<BoardLog> getLogs() {
		return logs;
	}

	public void setLogs(List<BoardLog> logs) {
		this.logs = logs;
	}

	public List<String> getBatch() {
		return batch;
	}

	public void setBatch(List<String> batch) {
		this.batch = batch;
	}
}
