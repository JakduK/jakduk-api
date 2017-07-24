package com.jakduk.api.model.web.board;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;


/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 11. 16.
 * @desc     :
 */

@Data
public class BoardCategoryWrite {
	
	@Id
	private String id;
	
	@NotEmpty
	private String code;

	@NotEmpty
	private String nameKr;

	@NotEmpty
	private String nameEn;
}
