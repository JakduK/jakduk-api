package com.jakduk.api.restcontroller.vo.search;

import com.jakduk.api.model.elasticsearch.EsGallerySource;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Created by pyohwan on 16. 12. 5.
 */

@Builder
@Getter
public class SearchGalleryResult {

    private Long took;
    private Long totalCount;
    private List<EsGallerySource> galleries;
}
