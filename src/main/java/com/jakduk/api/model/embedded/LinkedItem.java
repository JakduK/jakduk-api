package com.jakduk.api.model.embedded;

import com.jakduk.api.common.Constants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by pyohwanjang on 2017. 4. 10..
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LinkedItem {
	private String id;
	private Constants.GALLERY_FROM_TYPE from;
}
