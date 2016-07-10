package com.jakduk.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by pyohwan on 15. 12. 26.
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
