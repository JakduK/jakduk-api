package com.jakduk.api.model.embedded;

/**
 * 관계만 나타내기 위하여 PK 정보만 있음. 변경될 소지가 있는 필드는 없어야 한다.
 *
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 12. 30.
 * @desc     :
 */

public class ArticleItem {

	private String id;
	private Integer seq;
	private String board;

	public ArticleItem() {
	}

	public ArticleItem(String id, Integer seq, String board) {
		this.id = id;
		this.seq = seq;
		this.board = board;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getBoard() {
		return board;
	}

	public void setBoard(String board) {
		this.board = board;
	}
}
