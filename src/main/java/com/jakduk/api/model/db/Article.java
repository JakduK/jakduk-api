package com.jakduk.api.model.db;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.jakduk.api.model.embedded.ArticleStatus;
import com.jakduk.api.model.embedded.BoardLog;
import com.jakduk.api.model.embedded.CommonFeelingUser;
import com.jakduk.api.model.embedded.CommonWriter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 자유게시판 모델
 * @author pyohwan
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Document
public class Article implements UsersFeeling {

	@Id
	private String id;
	private Integer seq; // 글 번호
	private String board; // 게시판
	private String category; // 말머리 코드
	private ArticleStatus status;
	private CommonWriter writer; // 작성자
	private String subject; // 글 제목
	private String content; // 글 내용
	private Integer views; // 읽음 수
	private List<CommonFeelingUser> usersLiking;
	private List<CommonFeelingUser> usersDisliking;
	private List<BoardLog> logs; // 오래된 글은 logs가 없는 경우도 있다.
	private List<String> batch;
	private String shortContent;
	private LocalDateTime lastUpdated;
	private Boolean linkedGallery;

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
}
