package com.jakduk.api.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author pyohwan
 * 15. 12. 26 오후 10:22
 */

@AllArgsConstructor
@Getter
@ToString
public class LocalSimpleName {

    private String language;
    private String name;

}
