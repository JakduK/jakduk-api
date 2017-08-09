package com.jakduk.api.model.db;

import com.jakduk.api.common.Constants;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document
public class Token {

	@Id
	private String email;
	private String code;
	private Constants.TOKEN_TYPE tokenType = Constants.TOKEN_TYPE.RESET_PASSWORD;
	private Date expireAt;

}
