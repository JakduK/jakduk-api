package com.jakduk.authentication.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;
import com.jakduk.common.CommonConst;
import com.jakduk.service.CommonService;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 11. 8.
 * @desc     :
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommonPrincipal {

	private String id;

	private String email;
	
	private String username;
	
	private CommonConst.ACCOUNT_TYPE providerId;
}
