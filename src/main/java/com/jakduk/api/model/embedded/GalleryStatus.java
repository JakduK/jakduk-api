package com.jakduk.api.model.embedded;

import com.jakduk.api.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 2. 3.
 * @desc     :
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GalleryStatus {
	
	private Constants.GALLERY_STATUS_TYPE status;

}
