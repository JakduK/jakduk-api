package com.jakduk.core.model.embedded;

import com.jakduk.core.common.CommonConst;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 게시판 작성자
 * @author pyohwan
 *
 */

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonWriter {
	
	private String userId;
	private String username;
	private CommonConst.ACCOUNT_TYPE providerId;
}
