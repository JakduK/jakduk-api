package com.jakduk.api.model.simple;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.embedded.CommonWriter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 2. 12.
 * @desc     :
 */

@Document(collection = Constants.COLLECTION_GALLERY)
public class GallerySimple {
	
	@Id 
	private String id;
	private String name;
	private CommonWriter writer;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public CommonWriter getWriter() {
		return writer;
	}
}
