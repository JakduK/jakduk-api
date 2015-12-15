package com.jakduk.model.etc;

import org.bson.types.ObjectId;
import org.jongo.marshall.jackson.oid.MongoId;
import org.springframework.data.annotation.Id;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
* @company  : http://jakduk.com
* @date     : 2015. 11. 7.
* @desc     :
*/
public class BoardFeelingCount {
	
    @MongoId //see NewAnnotationsCompatibilitySuiteTest for more informations
    private ObjectId id;
	
	private int seq;
	
	private int usersLikingCount;
	
	private int usersDisLikingCount;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public int getUsersLikingCount() {
		return usersLikingCount;
	}

	public void setUsersLikingCount(int usersLikingCount) {
		this.usersLikingCount = usersLikingCount;
	}

	public int getUsersDisLikingCount() {
		return usersDisLikingCount;
	}

	public void setUsersDisLikingCount(int usersDisLikingCount) {
		this.usersDisLikingCount = usersDisLikingCount;
	}

	@Override
	public String toString() {
		return "BoardFeelingCount [id=" + id + ", seq=" + seq + ", usersLikingCount=" + usersLikingCount
				+ ", usersDisLikingCount=" + usersDisLikingCount + "]";
	}

}
