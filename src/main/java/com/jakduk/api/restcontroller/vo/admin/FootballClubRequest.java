package com.jakduk.api.restcontroller.vo.admin;

import lombok.Data;


/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 9. 17.
 * @desc     :
 */

@Data
public class FootballClubRequest {

	private String id;
	
	private String origin;

	private String active;
	
	private String shortNameKr;
	
	private String fullNameKr;
	
	private String shortNameEn;
	
	private String fullNameEn;

}
