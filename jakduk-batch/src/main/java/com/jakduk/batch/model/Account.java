package com.jakduk.batch.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Jang, Pyohwan(1100273)
 * @since 2016. 9. 5.
 */

@Data
@Document
public class Account {

	@Id
	private String id;

	private String firstName;
	private String lastName;
}
