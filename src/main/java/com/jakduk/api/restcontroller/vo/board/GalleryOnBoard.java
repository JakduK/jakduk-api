package com.jakduk.api.restcontroller.vo.board;

/**
 * 글/댓글 쓰기 시 사진 연동
 *
 * @author pyohwan
 *         16. 7. 19 오후 9:30
 */

public class GalleryOnBoard {
	private String id; // 사진 ID
	private String name; // 사진 이름

	public GalleryOnBoard() {
	}

	public GalleryOnBoard(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
