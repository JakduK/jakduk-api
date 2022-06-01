package com.jakduk.api.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by pyohwanjang on 2017. 2. 27..
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserPictureInfo {
	private String id;
	private String smallPictureUrl;
	private String largePictureUrl;

}
