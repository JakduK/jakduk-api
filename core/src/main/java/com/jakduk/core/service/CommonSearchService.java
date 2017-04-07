package com.jakduk.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jakduk.core.common.CoreConst;
import com.jakduk.core.common.util.CoreUtils;
import com.jakduk.core.common.util.ObjectMapperUtils;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.model.db.BoardFree;
import com.jakduk.core.model.elasticsearch.*;
import com.jakduk.core.model.embedded.BoardImage;
import com.jakduk.core.model.embedded.BoardItem;
import com.jakduk.core.model.embedded.CommonWriter;
import com.jakduk.core.repository.board.free.BoardFreeCommentRepository;
import com.jakduk.core.repository.board.free.BoardFreeRepository;
import com.jakduk.core.repository.gallery.GalleryRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by pyohwanjang on 2017. 4. 7..
 */

@Slf4j
@Service
public class CommonSearchService {

    @Value("${core.elasticsearch.enable}")
    private boolean elasticsearchEnable;

    @Value("${core.elasticsearch.index.board}")
    private String elasticsearchIndexBoard;

    @Value("${core.elasticsearch.index.gallery}")
    private String elasticsearchIndexGallery;

    @Value("${core.elasticsearch.index.search.word}")
    private String elasticsearchIndexSearchWord;

    @Value("${core.elasticsearch.bulk.actions}")
    private Integer bulkActions;

    @Value("${core.elasticsearch.bulk.size.mb}")
    private Integer bulkMbSize;

    @Value("${core.elasticsearch.bulk.flush.interval.seconds}")
    private Integer bulkFlushIntervalSeconds;

    @Value("${core.elasticsearch.bulk.concurrent.requests}")
    private Integer bulkConcurrentRequests;

    @Autowired
    private Client client;

    @Autowired
    private BoardFreeRepository boardFreeRepository;

    @Autowired
    private BoardFreeCommentRepository boardFreeCommentRepository;

    @Autowired
    private GalleryRepository galleryRepository;

