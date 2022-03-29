package com.jakduk.api.search;

import com.jakduk.api.TestMvcConfig;
import com.jakduk.api.common.Constants;
import com.jakduk.api.common.board.category.BoardCategory;
import com.jakduk.api.common.board.category.BoardCategoryGenerator;
import com.jakduk.api.common.rabbitmq.RabbitMQPublisher;
import com.jakduk.api.common.util.JakdukUtils;
import com.jakduk.api.common.util.ObjectMapperUtils;
import com.jakduk.api.mock.WithMockJakdukUser;
import com.jakduk.api.model.elasticsearch.EsCommentSource;
import com.jakduk.api.model.elasticsearch.EsGallerySource;
import com.jakduk.api.model.elasticsearch.EsParentArticle;
import com.jakduk.api.model.elasticsearch.EsTermsBucket;
import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.restcontroller.SearchRestController;
import com.jakduk.api.restcontroller.vo.board.BoardGallerySimple;
import com.jakduk.api.restcontroller.vo.search.*;
import com.jakduk.api.service.SearchService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SearchRestController.class)
@Import({TestMvcConfig.class})
@AutoConfigureRestDocs
public class SearchMvcTests {

    @Autowired
    private MockMvc mvc;

    @MockBean private SearchService searchService;

    @MockBean private RabbitMQPublisher rabbitMQPublisher;
    @MockBean private UserDetailsService userDetailsService;

    private static CommonWriter commonWriter;
    private static List<BoardCategory> categories;
    private static BoardCategory boardCategory;
    private static List<BoardGallerySimple> simpleGalleries;

    @BeforeAll
    public static void setUp(){
        commonWriter = new CommonWriter();
        commonWriter.setUserId("571ccf50ccbfc325b20711c5");
        commonWriter.setUsername("test07");
        commonWriter.setProviderId(Constants.ACCOUNT_TYPE.JAKDUK);

        categories = BoardCategoryGenerator.getCategories(Constants.BOARD_TYPE.FOOTBALL, JakdukUtils.getLocale());

        boardCategory = categories.get(0);

        simpleGalleries = Arrays.asList(
                new BoardGallerySimple() {{
                    setId("58b9050b807d714eaf50a111");
                    setThumbnailUrl("https://dev-api.jakduk.com//gallery/thumbnail/58b9050b807d714eaf50a111");
                }}
        );
    }

