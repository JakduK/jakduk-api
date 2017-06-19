package com.jakduk.core.model.rabbitmq;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.elasticsearch.EsDocument;
import lombok.Builder;
import lombok.Getter;

/**
 * Created by pyohwanjang on 2017. 6. 19..
 */

@Builder
@Getter
public class ElasticsearchPayload {

    private CoreConst.ELASTICSEARCH_TYPE type;
    private EsDocument document;

}
