package com.jakduk.api.search;

import com.jakduk.api.common.Constants;
import com.jakduk.api.common.board.category.BoardCategory;
import com.jakduk.api.common.board.category.BoardCategoryGenerator;
import com.jakduk.api.common.rabbitmq.RabbitMQPublisher;
import com.jakduk.api.common.util.JakdukUtils;
import com.jakduk.api.common.util.ObjectMapperUtils;
import com.jakduk.api.model.elasticsearch.EsCommentSource;
import com.jakduk.api.model.elasticsearch.EsGallerySource;
import com.jakduk.api.model.elasticsearch.EsParentArticle;
import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.restcontroller.SearchRestController;
import com.jakduk.api.restcontroller.vo.board.BoardGallerySimple;
import com.jakduk.api.restcontroller.vo.search.*;
import com.jakduk.api.service.SearchService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(SearchRestController.class)
@AutoConfigureRestDocs(outputDir = "build/snippets")
public class SearchMvcTests {

    @Autowired
    private MockMvc mvc;

    @MockBean private RestTemplateBuilder restTemplateBuilder;
    @MockBean private RabbitMQPublisher rabbitMQPublisher;
    @MockBean private SearchService searchService;

    private CommonWriter commonWriter;
    private List<BoardCategory> categories;
    private BoardCategory boardCategory;
    private List<BoardGallerySimple> simpleGalleries;

    @Before
    public void setUp(){
        commonWriter = CommonWriter.builder()
                .userId("571ccf50ccbfc325b20711c5")
                .username("test07")
                .providerId(Constants.ACCOUNT_TYPE.JAKDUK)
                .build();

        categories = BoardCategoryGenerator.getCategories(Constants.BOARD_TYPE.FOOTBALL, JakdukUtils.getLocale());

        boardCategory = categories.get(0);

        simpleGalleries = Arrays.asList(
                new BoardGallerySimple("58b9050b807d714eaf50a111", "https://dev-api.jakduk.com//gallery/thumbnail/58b9050b807d714eaf50a111"));
    }

