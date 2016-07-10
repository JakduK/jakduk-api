package com.jakduk.authentication.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 9. 10.
 * @desc     :
 */

@Data
public class CommonUser {
	private String email;
	private String gender;
	private String birthday;
	private String link;
	private String locale;
	private String bio;
	private String imagePath;
}
