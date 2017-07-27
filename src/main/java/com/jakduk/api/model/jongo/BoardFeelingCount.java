package com.jakduk.api.model.jongo;

import lombok.Data;
import org.bson.types.ObjectId;
import org.jongo.marshall.jackson.oid.MongoId;
import org.springframework.data.annotation.Id;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
* @company  : http://jakduk.com
* @date     : 2015. 11. 7.
* @desc     :
*/

@Data
public class BoardFeelingCount {
	
    @MongoId //see NewAnnotationsCompatibilitySuiteTest for more informations
    private ObjectId id;
	
	private int seq;
	
	private int usersLikingCount;
	
	private int usersDisLikingCount;

}
