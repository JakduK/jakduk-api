package com.jakduk.batch.service.search;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by pyohwanjang on 2017. 4. 7..
 */

@Slf4j
@Service
public class SearchService {

    @Value("${core.elasticsearch.index.board}")
    private String elasticsearchIndexBoard;

    @Value("${core.elasticsearch.index.gallery}")
    private String elasticsearchIndexGallery;

    @Value("${core.elasticsearch.index.search.word}")
    private String elasticsearchIndexSearchWord;

    @Autowired
    private Client client;

    public void deleteIndexBoard() {

        String index = elasticsearchIndexBoard;

        DeleteIndexResponse response = client.admin().indices()
                .delete(new DeleteIndexRequest(index))
                .actionGet();

        if (response.isAcknowledged()) {
            log.debug("Index {} deleted." + index);
        } else {
            throw new RuntimeException("Index " + index + " not deleted");
        }
    }

    public void deleteIndexGallery() {

        String index = elasticsearchIndexGallery;

        DeleteIndexResponse response = client.admin().indices()
                .delete(new DeleteIndexRequest(index))
                .actionGet();

        if (response.isAcknowledged()) {
            log.debug("Index {} deleted." + index);
        } else {
            throw new RuntimeException("Index " + index + " not deleted");
        }
    }

    public void deleteIndexSearchWord() {

        String index = elasticsearchIndexSearchWord;

        DeleteIndexResponse response = client.admin().indices()
                .delete(new DeleteIndexRequest(index))
                .actionGet();

        if (response.isAcknowledged()) {
            log.debug("Index {} deleted." + index);
        } else {
            throw new RuntimeException("Index " + index + " not deleted");
        }
    }
}
