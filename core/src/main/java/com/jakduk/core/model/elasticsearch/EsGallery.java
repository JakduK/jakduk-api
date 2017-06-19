package com.jakduk.core.model.elasticsearch;

import com.jakduk.core.model.embedded.CommonWriter;
import lombok.*;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
* @company  : http://jakduk.com
* @date     : 2015. 8. 27.
* @desc     :
*/

@Builder
@Getter
@Setter
public class EsGallery implements EsDocument {

    private String id;
	
	private String name;
	
	private CommonWriter writer;
}