    @Test
    @WithMockUser
    public void searchUnifiedTest() throws Exception {

        SearchArticleResult searchArticleResult = SearchArticleResult.builder()
                .took(330L)
                .totalCount(9L)
                .articles(
                        Arrays.asList(
                                ArticleSource.builder()
                                        .id("58b7b9dd716dce06b10e449a")
                                        .seq(2)
                                        .board(Constants.BOARD_TYPE.FOOTBALL.name())
                                        .category(boardCategory.getCode())
                                        .writer(commonWriter)
                                        .galleries(simpleGalleries)
                                        .score(4.9219737F)
                                        .highlight(
                                                new HashMap<String, List<String>>(){{
                                                    put("subject", Arrays.asList("Summernote insert-image <div class=\\\"description\\\">테스트</div>"));
                                                    put("content", Arrays.asList("♪ 오 잘된다"));
                                                }})
                                        .build()
                        ))
                .build();

        SearchCommentResult searchCommentResult = SearchCommentResult.builder()
                .took(31L)
                .totalCount(47L)
                .comments(
                        Arrays.asList(
                                EsCommentSource.builder()
                                        .id("578cd13e807d7113f246fa2e")
                                        .article(
                                                EsParentArticle.builder()
                                                        .id("578cd105807d7113f246fa2d")
                                                        .seq(88)
                                                        .board(Constants.BOARD_TYPE.FOOTBALL.name())
                                                        .category(boardCategory.getCode())
                                                        .subject("글 제목입니다.")
                                                        .build()
                                        )
                                        .writer(commonWriter)
                                        .score(2.2118008F)
                                        .highlight(
                                                new HashMap<String, List<String>>(){{
                                                    put("content", Arrays.asList("<div class=\\\"description\\\">테스트</div> 합니다. ㅎㅎ"));
                                                }})
                                .build()
                        ))
                .build();

        SearchGalleryResult searchGalleryResult = SearchGalleryResult.builder()
                .took(16L)
                .totalCount(5L)
                .galleries(
                        Arrays.asList(
                                EsGallerySource.builder()
                                        .id("55d94c23e4b08bb591207f08")
                                        .writer(commonWriter)
                                        .score(4.4011974F)
                                        .highlight(
                                                new HashMap<String, List<String>>(){{
                                                    put("name", Arrays.asList("shortContent와 썸네일URL 추가 <div class=\\\"description\\\">테스트</div>"));
                                                }})
                                .build()
                        )
                )
                .build();

        SearchUnifiedResponse expectResponse = SearchUnifiedResponse.builder()
                .articleResult(searchArticleResult)
                .commentResult(searchCommentResult)
                .galleryResult(searchGalleryResult)
                .build();

        when(searchService.searchUnified(anyString(), anyString(), anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(expectResponse);

        mvc.perform(
                get("/api/search")
                        .param("q", "test")
                        .param("w", "ARTICLE;COMMENT;GALLERY")
                        .param("from", "0")
                        .param("size", "10")
                        .param("tag", "div")
                        .param("styleClass", "description")
                        .header("Cookie", "JSESSIONID=3F0E029648484BEAEF6B5C3578164E99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(expectResponse)))
                .andDo(
                        document("search-unified",
                                requestHeaders(
                                        headerWithName("Cookie").description("(optional) 인증 쿠키. value는 JSESSIONID=키값")
                                ),
                                requestParameters(
                                        parameterWithName("q").description("검색어"),
                                        parameterWithName("w").description("검색 범위. ARTICLE;COMMENT;GALLERY"),
                                        parameterWithName("from").description("(default 0) 페이지 시작 위치"),
                                        parameterWithName("size").description("(default 10) 페이지 크기"),
                                        parameterWithName("tag").description("(default em) 하이라이트의 HTML 태그"),
                                        parameterWithName("styleClass").description("(default 없음) 하이라이트의 HTML 태그 클래스")
                                ),
                                responseFields(
                                        fieldWithPath("articleResult").type(JsonFieldType.OBJECT).description("매칭된 글 객체"),
                                        fieldWithPath("articleResult.took").type(JsonFieldType.NUMBER).description("찾는데 걸린 시간(ms)"),
                                        fieldWithPath("articleResult.totalCount").type(JsonFieldType.NUMBER).description("매칭되는 아이템 수"),
                                        fieldWithPath("articleResult.articles").type(JsonFieldType.ARRAY).description("매칭되는 게시물 목록"),
                                        fieldWithPath("articleResult.articles").type(JsonFieldType.ARRAY).description("매칭되는 게시물 목록"),
                                        fieldWithPath("articleResult.articles.[].id").type(JsonFieldType.STRING).description("글 ID"),
                                        fieldWithPath("articleResult.articles.[].seq").type(JsonFieldType.NUMBER).description("글번호"),
                                        fieldWithPath("articleResult.articles.[].board").type(JsonFieldType.STRING).description("게시판"),
                                        fieldWithPath("articleResult.articles.[].category").type(JsonFieldType.STRING).description("말머리"),
                                        fieldWithPath("articleResult.articles.[].writer").type(JsonFieldType.OBJECT).description("글쓴이"),
                                        fieldWithPath("articleResult.articles.[].galleries").type(JsonFieldType.ARRAY).description("그림 목록"),
                                        fieldWithPath("articleResult.articles.[].score").type(JsonFieldType.NUMBER).description("매칭 점수"),
                                        fieldWithPath("articleResult.articles.[].highlight").type(JsonFieldType.OBJECT).description("매칭 단어 하이라이트"),
                                        fieldWithPath("commentResult").type(JsonFieldType.OBJECT).description("매칭된 댓글 객체"),
                                        fieldWithPath("commentResult.took").type(JsonFieldType.NUMBER).description("찾는데 걸린 시간(ms)"),
                                        fieldWithPath("commentResult.totalCount").type(JsonFieldType.NUMBER).description("매칭되는 아이템 수"),
                                        fieldWithPath("commentResult.comments").type(JsonFieldType.ARRAY).description("매칭되는 댓글 목록"),
                                        fieldWithPath("commentResult.comments.[].id").type(JsonFieldType.STRING).description("댓글 ID"),
                                        fieldWithPath("commentResult.comments.[].article").type(JsonFieldType.OBJECT).description("연관 글 객체"),
                                        fieldWithPath("commentResult.comments.[].writer").type(JsonFieldType.OBJECT).description("글쓴이"),
                                        fieldWithPath("commentResult.comments.[].score").type(JsonFieldType.NUMBER).description("매칭 점수"),
                                        fieldWithPath("commentResult.comments.[].highlight").type(JsonFieldType.OBJECT).description("매칭 단어 하이라이트"),
                                        fieldWithPath("galleryResult").type(JsonFieldType.OBJECT).description("매칭된 그림 객체"),
                                        fieldWithPath("galleryResult.took").type(JsonFieldType.NUMBER).description("찾는데 걸린 시간(ms)"),
                                        fieldWithPath("galleryResult.totalCount").type(JsonFieldType.NUMBER).description("매칭되는 아이템 수"),
                                        fieldWithPath("galleryResult.galleries").type(JsonFieldType.ARRAY).description("매칭되는 그림 목록"),
                                        fieldWithPath("galleryResult.galleries.[].id").type(JsonFieldType.STRING).description("그림 ID"),
                                        fieldWithPath("galleryResult.galleries.[].writer").type(JsonFieldType.OBJECT).description("글쓴이"),
                                        fieldWithPath("galleryResult.galleries.[].score").type(JsonFieldType.NUMBER).description("매칭 점수"),
                                        fieldWithPath("galleryResult.galleries.[].highlight").type(JsonFieldType.OBJECT).description("매칭 단어 하이라이트")
                                )
                        ));

    }

}
