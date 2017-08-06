package com.jakduk.api.model.jongo;

import com.jakduk.api.model.embedded.BoardStatus;
import lombok.Builder;
import lombok.Data;
import org.jongo.marshall.jackson.oid.MongoId;

@Data
@Builder
public class BoardFreeOnBest {

	@MongoId //see NewAnnotationsCompatibilitySuiteTest for more informations
	private String id;
	
	private int seq;
	
	private BoardStatus status;
	
	private String subject;
	
	private int count;
	
	private int views;

}
