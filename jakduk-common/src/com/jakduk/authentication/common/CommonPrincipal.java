package com.jakduk.authentication.common;

import com.jakduk.common.CommonConst;
import lombok.Data;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 11. 8.
 * @desc     :
 */

@Data
public class CommonPrincipal {
	
	private String id;
	
	private String username;
	
	private CommonConst.ACCOUNT_TYPE type;

}
