package com.jakduk.api.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pyohwan
 * 15. 12. 26 오후 8:50
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LocalName {
	private String language;
	private String fullName;
	private String shortName;
}
