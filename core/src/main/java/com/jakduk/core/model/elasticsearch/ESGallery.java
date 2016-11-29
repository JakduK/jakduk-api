package com.jakduk.core.model.elasticsearch;

import com.jakduk.core.model.db.Gallery;
import com.jakduk.core.model.embedded.CommonWriter;
import io.searchbox.annotations.JestId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
* @company  : http://jakduk.com
* @date     : 2015. 8. 27.
* @desc     :
*/

@NoArgsConstructor
@Getter
public class ESGallery {

	@JestId
    private String id;
	
	private String name;
	
	private CommonWriter writer;

	public ESGallery(Gallery gallery) {
		this.id = gallery.getId();
		this.name = gallery.getName();
		this.writer = gallery.getWriter();
	}
}