    public void createIndexBoard() {

        String index = elasticsearchIndexBoard;

        try {
            CreateIndexResponse response = client.admin().indices().prepareCreate(index)
                    .setSettings(getIndexSettings())
                    .addMapping(CoreConst.ES_TYPE_BOARD, getBoardFreeMappings())
                    .addMapping(CoreConst.ES_TYPE_COMMENT, getBoardFreeCommentMappings())
                    .get();

            if (response.isAcknowledged()) {
                log.debug("Index " + index + " created");
            } else {
                throw new RuntimeException("Index " + index + " not created");
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Index " + index + " not created", e.getCause());
        }
    }

    public void createIndexGallery() {

        String index = elasticsearchIndexGallery;

        try {
            CreateIndexResponse response = client.admin().indices().prepareCreate(index)
                    .setSettings(getIndexSettings())
                    .addMapping(CoreConst.ES_TYPE_GALLERY, getGalleryMappings())
                    .get();

            if (response.isAcknowledged()) {
                log.debug("Index " + index + " created");
            } else {
                throw new RuntimeException("Index " + index + " not created");
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Index " + index + " not created", e.getCause());
        }
    }

    public void createIndexSearchWord() {

        String index = elasticsearchIndexSearchWord;

        try {
            CreateIndexResponse response = client.admin().indices().prepareCreate(index)
                    .setSettings(getIndexSettings())
                    .addMapping(CoreConst.ES_TYPE_SEARCH_WORD, getSearchWordMappings())
                    .get();

            if (response.isAcknowledged()) {
                log.debug("Index " + index + " created");
            } else {
                throw new RuntimeException("Index " + index + " not created");
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Index " + index + " not created", e.getCause());
        }
    }

    public void processBulkInsertBoard() throws InterruptedException {

        BulkProcessor bulkProcessor = getBulkProcessor();

        Boolean hasPost = true;
        ObjectId lastPostId = null;

        do {
            List<BoardFree> posts = boardFreeRepository.findPostsGreaterThanId(lastPostId, CoreConst.ES_BULK_LIMIT);

            List<ESBoard> esBoards = posts.stream()
                    .filter(Objects::nonNull)
                    .map(post -> {
                        List<String> galleryIds = null;

                        if (Objects.nonNull(post.getGalleries())) {
                            galleryIds = post.getGalleries().stream()
                                    .filter(Objects::nonNull)
                                    .map(BoardImage::getId)
                                    .collect(Collectors.toList());
                        }

                        return ESBoard.builder()
                                .id(post.getId())
                                .seq(post.getSeq())
                                .writer(post.getWriter())
                                .subject(CoreUtils.stripHtmlTag(post.getSubject()))
                                .content(CoreUtils.stripHtmlTag(post.getContent()))
                                .category(Objects.nonNull(post.getCategory()) ? post.getCategory().name() : null)
                                .galleries(galleryIds)
                                .build();
                    })
                    .collect(Collectors.toList());

            if (esBoards.isEmpty()) {
                hasPost = false;
            } else {
                ESBoard lastPost = esBoards.get(esBoards.size() - 1);
                lastPostId = new ObjectId(lastPost.getId());
            }

            esBoards.forEach(post -> {
                IndexRequestBuilder index = client.prepareIndex(
                        elasticsearchIndexBoard,
                        CoreConst.ES_TYPE_BOARD,
                        post.getId()
                );

                try {
                    index.setSource(ObjectMapperUtils.writeValueAsString(post));
                    bulkProcessor.add(index.request());

                } catch (JsonProcessingException e) {
                    log.error(e.getLocalizedMessage());
                }

            });

        } while (hasPost);

        bulkProcessor.awaitClose(CoreConst.ES_AWAIT_CLOSE_TIMEOUT_MINUTES, TimeUnit.MINUTES);
    }

    public void processBulkInsertComment() throws InterruptedException {

        BulkProcessor bulkProcessor = getBulkProcessor();

        Boolean hasComment = true;
        ObjectId lastCommentId = null;

        do {
            List<ESComment> comments = boardFreeCommentRepository.findCommentsGreaterThanId(lastCommentId, CoreConst.ES_BULK_LIMIT);

            if (comments.isEmpty()) {
                hasComment = false;
            } else {
                ESComment lastComment = comments.get(comments.size() - 1);
                lastCommentId = new ObjectId(lastComment.getId());
            }

            comments.forEach(comment -> {
                try {
                    IndexRequestBuilder index = client.prepareIndex()
                            .setIndex(elasticsearchIndexBoard)
                            .setType(CoreConst.ES_TYPE_COMMENT)
                            .setId(comment.getId())
                            .setParent(comment.getBoardItem().getId())
                            .setSource(ObjectMapperUtils.writeValueAsString(comment));

                    bulkProcessor.add(index.request());

                } catch (JsonProcessingException e) {
                    log.error(e.getLocalizedMessage());
                }

            });

        } while (hasComment);

        bulkProcessor.awaitClose(CoreConst.ES_AWAIT_CLOSE_TIMEOUT_MINUTES, TimeUnit.MINUTES);
    }

    public void processBulkInsertGallery() throws InterruptedException {

        BulkProcessor bulkProcessor = getBulkProcessor();

        Boolean hasGallery = true;
        ObjectId lastGalleryId = null;

        do {
            List<ESGallery> comments = galleryRepository.findGalleriesGreaterThanId(lastGalleryId, CoreConst.ES_BULK_LIMIT);

            if (comments.isEmpty()) {
                hasGallery = false;
            } else {
                ESGallery lastGallery = comments.get(comments.size() - 1);
                lastGalleryId = new ObjectId(lastGallery.getId());
            }

            comments.forEach(comment -> {
                IndexRequestBuilder index = client.prepareIndex(
                        elasticsearchIndexGallery,
                        CoreConst.ES_TYPE_GALLERY,
                        comment.getId()
                );

                try {
                    index.setSource(ObjectMapperUtils.writeValueAsString(comment));
                    bulkProcessor.add(index.request());

                } catch (JsonProcessingException e) {
                    log.error(e.getLocalizedMessage());
                }

            });

        } while (hasGallery);

        bulkProcessor.awaitClose(CoreConst.ES_AWAIT_CLOSE_TIMEOUT_MINUTES, TimeUnit.MINUTES);
    }

    @Async
    public void indexDocumentBoard(String id, Integer seq, CommonWriter writer, String subject, String content, String category,
                                   List<String> galleryIds) {

        if (! elasticsearchEnable)
            return;

        ESBoard esBoard = ESBoard.builder()
                .id(id)
                .seq(seq)
                .writer(writer)
                .subject(CoreUtils.stripHtmlTag(subject))
                .content(CoreUtils.stripHtmlTag(content))
                .category(category)
                .galleries(galleryIds)
                .build();

        try {
            IndexResponse response = client.prepareIndex()
                    .setIndex(elasticsearchIndexBoard)
                    .setType(CoreConst.ES_TYPE_BOARD)
                    .setId(id)
                    .setSource(ObjectMapperUtils.writeValueAsString(esBoard))
                    .get();

        } catch (IOException e) {
            throw new ServiceException(ServiceError.ELASTICSEARCH_INDEX_FAILED, e.getCause());
        }
    }

    @Async
    public void deleteDocumentBoard(String id) {

        if (! elasticsearchEnable)
            return;

        DeleteResponse response = client.prepareDelete()
                .setIndex(elasticsearchIndexBoard)
                .setType(CoreConst.ES_TYPE_BOARD)
                .setId(id)
                .get();

        if (! response.isFound())
            log.info("board id {} is not found. so can't delete it!", id);

    }

    @Async
    public void indexDocumentBoardComment(String id, BoardItem boardItem, CommonWriter writer, String content) {

        if (! elasticsearchEnable)
            return;

        ESComment esComment = ESComment.builder()
                .id(id)
                .boardItem(boardItem)
                .writer(writer)
                .content(CoreUtils.stripHtmlTag(content))
                .build();

        try {
            IndexResponse response = client.prepareIndex()
                    .setIndex(elasticsearchIndexBoard)
                    .setType(CoreConst.ES_TYPE_COMMENT)
                    .setId(id)
                    .setParent(boardItem.getId())
                    .setSource(ObjectMapperUtils.writeValueAsString(esComment))
                    .get();

        } catch (IOException e) {
            throw new ServiceException(ServiceError.ELASTICSEARCH_INDEX_FAILED, e.getCause());
        }
    }

    @Async
    public void deleteDocumentBoardComment(String id) {

        if (! elasticsearchEnable)
            return;

        DeleteResponse response = client.prepareDelete()
                .setIndex(elasticsearchIndexBoard)
                .setType(CoreConst.ES_TYPE_COMMENT)
                .setId(id)
                .get();

        if (! response.isFound())
            log.info("comment id {} is not found. so can't delete it!", id);

    }

    // TODO : 구현 해야 함
    public void createDocumentJakduComment(ESJakduComment ESJakduComment) {}

    @Async
    public void indexDocumentGallery(String id, CommonWriter writer, String name) {

        if (! elasticsearchEnable)
            return;

        ESGallery esGallery = ESGallery.builder()
                .id(id)
                .writer(writer)
                .name(name)
                .build();

        try {
            IndexResponse response = client.prepareIndex()
                    .setIndex(elasticsearchIndexGallery)
                    .setType(CoreConst.ES_TYPE_GALLERY)
                    .setId(id)
                    .setSource(ObjectMapperUtils.writeValueAsString(esGallery))
                    .get();

        } catch (IOException e) {
            throw new ServiceException(ServiceError.ELASTICSEARCH_INDEX_FAILED, e.getCause());
        }
    }

    @Async
    public void deleteDocumentGallery(String id) {

        if (! elasticsearchEnable)
            return;

        DeleteResponse response = client.prepareDelete()
                .setIndex(elasticsearchIndexGallery)
                .setType(CoreConst.ES_TYPE_GALLERY)
                .setId(id)
                .get();

        if (! response.isFound())
            log.info("gallery id {} is not found. so can't delete it!", id);
    }

    @Async
    public void indexDocumentSearchWord(String word, CommonWriter writer) {

        if (! elasticsearchEnable)
            return;

        ESSearchWord esSearchWord = ESSearchWord.builder()
                .word(word)
                .writer(writer)
                .registerDate(LocalDateTime.now())
                .build();

        try {
            IndexRequestBuilder indexRequestBuilder = client.prepareIndex();

            IndexResponse response = indexRequestBuilder
                    .setIndex(elasticsearchIndexSearchWord)
                    .setType(CoreConst.ES_TYPE_SEARCH_WORD)
                    .setSource(ObjectMapperUtils.writeValueAsString(esSearchWord))
                    .get();

            log.debug("indexDocumentSearchWord Source:\n {}", indexRequestBuilder.request().getDescription());

        } catch (IOException e) {
            throw new ServiceException(ServiceError.ELASTICSEARCH_INDEX_FAILED, e.getCause());
        }
    }

    private BulkProcessor getBulkProcessor() {
        BulkProcessor.Listener bulkProcessorListener = new BulkProcessor.Listener() {
            @Override public void beforeBulk(long l, BulkRequest bulkRequest) {
            }

            @Override public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {
                log.debug("bulk took: {}", bulkResponse.getTookInMillis());

                if (bulkResponse.hasFailures())
                    log.error(bulkResponse.buildFailureMessage());
            }

            @Override public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) {
                log.error(throwable.getLocalizedMessage());
            }
        };

        return BulkProcessor.builder(client, bulkProcessorListener)
                .setBulkActions(bulkActions)
                .setBulkSize(new ByteSizeValue(bulkMbSize, ByteSizeUnit.MB))
                .setFlushInterval(TimeValue.timeValueSeconds(bulkFlushIntervalSeconds))
                .setConcurrentRequests(bulkConcurrentRequests)
                .build();
    }


    private Settings.Builder getIndexSettings() {

        //settingsBuilder.put("number_of_shards", 5);
        //settingsBuilder.put("number_of_replicas", 1);

        String[] userWords = new String[]{
                "k리그",
                "내셔널리그",
                "k3리그",
                "k3",
                "성남fc",
                "수원fc",
                "인천utd",
                "인천유나이티드",
                "강원fc",
                "fc서울",
                "fc안양",
                "부천fc",
                "대구fc",
                "광주fc",
                "경남fc",
                "제주utd",
                "제주유나이티드",
                "서울e"
        };

        return Settings.builder()
                .put("index.analysis.analyzer.korean.type", "custom")
                .put("index.analysis.analyzer.korean.tokenizer", "seunjeon_default_tokenizer")
                .putArray("index.analysis.tokenizer.seunjeon_default_tokenizer.user_words", userWords)
                .put("index.analysis.tokenizer.seunjeon_default_tokenizer.type", "seunjeon_tokenizer")
                .put("index.analysis.tokenizer.seunjeon_default_tokenizer.pos_tagging", false)
                .put("index.analysis.tokenizer.seunjeon_default_tokenizer.decompound", true)
                .putArray("index.analysis.tokenizer.seunjeon_default_tokenizer.index_poses",
                        "N", "SL", "SH", "SN", "XR", "V", "UNK", "I", "M");
    }

    private String getBoardFreeMappings() throws JsonProcessingException {

        ObjectMapper objectMapper = ObjectMapperUtils.getObjectMapper();

        JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;

        ObjectNode propertiesNode = jsonNodeFactory.objectNode();

        propertiesNode.set("id",
                jsonNodeFactory.objectNode()
                        .put("type", "string"));

        propertiesNode.set("subject",
                jsonNodeFactory.objectNode()
                        .put("type", "string")
                        .put("analyzer", "korean")
        );

        propertiesNode.set("content",
                jsonNodeFactory.objectNode()
                        .put("type", "string")
                        .put("analyzer", "korean")
        );

        propertiesNode.set("seq",
                jsonNodeFactory.objectNode()
                        .put("type", "integer")
                        .put("index", "no")
        );

        propertiesNode.set("category",
                jsonNodeFactory.objectNode()
                        .put("type", "string")
                        .put("index", "not_analyzed")
        );

        propertiesNode.set("galleries",
                jsonNodeFactory.objectNode()
                        .put("type", "string")
                        .put("index", "no")
        );

        ObjectNode writerNode = jsonNodeFactory.objectNode();
        writerNode.set("providerId", jsonNodeFactory.objectNode().put("type", "string").put("index", "no"));
        writerNode.set("userId", jsonNodeFactory.objectNode().put("type", "string").put("index", "no"));
        writerNode.set("username", jsonNodeFactory.objectNode().put("type", "string").put("index", "no"));
        propertiesNode.set("writer", jsonNodeFactory.objectNode().set("properties", writerNode));

        propertiesNode.set("registerDate",
                jsonNodeFactory.objectNode()
                        .put("type", "date")
        );

        ObjectNode mappings = jsonNodeFactory.objectNode();
        mappings.set("properties", propertiesNode);

        return objectMapper.writeValueAsString(mappings);
    }

    private String getBoardFreeCommentMappings() throws JsonProcessingException {
        ObjectMapper objectMapper = ObjectMapperUtils.getObjectMapper();

        ObjectNode idNode = objectMapper.createObjectNode();
        idNode.put("type", "string");

        ObjectNode contentNode = objectMapper.createObjectNode();
        contentNode.put("type", "string");
        contentNode.put("analyzer", "korean");

        // boardItem
        ObjectNode boardItemIdNode = objectMapper.createObjectNode();
        boardItemIdNode.put("type", "string");
        boardItemIdNode.put("index", "no");

        ObjectNode boardItemSeqNode = objectMapper.createObjectNode();
        boardItemSeqNode.put("type", "integer");
        boardItemSeqNode.put("index", "no");

        ObjectNode boardItemPropertiesNode = objectMapper.createObjectNode();
        boardItemPropertiesNode.set("id", boardItemIdNode);
        boardItemPropertiesNode.set("seq", boardItemSeqNode);

        ObjectNode boardItemNode = objectMapper.createObjectNode();
        boardItemNode.set("properties", boardItemPropertiesNode);

        // writer
        ObjectNode writerProviderIdNode = objectMapper.createObjectNode();
        writerProviderIdNode.put("type", "string");
        writerProviderIdNode.put("index", "no");

        ObjectNode writerUserIdNode = objectMapper.createObjectNode();
        writerUserIdNode.put("type", "string");
        writerUserIdNode.put("index", "no");

        ObjectNode writerUsernameNode = objectMapper.createObjectNode();
        writerUsernameNode.put("type", "string");
        writerUsernameNode.put("index", "no");

        ObjectNode writerPropertiesNode = objectMapper.createObjectNode();
        writerPropertiesNode.set("providerId", writerProviderIdNode);
        writerPropertiesNode.set("userId", writerUserIdNode);
        writerPropertiesNode.set("username", writerUsernameNode);

        ObjectNode writerNode = objectMapper.createObjectNode();
        writerNode.set("properties", writerPropertiesNode);

        // properties
        ObjectNode propertiesNode = objectMapper.createObjectNode();
        propertiesNode.set("id", idNode);
        propertiesNode.set("content", contentNode);
        propertiesNode.set("writer", writerNode);
        propertiesNode.set("boardItem", boardItemNode);

        ObjectNode parentNode = objectMapper.createObjectNode();
        parentNode.put("type", CoreConst.ES_TYPE_BOARD);

        ObjectNode mappings = objectMapper.createObjectNode();
        mappings.set("_parent", parentNode);
        mappings.set("properties", propertiesNode);

        return objectMapper.writeValueAsString(mappings);
    }

    private String getGalleryMappings() throws JsonProcessingException {
        ObjectMapper objectMapper = ObjectMapperUtils.getObjectMapper();

        ObjectNode idNode = objectMapper.createObjectNode();
        idNode.put("type", "string");

        ObjectNode nameNode = objectMapper.createObjectNode();
        nameNode.put("type", "string");
        nameNode.put("analyzer", "korean");

        // writer
        ObjectNode writerProviderIdNode = objectMapper.createObjectNode();
        writerProviderIdNode.put("type", "string");
        writerProviderIdNode.put("index", "no");

        ObjectNode writerUserIdNode = objectMapper.createObjectNode();
        writerUserIdNode.put("type", "string");
        writerUserIdNode.put("index", "no");

        ObjectNode writerUsernameNode = objectMapper.createObjectNode();
        writerUsernameNode.put("type", "string");
        writerUsernameNode.put("index", "no");

        ObjectNode writerPropertiesNode = objectMapper.createObjectNode();
        writerPropertiesNode.set("providerId", writerProviderIdNode);
        writerPropertiesNode.set("userId", writerUserIdNode);
        writerPropertiesNode.set("username", writerUsernameNode);

        ObjectNode writerNode = objectMapper.createObjectNode();
        writerNode.set("properties", writerPropertiesNode);

        // properties
        ObjectNode propertiesNode = objectMapper.createObjectNode();
        propertiesNode.set("id", idNode);
        propertiesNode.set("name", nameNode);
        propertiesNode.set("writer", writerNode);

        ObjectNode mappings = objectMapper.createObjectNode();
        mappings.set("properties", propertiesNode);

        return objectMapper.writeValueAsString(mappings);
    }

    private String getSearchWordMappings() throws JsonProcessingException {
        ObjectMapper objectMapper = ObjectMapperUtils.getObjectMapper();

        JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;

        ObjectNode propertiesNode = jsonNodeFactory.objectNode();

        propertiesNode.set("id",
                jsonNodeFactory.objectNode()
                        .put("type", "string"));

        propertiesNode.set("word",
                jsonNodeFactory.objectNode()
                        .put("type", "string")
                        .put("index", "not_analyzed")
        );

        ObjectNode writerNode = jsonNodeFactory.objectNode();
        writerNode.set("providerId", jsonNodeFactory.objectNode().put("type", "string").put("index", "no"));
        writerNode.set("userId", jsonNodeFactory.objectNode().put("type", "string").put("index", "no"));
        writerNode.set("username", jsonNodeFactory.objectNode().put("type", "string").put("index", "no"));
        propertiesNode.set("writer", jsonNodeFactory.objectNode().set("properties", writerNode));

        propertiesNode.set("registerDate",
                jsonNodeFactory.objectNode()
                        .put("type", "date")
        );

        ObjectNode mappings = jsonNodeFactory.objectNode();
        mappings.set("properties", propertiesNode);

        return objectMapper.writeValueAsString(mappings);
    }
}
