package com.jakduk.core.model.elasticsearch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jakduk.core.model.embedded.CommonWriter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Created by pyohwan on 16. 12. 5.
 */

@NoArgsConstructor
@Getter
@Setter
public class EsGallerySource {

    private String id;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String name;

    private CommonWriter writer;

    private Float score;

    private Map<String, List<String>> highlight;

}
