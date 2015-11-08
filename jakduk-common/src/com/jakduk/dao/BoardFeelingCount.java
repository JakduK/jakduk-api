package com.jakduk.dao;

import org.springframework.data.annotation.Id;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
* @company  : http://jakduk.com
* @date     : 2015. 11. 7.
* @desc     :
*/
public class BoardFeelingCount {
	
	@Id
	private String id;
	
	private int seq;
	
	private Integer usersLikingCount;

	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public int getSeq() {
		return seq;
	}



	public void setSeq(int seq) {
		this.seq = seq;
	}



	public Integer getUsersLikingCount() {
		return usersLikingCount;
	}



	public void setUsersLikingCount(Integer usersLikingCount) {
		this.usersLikingCount = usersLikingCount;
	}



	@Override
	public String toString() {
		return "BoardFeelingCount [id=" + id + ", seq=" + seq + ", usersLikingCount=" + usersLikingCount + "]";
	}

}
