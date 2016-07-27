package com.jakduk.model.embedded;

import com.jakduk.common.CommonConst;
import lombok.Data;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 2. 3.
 * @desc     :
 */

@Data
public class GalleryStatus {
	
	private CommonConst.GALLERY_STATUS_TYPE status;

	private CommonConst.GALLERY_FROM_TYPE from;
}
