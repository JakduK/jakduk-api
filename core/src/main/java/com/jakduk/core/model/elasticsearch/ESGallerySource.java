package com.jakduk.core.model.elasticsearch;

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
public class ESGallerySource {

    private String id;

    private String name;

    private CommonWriter writer;

    private Float score;

    private Map<String, List<String>> highlight;

}
