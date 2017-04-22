package com.jakduk.core.model.simple;

import com.jakduk.core.model.embedded.CommonWriter;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 2. 12.
 * @desc     :
 */

@Getter
@Document(collection = "gallery")
public class GallerySimple {
	
	@Id 
	private String id;
	
	private String name;
	
	private CommonWriter writer;
	
	private int views = 0;
	
}
