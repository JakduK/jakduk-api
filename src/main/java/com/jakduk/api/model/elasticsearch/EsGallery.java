package com.jakduk.api.model.elasticsearch;

import com.jakduk.api.model.embedded.CommonWriter;
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
public class EsGallery {

    private String id;
	
	private String name;
	
	private CommonWriter writer;
}
