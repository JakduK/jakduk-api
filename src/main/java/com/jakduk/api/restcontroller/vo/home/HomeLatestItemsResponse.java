package com.jakduk.api.restcontroller.vo.home;

import com.jakduk.api.model.db.HomeDescription;
import com.jakduk.api.model.simple.UserSimple;

import java.util.List;

/**
 * 홈에서 보여질 데이터 들
 *
 * @author pyohwan
 * 16. 5. 7 오후 10:01
 */

public class HomeLatestItemsResponse {
	private List<HomeArticle> articles; // 최근 글
	private List<UserSimple> users; // 최근 가입 회원
	private List<HomeGallery> galleries; // 최근 사진
	private List<HomeArticleComment> comments; // 최근 댓글
	private HomeDescription homeDescription; // 상단 글

	public List<HomeArticle> getArticles() {
		return articles;
	}

	public void setArticles(List<HomeArticle> articles) {
		this.articles = articles;
	}

	public List<UserSimple> getUsers() {
		return users;
	}

	public void setUsers(List<UserSimple> users) {
		this.users = users;
	}

	public List<HomeGallery> getGalleries() {
		return galleries;
	}

	public void setGalleries(List<HomeGallery> galleries) {
		this.galleries = galleries;
	}

	public List<HomeArticleComment> getComments() {
		return comments;
	}

	public void setComments(List<HomeArticleComment> comments) {
		this.comments = comments;
	}

	public HomeDescription getHomeDescription() {
		return homeDescription;
	}

	public void setHomeDescription(HomeDescription homeDescription) {
		this.homeDescription = homeDescription;
	}
}