    @Test
    @WithMockJakdukUser
    public void searchUnifiedTest() throws Exception {

        SearchArticleResult searchArticleResult = new SearchArticleResult();
        searchArticleResult.setTook(330L);
        searchArticleResult.setTotalCount(9L);
        searchArticleResult.setArticles(
                Arrays.asList(
                        new ArticleSource() {
                            {
                                setId("58b7b9dd716dce06b10e449a");
                                setSeq(2);
                                setBoard(Constants.BOARD_TYPE.FOOTBALL.name());
                                setCategory(boardCategory.getCode());
                                setWriter(commonWriter);
                                setGalleries(simpleGalleries);
                                setScore(4.9219737F);
                                setHighlight(
                                        new HashMap<String, List<String>>() {{
                                            put("subject", Arrays.asList("Summernote insert-image <div class=\\\"description\\\">테스트</div>"));
                                            put("content", Arrays.asList("♪ 오 잘된다"));
                                        }}
                                );
                            }})
        );

        SearchCommentResult searchCommentResult = new SearchCommentResult();
        searchCommentResult.setTook(31L);
        searchCommentResult.setTotalCount(47L);
        searchCommentResult.setComments(
                        Arrays.asList(
                                new EsCommentSource(){{
                                    setId("578cd13e807d7113f246fa2e");
                                    setArticle(
                                            new EsParentArticle(){{
                                                setId("578cd105807d7113f246fa2d");
                                                setSeq(88);
                                                setBoard(Constants.BOARD_TYPE.FOOTBALL.name());
                                                setCategory(boardCategory.getCode());
                                                setSubject("글 제목입니다.");
                                            }}
                                    );
                                    setWriter(commonWriter);
                                    setScore(2.2118008F);
                                    setHighlight(
                                            new HashMap<String, List<String>>(){{
                                                put("content", Arrays.asList("<div class=\\\"description\\\">테스트</div> 합니다. ㅎㅎ"));
                                            }}
                                    );
                                }}
                        ));

        SearchGalleryResult searchGalleryResult = new SearchGalleryResult();
        searchGalleryResult.setTook(16L);
        searchGalleryResult.setTotalCount(5L);
        searchGalleryResult.setGalleries(
                        Arrays.asList(
                                new EsGallerySource() {{
                                    setId("55d94c23e4b08bb591207f08");
                                    setWriter(commonWriter);
                                    setScore(4.4011974F);
                                    setHighlight(
                                            new HashMap<String, List<String>>(){{
                                                put("name", Arrays.asList("shortContent와 썸네일URL 추가 <div class=\\\"description\\\">테스트</div>"));
                                            }}
                                    );
                                }}
                        )
                );

        SearchUnifiedResponse expectResponse = new SearchUnifiedResponse();
        expectResponse.setArticleResult(searchArticleResult);
        expectResponse.setCommentResult(searchCommentResult);
        expectResponse.setGalleryResult(searchGalleryResult);

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
                                        fieldWithPath("articleResult.articles.[].id").type(JsonFieldType.STRING).description("글 ID"),
                                        fieldWithPath("articleResult.articles.[].seq").type(JsonFieldType.NUMBER).description("글번호"),
                                        fieldWithPath("articleResult.articles.[].board").type(JsonFieldType.STRING).description("게시판"),
                                        fieldWithPath("articleResult.articles.[].category").type(JsonFieldType.STRING).description("말머리"),
                                        subsectionWithPath("articleResult.articles.[].writer").type(JsonFieldType.OBJECT).description("글쓴이"),
                                        subsectionWithPath("articleResult.articles.[].galleries").type(JsonFieldType.ARRAY).description("그림 목록"),
                                        fieldWithPath("articleResult.articles.[].score").type(JsonFieldType.NUMBER).description("매칭 점수"),
                                        subsectionWithPath("articleResult.articles.[].highlight").type(JsonFieldType.OBJECT).description("매칭 단어 하이라이트"),
                                        fieldWithPath("commentResult").type(JsonFieldType.OBJECT).description("매칭된 댓글 객체"),
                                        fieldWithPath("commentResult.took").type(JsonFieldType.NUMBER).description("찾는데 걸린 시간(ms)"),
                                        fieldWithPath("commentResult.totalCount").type(JsonFieldType.NUMBER).description("매칭되는 아이템 수"),
                                        fieldWithPath("commentResult.comments").type(JsonFieldType.ARRAY).description("매칭되는 댓글 목록"),
                                        fieldWithPath("commentResult.comments.[].id").type(JsonFieldType.STRING).description("댓글 ID"),
                                        subsectionWithPath("commentResult.comments.[].article").type(JsonFieldType.OBJECT).description("연관 글 객체"),
                                        subsectionWithPath("commentResult.comments.[].writer").type(JsonFieldType.OBJECT).description("글쓴이"),
                                        fieldWithPath("commentResult.comments.[].score").type(JsonFieldType.NUMBER).description("매칭 점수"),
                                        subsectionWithPath("commentResult.comments.[].highlight").type(JsonFieldType.OBJECT).description("매칭 단어 하이라이트"),
                                        fieldWithPath("galleryResult").type(JsonFieldType.OBJECT).description("매칭된 그림 객체"),
                                        fieldWithPath("galleryResult.took").type(JsonFieldType.NUMBER).description("찾는데 걸린 시간(ms)"),
                                        fieldWithPath("galleryResult.totalCount").type(JsonFieldType.NUMBER).description("매칭되는 아이템 수"),
                                        subsectionWithPath("galleryResult.galleries").type(JsonFieldType.ARRAY).description("매칭되는 그림 목록"),
                                        fieldWithPath("galleryResult.galleries.[].id").type(JsonFieldType.STRING).description("그림 ID"),
                                        fieldWithPath("galleryResult.galleries.[].writer").type(JsonFieldType.OBJECT).description("글쓴이"),
                                        fieldWithPath("galleryResult.galleries.[].score").type(JsonFieldType.NUMBER).description("매칭 점수"),
                                        subsectionWithPath("galleryResult.galleries.[].highlight").type(JsonFieldType.OBJECT).description("매칭 단어 하이라이트")
                                )
                        ));
    }

    @Test
    @WithMockUser
    public void searchPopularWordsTest() throws Exception {

        PopularSearchWordResult expectResponse = new PopularSearchWordResult();
        expectResponse.setTook(18L);
        expectResponse.setPopularSearchWords(
                Arrays.asList(
                        new EsTermsBucket("제목입니다", 33L),
                        new EsTermsBucket("test", 10L),
                        new EsTermsBucket("축구", 21L)
                ));

        when(searchService.aggregateSearchWord(any(LocalDate.class), anyInt()))
                .thenReturn(expectResponse);

        mvc.perform(
                get("/api/search/popular-words")
                        .param("size", "5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(expectResponse)))
                .andDo(
                        document("search-popular-words",
                                requestParameters(
                                        parameterWithName("size").description("(default 5) 반환 개수")
                                ),
                                responseFields(
                                        fieldWithPath("took").type(JsonFieldType.NUMBER).description("찾는데 걸린 시간(ms)"),
                                        fieldWithPath("popularSearchWords").type(JsonFieldType.ARRAY).description("인기 검색어 목록"),
                                        fieldWithPath("popularSearchWords.[].key").type(JsonFieldType.STRING).description("검색어"),
                                        fieldWithPath("popularSearchWords.[].count").type(JsonFieldType.NUMBER).description("검색수")
                                )
                        ));
    }

}
