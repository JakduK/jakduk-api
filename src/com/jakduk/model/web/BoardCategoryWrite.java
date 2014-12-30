package com.jakduk.model.web;

import org.hibernate.validator.constraints.NotEmpty;


/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 11. 16.
 * @desc     :
 */
public class BoardCategoryWrite {
	
	@NotEmpty
	private String name;
	
	@NotEmpty
	private String resName;
	
	@NotEmpty
	private String usingBoard;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public String getUsingBoard() {
		return usingBoard;
	}

	public void setUsingBoard(String usingBoard) {
		this.usingBoard = usingBoard;
	}

	@Override
	public String toString() {
		return "BoardCategoryWrite [name=" + name + ", resName=" + resName
				+ ", usingBoard=" + usingBoard + "]";
	}

}
