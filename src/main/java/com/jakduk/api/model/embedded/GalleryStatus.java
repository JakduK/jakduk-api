package com.jakduk.api.model.embedded;

import com.jakduk.api.common.Constants;
import lombok.*;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 2. 3.
 * @desc     :
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GalleryStatus {
	
	private Constants.GALLERY_STATUS_TYPE status;

}
