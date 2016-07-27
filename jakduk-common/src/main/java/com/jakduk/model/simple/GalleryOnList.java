package com.jakduk.model.simple;

import com.jakduk.model.embedded.CommonWriter;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 2. 12.
 * @desc     :
 */

@Data
@Document(collection = "gallery")
public class GalleryOnList {
	
	@Id 
	private String id;
	
	private String name;
	
	private CommonWriter writer;
	
	private int views = 0;
	
}
