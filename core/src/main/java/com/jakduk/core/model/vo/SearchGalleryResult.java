package com.jakduk.core.model.vo;

import com.jakduk.core.model.elasticsearch.ESGallerySource;
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
    private List<ESGallerySource> galleries;
}
