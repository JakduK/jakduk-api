package com.jakduk.api.model.db;

import com.jakduk.api.model.embedded.BoardLog;
import com.jakduk.api.model.embedded.BoardStatus;
import com.jakduk.api.model.embedded.CommonFeelingUser;
import com.jakduk.api.model.embedded.CommonWriter;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 자유게시판 모델
 * @author pyohwan
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Document
public class Article implements UsersFeeling {

	@Id
	private String id;

	/**
	 * 작성자
	 */
	private CommonWriter writer;
	
	/**
	 * 글 제목
	 */
	private String subject;
	
	/**
	 * 글 내용
	 */
	private String content;
	
	/**
	 * 글 번호
	 */
	private Integer seq;

	// 게시판
	private String board;
	
	/**
	 * 분류 ID
	 */
	private String category;
	
	/**
	 * 조회
	 */
	private int views;
	
	private List<CommonFeelingUser> usersLiking;
	
	private List<CommonFeelingUser> usersDisliking;
	
	private BoardStatus status;

	// 오래된 글은 logs가 없는 경우도 있다.
	private List<BoardLog> logs;

	private List<String> batch;

	private String shortContent;

	private LocalDateTime lastUpdated;

	private Boolean linkedGallery;

}
