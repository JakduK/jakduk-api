package com.jakduk.core.model.jongo;

import com.jakduk.core.model.embedded.BoardStatus;
import lombok.Data;
import org.jongo.marshall.jackson.oid.MongoId;

@Data
public class BoardFreeOnBest {

	@MongoId //see NewAnnotationsCompatibilitySuiteTest for more informations
	private String id;
	
	private int seq;
	
	private BoardStatus status;
	
	private String subject;
	
	private int count;
	
	private int views;

}
