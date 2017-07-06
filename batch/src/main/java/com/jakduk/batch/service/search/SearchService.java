package com.jakduk.batch.service.search;

import com.jakduk.core.configuration.CoreProperties;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by pyohwanjang on 2017. 4. 7..
 */

@Slf4j
@Service
public class SearchService {

    @Resource
    private CoreProperties coreProperties;

    @Autowired
    private Client client;

    public void deleteIndexBoard() {

        String index = coreProperties.getElasticsearch().getIndexBoard();

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

        String index = coreProperties.getElasticsearch().getIndexGallery();

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

        String index = coreProperties.getElasticsearch().getIndexSearchWord();

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
