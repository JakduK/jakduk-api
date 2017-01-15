package com.jakduk.core.model.db;

import com.jakduk.core.model.embedded.LocalSimpleName;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.List;

@Getter
@Builder
@Document
public class BoardCategory {

	@Id  @GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	private String code;

	private List<LocalSimpleName> names;

}
