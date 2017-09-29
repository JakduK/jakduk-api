package com.jakduk.api.board;

import com.jakduk.api.TestMvcConfig;
import com.jakduk.api.common.AuthHelper;
import com.jakduk.api.common.Constants;
import com.jakduk.api.common.board.category.BoardCategory;
import com.jakduk.api.common.board.category.BoardCategoryGenerator;
import com.jakduk.api.common.util.DateUtils;
import com.jakduk.api.common.util.JakdukUtils;
import com.jakduk.api.common.util.ObjectMapperUtils;
import com.jakduk.api.model.db.Article;
import com.jakduk.api.model.db.ArticleComment;
import com.jakduk.api.model.embedded.*;
import com.jakduk.api.model.simple.ArticleSimple;
import com.jakduk.api.restcontroller.BoardRestController;
import com.jakduk.api.restcontroller.vo.board.*;
import com.jakduk.api.service.ArticleService;
import com.jakduk.api.service.GalleryService;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
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
    private ArticleComment articleComment;
    private ArticleSimple articleSimple;
    private List<BoardCategory> categories;
    private BoardCategory boardCategory;
    private List<GetArticleComment> articleComments;

    @Before
    public void setUp(){
        commonWriter = CommonWriter.builder()
                .userId("571ccf50ccbfc325b20711c5")
                .username("test07")
                .providerId(Constants.ACCOUNT_TYPE.JAKDUK)
                .build();

        List<CommonFeelingUser> commonFeelingUsers = Arrays.asList(new CommonFeelingUser("58ee4993807d713fa7735f1d", "566d68d5e4b0dfaaa5b98685", "test05"));

        categories = BoardCategoryGenerator.getCategories(Constants.BOARD_TYPE.FOOTBALL, JakdukUtils.getLocale());

        boardCategory = categories.get(0);

        article = Article.builder()
                .id("59c8879fa2b594c5d33e6ac4")
                .seq(2)
                .writer(commonWriter)
                .subject("글 제목입니다.")
                .content("내용입니다. 아주 길 수도 있음.")
                .board(Constants.BOARD_TYPE.FOOTBALL.name())
                .category(boardCategory.getCode())
                .views(15)
                .usersLiking(commonFeelingUsers)
                .usersDisliking(commonFeelingUsers)
                .status(new ArticleStatus(false, false, Constants.DEVICE_TYPE.NORMAL))
                .logs(Arrays.asList(new BoardLog("58e9959b807d71113a999c6d", Constants.ARTICLE_LOG_TYPE.CREATE.name(), new SimpleWriter("58ee4993807d713fa7735f1d", "test05"))))
                .shortContent("본문입니다. (100자)")
                .lastUpdated(LocalDateTime.parse("2017-09-27T23:42:44.810"))
                .linkedGallery(true)
                .build();

        articleComment = ArticleComment.builder()
                .id("54b5058c3d96b205dc7e2809")
                .article(new ArticleItem(article.getId(), article.getSeq(), article.getBoard()))
                .status(new ArticleCommentStatus(Constants.DEVICE_TYPE.NORMAL))
                .writer(commonWriter)
                .content("댓글 내용입니다.")
                .usersLiking(commonFeelingUsers)
                .usersDisliking(commonFeelingUsers)
                .linkedGallery(true)
                .logs(Arrays.asList(new BoardLog("58e9959b807d71113a999c6d", Constants.ARTICLE_COMMENT_LOG_TYPE.CREATE.name(), new SimpleWriter("58ee4993807d713fa7735f1d", "test05"))))
                .build();

        List<ArticleGallery> articleGalleries = Arrays.asList(
                ArticleGallery.builder()
                        .id("58b9050b807d714eaf50a111")
                        .name("공차는사진")
                        .imageUrl("https://dev-api.jakduk.com//gallery/58b9050b807d714eaf50a111")
                        .thumbnailUrl("https://dev-api.jakduk.com//gallery/thumbnail/58b9050b807d714eaf50a111")
                        .build()
        );

        articleSimple = new ArticleSimple();
        BeanUtils.copyProperties(article, articleSimple);

        GetArticleComment getArticleComment = new GetArticleComment();
        ArticleSimple articleSimple = new ArticleSimple();
        BeanUtils.copyProperties(articleComment, getArticleComment);
        BeanUtils.copyProperties(article, articleSimple);

        getArticleComment.setArticle(articleSimple);
        getArticleComment.setNumberOfLike(articleComment.getUsersLiking().size());
        getArticleComment.setNumberOfDislike(articleComment.getUsersDisliking().size());
        getArticleComment.setMyFeeling(Constants.FEELING_TYPE.LIKE);
        getArticleComment.setGalleries(articleGalleries);
        getArticleComment.setLogs(
                articleComment.getLogs().stream()
                        .map(boardLog -> {
                            ArticleCommentLog articleCommentLog = new ArticleCommentLog();
                            BeanUtils.copyProperties(boardLog, articleCommentLog);
                            LocalDateTime timestamp = DateUtils.dateToLocalDateTime(new ObjectId(articleCommentLog.getId()).getDate());
                            articleCommentLog.setType(Constants.ARTICLE_COMMENT_LOG_TYPE.valueOf(boardLog.getType()));
                            articleCommentLog.setTimestamp(timestamp);

                            return articleCommentLog;
                        })
                        .sorted(Comparator.comparing(ArticleCommentLog::getId).reversed())
                        .collect(Collectors.toList())
        );

        articleComments = Arrays.asList(getArticleComment);
    }

    @Test
    @WithMockUser
    public void getArticleCommentsTest() throws Exception {

        GetArticleCommentsResponse expectResponse = GetArticleCommentsResponse.builder()
                .comments(articleComments)
                .last(true)
                .first(true)
                .totalPages(10)
                .size(20)
                .number(0)
                .numberOfElements(20)
                .totalElements(201L)
                .build();

        when(articleService.getArticleComments(any(CommonWriter.class), any(Constants.BOARD_TYPE.class), anyInt(), anyInt()))
                .thenReturn(expectResponse);

        mvc.perform(
                get("/api/board/{board}/comments", Constants.BOARD_TYPE.FOOTBALL.name().toLowerCase())
                        .header("Cookie", "JSESSIONID=3F0E029648484BEAEF6B5C3578164E99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(expectResponse)))
                .andDo(
                        document("getArticleComments",
                                requestHeaders(
                                        headerWithName("Cookie").description("(optional) 인증 쿠키. value는 JESSIONID=키값")
                                ),
                                pathParameters(
                                        parameterWithName("board").description("게시판 " +
                                                Stream.of(Constants.BOARD_TYPE.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.toList()))
                                ),
                                requestParameters(
                                        parameterWithName("page").description("(optional, default 1) 페이지 번호. 1부터 시작.").optional(),
                                        parameterWithName("size").description("(optional, default 20) 페이지 크기.").optional()
                                ),
                                responseFields(
                                        this.getArticleCommentsDescriptor(
                                                fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                                                fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
                                                fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수"),
                                                fieldWithPath("size").type(JsonFieldType.NUMBER).description("페이지당 글 수"),
                                                fieldWithPath("number").type(JsonFieldType.NUMBER).description("현재 페이지(0부터 시작)"),
                                                fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("현제 페이지에서 글 수"),
                                                fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("전체 글 수")
                                        )
                                )
                        ));
    }

    @Test
    @WithMockUser
    public void getArticleDetailCommentsTest() throws Exception {

        GetArticleDetailCommentsResponse expectResponse = GetArticleDetailCommentsResponse.builder()
                .comments(articleComments)
                .count(articleComments.size())
                .build();

        when(articleService.getArticleDetailComments(any(CommonWriter.class), any(Constants.BOARD_TYPE.class), anyInt(), anyString()))
                .thenReturn(expectResponse);

        mvc.perform(
                get("/api/board/{board}/{seq}/comments", Constants.BOARD_TYPE.FOOTBALL.name().toLowerCase(), article.getSeq())
                        .header("Cookie", "JSESSIONID=3F0E029648484BEAEF6B5C3578164E99")
                        .param("commentId", "59c2ec0fbe3eb62dfca3ed9c")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(expectResponse)))
                .andDo(
                        document("getArticleDetailComments",
                                requestHeaders(
                                        headerWithName("Cookie").description("(optional) 인증 쿠키. value는 JESSIONID=키값")
                                ),
                                pathParameters(
                                        parameterWithName("board").description("게시판 " +
                                                Stream.of(Constants.BOARD_TYPE.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.toList())),
                                        parameterWithName("seq").description("글 번호")
                                ),
                                requestParameters(
                                        parameterWithName("commentId").description("(optional) commentId 이후부터 목록 가져옴")
                                ),
                                responseFields(
                                        this.getArticleCommentsDescriptor(fieldWithPath("count").type(JsonFieldType.NUMBER).description("댓글 수"))
                                        )
                                )
                        );
    }

    private List<FieldDescriptor> getArticleCommentsDescriptor(FieldDescriptor... descriptors) {
        List<FieldDescriptor> fieldDescriptors = new ArrayList<>(
                Arrays.asList(
                        fieldWithPath("comments").type(JsonFieldType.ARRAY).description("댓글 목록"),
                        fieldWithPath("comments.[].id").type(JsonFieldType.STRING).description("댓글 ID"),
                        fieldWithPath("comments.[].article").type(JsonFieldType.OBJECT).description("연동 글"),
                        fieldWithPath("comments.[].writer").type(JsonFieldType.OBJECT).description("글쓴이"),
                        fieldWithPath("comments.[].status").type(JsonFieldType.OBJECT).description("댓글 상태"),
                        fieldWithPath("comments.[].content").type(JsonFieldType.STRING).description("댓글 내용"),
                        fieldWithPath("comments.[].numberOfLike").type(JsonFieldType.NUMBER).description("좋아요 수"),
                        fieldWithPath("comments.[].numberOfDislike").type(JsonFieldType.NUMBER).description("싫어요 수"),
                        fieldWithPath("comments.[].myFeeling").type(JsonFieldType.STRING).description("나의 감정 상태. 인증 쿠키가 있고, 감정 표현을 한 경우 포함 된다."),
                        fieldWithPath("comments.[].galleries").type(JsonFieldType.ARRAY).description("그림 목록"),
                        fieldWithPath("comments.[].logs").type(JsonFieldType.ARRAY).description("로그 기록 목록")
                )
        );

        CollectionUtils.mergeArrayIntoCollection(descriptors, fieldDescriptors);

        return fieldDescriptors;
    }

}
