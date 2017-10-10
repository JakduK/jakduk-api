package com.jakduk.api.model.elasticsearch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jakduk.api.model.embedded.CommonWriter;
import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * Created by pyohwan on 16. 12. 5.
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class EsGallerySource {

    private String id;
    private String name;
    private CommonWriter writer;
    private Float score;
    private Map<String, List<String>> highlight;

}
