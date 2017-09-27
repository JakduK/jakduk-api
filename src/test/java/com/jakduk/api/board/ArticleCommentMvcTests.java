package com.jakduk.api.board;

import com.jakduk.api.TestMvcConfig;
import com.jakduk.api.common.AuthHelper;
import com.jakduk.api.common.Constants;
import com.jakduk.api.common.board.category.BoardCategory;
import com.jakduk.api.common.board.category.BoardCategoryGenerator;
import com.jakduk.api.common.util.JakdukUtils;
import com.jakduk.api.common.util.ObjectMapperUtils;
import com.jakduk.api.model.db.Article;
import com.jakduk.api.model.embedded.ArticleCommentStatus;
import com.jakduk.api.model.embedded.ArticleStatus;
import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.model.simple.ArticleOnSearch;
import com.jakduk.api.restcontroller.BoardRestController;
import com.jakduk.api.restcontroller.vo.board.GetArticleComment;
import com.jakduk.api.restcontroller.vo.board.GetArticleCommentsResponse;
import com.jakduk.api.service.ArticleService;
import com.jakduk.api.service.GalleryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BoardRestController.class)
@Import(TestMvcConfig.class)
@AutoConfigureRestDocs(outputDir = "build/snippets")
public class ArticleCommentMvcTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RestTemplateBuilder restTemplateBuilder;
    @MockBean private ArticleService articleService;
    @MockBean private GalleryService galleryService;
    @MockBean private AuthHelper authHelper;

    private CommonWriter commonWriter;
    private Article article;
    private BoardCategory boardCategory;
    private Map<String, String> categoriesMap;

    @Before
    public void setUp(){
        commonWriter = CommonWriter.builder()
                .userId("userid01")
                .username("user01")
                .providerId(Constants.ACCOUNT_TYPE.JAKDUK)
                .build();

        article = Article.builder()
                .id("boardFreeId01")
                .seq(1)
                .writer(commonWriter)
                .subject("제목입니다.")
                .content("내용입니다.")
                .board(Constants.BOARD_TYPE.FOOTBALL.name())
                .category("CLASSIC")
                .linkedGallery(true)
                .status(new ArticleStatus(false, false, Constants.DEVICE_TYPE.NORMAL))
                .build();

        List<BoardCategory> categories = BoardCategoryGenerator.getCategories(Constants.BOARD_TYPE.FOOTBALL, JakdukUtils.getLocale());

        boardCategory = categories.get(0);

        categoriesMap = categories.stream()
                .collect(Collectors.toMap(BoardCategory::getCode, boardCategory -> boardCategory.getNames().get(0).getName()));

        categoriesMap.put("ALL", JakdukUtils.getResourceBundleMessage("messages.board", "board.category.all"));
    }

    @Test
    @WithMockUser
    public void getArticleCommentsTest() throws Exception {

        GetArticleCommentsResponse expectResponse = GetArticleCommentsResponse.builder()
                .comments(
                        Arrays.asList(
                                GetArticleComment.builder()
                                        .id("54b5058c3d96b205dc7e2809")
                                        .article(
                                                ArticleOnSearch.builder()
                                                        .id(article.getId())
                                                        .seq(article.getSeq())
                                                        .subject(article.getSubject())
                                                        .build())
                                        .writer(commonWriter)
                                        .content("댓글 내용입니다.")
                                        .status(new ArticleCommentStatus(Constants.DEVICE_TYPE.NORMAL))
                                        .numberOfLike(5)
                                        .numberOfDislike(3)
                                        .build()
                        )
                )
                .last(true)
                .first(true)
                .totalPages(10)
                .size(20)
                .number(0)
                .numberOfElements(20)
                .totalElements(201L)
                .build();

        when(articleService.getArticleComments(any(CommonWriter.class), anyString(), anyInt(), anyInt()))
                .thenReturn(expectResponse);

        mvc.perform(
                get("/api/board/{board}/comments", Constants.BOARD_TYPE.FOOTBALL.name().toLowerCase())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(expectResponse)))
                .andDo(
                        document("getArticleComments",
                                pathParameters(
                                        parameterWithName("board").description("게시판 " +
                                                Stream.of(Constants.BOARD_TYPE.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.toList()))
                                ),
                                requestParameters(
                                        parameterWithName("page").description("(optional, default 1) 페이지 번호. 1부터 시작.").optional(),
                                        parameterWithName("size").description("(optional, default 20) 페이지 크기.").optional()
                                ),
                                responseFields(
                                        fieldWithPath("comments").type(JsonFieldType.ARRAY).description("댓글 목록"),
                                        fieldWithPath("comments.[].id").type(JsonFieldType.STRING).description("댓글 ID"),
                                        fieldWithPath("comments.[].article").type(JsonFieldType.OBJECT).description("연동 글"),
                                        fieldWithPath("comments.[].writer").type(JsonFieldType.OBJECT).description("글쓴이"),
                                        fieldWithPath("comments.[].content").type(JsonFieldType.STRING).description("댓글 내용"),
                                        fieldWithPath("comments.[].status").type(JsonFieldType.OBJECT).description("댓글 상태"),
                                        fieldWithPath("comments.[].numberOfLike").type(JsonFieldType.NUMBER).description("좋아요 수"),
                                        fieldWithPath("comments.[].numberOfDislike").type(JsonFieldType.NUMBER).description("싫어요 수"),
                                        fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                                        fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
                                        fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수"),
                                        fieldWithPath("size").type(JsonFieldType.NUMBER).description("페이지당 글 수"),
                                        fieldWithPath("number").type(JsonFieldType.NUMBER).description("현재 페이지(0부터 시작)"),
                                        fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("현제 페이지에서 글 수"),
                                        fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("전체 글 수")
                                )
                        ));

    }

}
