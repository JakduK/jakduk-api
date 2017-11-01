package com.jakduk.api.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author pyohwan
 * 15. 12. 26 오후 8:50
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LocalName {

    private String language;
    private String fullName;
    private String shortName;

}
