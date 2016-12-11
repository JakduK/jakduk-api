package com.jakduk.core.model.db;

import com.jakduk.core.common.CoreConst;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Document
@Data
public class Token {

	@Id
	private String email;
	private String code;
	private CoreConst.TOKEN_TYPE tokenType = CoreConst.TOKEN_TYPE.RESET_PASSWORD;

	@Temporal(TemporalType.DATE)
	private Date expireAt;
}
