package com.jakduk.model.db;

import com.jakduk.model.embedded.LocalSimpleName;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.List;

@Data
@Document
public class BoardCategory {

	@Id  @GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	private String code;

	private List<LocalSimpleName> names;

}
