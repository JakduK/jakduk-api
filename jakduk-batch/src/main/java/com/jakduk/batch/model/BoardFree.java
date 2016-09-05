package com.jakduk.batch.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 자유게시판 모델
 * @author pyohwan
 *
 */

@Data
@Document
public class BoardFree {

	/**
	 * ID
	 */
	@Id
	private String id;


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
	private int seq;
	

	/**
	 * 조회
	 */
	private int views;
	

}
