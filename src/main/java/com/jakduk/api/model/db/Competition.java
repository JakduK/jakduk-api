package com.jakduk.api.model.db;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.jakduk.api.model.embedded.LocalName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pyohwan
 * 15. 12. 26 오후 11:06
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document
public class Competition {

	@Id
	private String id;
	private String code;
	private List<LocalName> names;

}
