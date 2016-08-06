package com.jakduk.core.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pyohwan
 * 15. 12. 26 오후 8:50
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocalName {

    private String language;

    private String fullName;

    private String shortName;
}
