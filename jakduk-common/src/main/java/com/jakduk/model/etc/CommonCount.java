package com.jakduk.model.etc;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 12. 30.
 * @desc     :
 */

@Data
public class CommonCount {
	private String id;
	private Integer count;
}