package com.jakduk.api.board;

import com.jakduk.api.TestMvcConfig;
import com.jakduk.api.WithMockJakdukUser;
import com.jakduk.api.common.Constants;
import com.jakduk.api.common.board.category.BoardCategory;
import com.jakduk.api.common.board.category.BoardCategoryGenerator;
import com.jakduk.api.common.rabbitmq.RabbitMQPublisher;
import com.jakduk.api.common.util.DateUtils;
import com.jakduk.api.common.util.JakdukUtils;
import com.jakduk.api.common.util.ObjectMapperUtils;
import com.jakduk.api.model.db.Article;
import com.jakduk.api.model.db.ArticleComment;
import com.jakduk.api.model.db.Gallery;
import com.jakduk.api.model.embedded.*;
import com.jakduk.api.model.simple.ArticleSimple;
import com.jakduk.api.restcontroller.BoardRestController;
import com.jakduk.api.restcontroller.vo.EmptyJsonResponse;
import com.jakduk.api.restcontroller.vo.UserFeelingResponse;
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
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BoardRestController.class)
@Import(TestMvcConfig.class)
@AutoConfigureRestDocs(outputDir = "build/snippets")
public class ArticleCommentMvcTests {

    @Autowired
    private MockMvc mvc;

    @MockBean private RestTemplateBuilder restTemplateBuilder;
    @MockBean private ArticleService articleService;
    @MockBean private GalleryService galleryService;
    @MockBean private RabbitMQPublisher rabbitMQPublisher;

    private CommonWriter commonWriter;
    private Article article;
    private ArticleComment articleComment;
    private ArticleSimple articleSimple;
    private List<BoardCategory> categories;
    private BoardCategory boardCategory;
    private List<GetArticleComment> articleComments;
    private List<Gallery> galleries;
    private WriteArticleComment writeArticleComment;

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
                .status(new ArticleStatus(false, false))
                .logs(Arrays.asList(new BoardLog("58e9959b807d71113a999c6d", Constants.ARTICLE_LOG_TYPE.CREATE.name(), new SimpleWriter("58ee4993807d713fa7735f1d", "test05"))))
                .shortContent("본문입니다. (100자)")
                .lastUpdated(LocalDateTime.parse("2017-09-27T23:42:44.810"))
                .linkedGallery(true)
                .build();

        articleComment = ArticleComment.builder()
                .id("54b5058c3d96b205dc7e2809")
                .article(new ArticleItem(article.getId(), article.getSeq(), article.getBoard()))
                .writer(commonWriter)
                .content("댓글 내용입니다.")
                .usersLiking(commonFeelingUsers)
                .usersDisliking(commonFeelingUsers)
                .linkedGallery(true)
                .logs(Arrays.asList(new BoardLog("58e9959b807d71113a999c6d", Constants.ARTICLE_COMMENT_LOG_TYPE.CREATE.name(), new SimpleWriter("58ee4993807d713fa7735f1d", "test05"))))
                .build();

        List<BoardGallerySimple> articleGalleries = Arrays.asList(
                BoardGallerySimple.builder()
                        .id("58b9050b807d714eaf50a111")
                        .thumbnailUrl("https://dev-api.jakduk.com//gallery/thumbnail/58b9050b807d714eaf50a111")
                        .build()
        );

        articleSimple = new ArticleSimple();
        BeanUtils.copyProperties(article, articleSimple);

        GetArticleComment getArticleComment = new GetArticleComment();
        BeanUtils.copyProperties(articleComment, getArticleComment);

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

        GalleryOnBoard galleryOnBoard = new GalleryOnBoard("59c2945bbe3eb62dfca3ed97", "공차는사진");

        galleries = Arrays.asList(
                Gallery.builder()
                        .id(galleryOnBoard.getId())
                        .name(galleryOnBoard.getName())
                        .fileName("Cat Profile-48.png")
                        .contentType("image/png")
                        .writer(commonWriter)
                        .size(1149L)
                        .fileSize(1870L)
                        .status(new GalleryStatus(Constants.GALLERY_STATUS_TYPE.TEMP))
                        .hash("7eb65b85521d247ab4c5f79e279c03db")
                        .build()
        );

        writeArticleComment = WriteArticleComment.builder()
                .content(articleComment.getContent())
                .galleries(Arrays.asList(galleryOnBoard))
                .build();
    }

    @Test
    @WithMockJakdukUser
    public void getArticleDetailCommentsTest() throws Exception {

        GetArticleDetailCommentsResponse expectResponse = GetArticleDetailCommentsResponse.builder()
                .comments(articleComments)
                .count(articleComments.size())
                .build();

        when(articleService.getArticleDetailComments(any(CommonWriter.class), any(Constants.BOARD_TYPE.class), anyInt(), anyString()))
                .thenReturn(expectResponse);

        mvc.perform(
                get("/api/board/{board}/{seq}/comments", Constants.BOARD_TYPE.FOOTBALL.name().toLowerCase(), article.getSeq())
                        .cookie(new Cookie("JSESSIONID", "3F0E029648484BEAEF6B5C3578164E99"))
                        .param("commentId", "59c2ec0fbe3eb62dfca3ed9c")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(expectResponse)))
                .andDo(
                        document("getArticleDetailComments",
                                requestHeaders(
                                        headerWithName("Cookie").optional().description("(optional) 인증 쿠키. value는 JSESSIONID=키값")
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

    @Test
    @WithMockJakdukUser
    public void writeArticleComment() throws Exception {

        when(galleryService.findByIdIn(any()))
                .thenReturn(galleries);

        when(articleService.insertArticleComment(any(CommonWriter.class), any(Constants.BOARD_TYPE.class), anyInt(), anyString(),
                anyList()))
                .thenReturn(articleComment);

        doNothing().when(galleryService)
                .processLinkedGalleries(anyString(), anyList(), anyList(), anyList(), any(Constants.GALLERY_FROM_TYPE.class), anyString());

        mvc.perform(
                post("/api/board/{board}/{seq}/comment", article.getBoard().toLowerCase(), articleComment.getArticle().getSeq())
                        .cookie(new Cookie("JSESSIONID", "3F0E029648484BEAEF6B5C3578164E99"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(ObjectMapperUtils.writeValueAsString(writeArticleComment)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(articleComment)))
                .andDo(
                        document("writeArticleComment",
                                requestHeaders(
                                        headerWithName("Cookie").optional().description("인증 쿠키. value는 JSESSIONID=키값")
                                ),
                                pathParameters(
                                        parameterWithName("board").description("게시판 " +
                                                Stream.of(Constants.BOARD_TYPE.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.toList())),
                                        parameterWithName("seq").description("글 번호")
                                ),
                                requestFields(this.getWriteArticleCommentFormDescriptor()),
                                responseFields(this.getWriteArticleCommentDescriptor())
                        ));
    }

    @Test
    @WithMockJakdukUser
    public void editArticleCommentTest() throws Exception {

        when(galleryService.findByIdIn(any()))
                .thenReturn(galleries);

        when(articleService.updateArticleComment(any(CommonWriter.class), any(Constants.BOARD_TYPE.class), anyString(), anyString(),
                anyList()))
                .thenReturn(articleComment);

        doNothing().when(galleryService)
                .processLinkedGalleries(anyString(), anyList(), anyList(), anyList(), any(Constants.GALLERY_FROM_TYPE.class), anyString());

        mvc.perform(
                put("/api/board/{board}/comment/{id}", article.getBoard().toLowerCase(), articleComment.getId())
                        .cookie(new Cookie("JSESSIONID", "3F0E029648484BEAEF6B5C3578164E99"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(ObjectMapperUtils.writeValueAsString(writeArticleComment)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(articleComment)))
                .andDo(
                        document("editArticleComment",
                                requestHeaders(
                                        headerWithName("Cookie").optional().description("인증 쿠키. value는 JSESSIONID=키값")
                                ),
                                pathParameters(
                                        parameterWithName("board").description("게시판 " +
                                                Stream.of(Constants.BOARD_TYPE.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.toList())),
                                        parameterWithName("id").description("댓글 ID")
                                ),
                                requestFields(this.getWriteArticleCommentFormDescriptor()),
                                responseFields(this.getWriteArticleCommentDescriptor())
                        ));
    }

    @Test
    @WithMockJakdukUser
    public void deleteArticleCommentTest() throws Exception {

        doNothing().when(articleService)
                .deleteArticleComment(any(CommonWriter.class), any(Constants.BOARD_TYPE.class), anyString());

        mvc.perform(
                delete("/api/board/{board}/comment/{id}", article.getBoard().toLowerCase(), articleComment.getId())
                        .cookie(new Cookie("JSESSIONID", "3F0E029648484BEAEF6B5C3578164E99"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(EmptyJsonResponse.newInstance())))
                .andDo(
                        document("deleteArticleComment",
                                requestHeaders(
                                        headerWithName("Cookie").optional().description("인증 쿠키. value는 JSESSIONID=키값")
                                ),
                                pathParameters(
                                        parameterWithName("board").description("게시판 " +
                                                Stream.of(Constants.BOARD_TYPE.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.toList())),
                                        parameterWithName("id").description("댓글 ID")
                                )
                        ));
    }

    @Test
    @WithMockJakdukUser
    public void setArticleCommentFeelingTest() throws Exception {

        when(articleService.setArticleCommentFeeling(any(CommonWriter.class), anyString(), any(Constants.FEELING_TYPE.class)))
                .thenReturn(articleComment);

        List<CommonFeelingUser> usersLiking = articleComment.getUsersLiking();
        List<CommonFeelingUser> usersDisliking = articleComment.getUsersDisliking();

        UserFeelingResponse expectResponse = UserFeelingResponse.builder()
                .myFeeling(JakdukUtils.getMyFeeling(commonWriter, usersLiking, usersDisliking))
                .numberOfLike(CollectionUtils.isEmpty(usersLiking) ? 0 : usersLiking.size())
                .numberOfDislike(CollectionUtils.isEmpty(usersDisliking) ? 0 : usersDisliking.size())
                .build();

        mvc.perform(
                post("/api/board/{board}/comment/{commentId}/{feeling}", articleComment.getArticle().getBoard().toLowerCase(),
                        articleComment.getId(), Constants.FEELING_TYPE.LIKE.name().toLowerCase())
                        .cookie(new Cookie("JSESSIONID", "3F0E029648484BEAEF6B5C3578164E99"))
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(expectResponse)))
                .andDo(
                        document("setArticleCommentFeeling",
                                requestHeaders(
                                        headerWithName("Cookie").optional().description("인증 쿠키. value는 JSESSIONID=키값")
                                ),
                                pathParameters(
                                        parameterWithName("board").description("게시판 " +
                                                Stream.of(Constants.BOARD_TYPE.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.toList())),
                                        parameterWithName("commentId").description("댓글 ID"),
                                        parameterWithName("feeling").description("감정 표현 종류 " +
                                                Stream.of(Constants.FEELING_TYPE.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.toList()))
                                ),
                                responseFields(
                                        fieldWithPath("myFeeling").type(JsonFieldType.STRING).description("나의 감정 표현 종류 " +
                                                Stream.of(Constants.FEELING_TYPE.values()).map(Enum::name).collect(Collectors.toList())).optional(),
                                        fieldWithPath("numberOfLike").type(JsonFieldType.NUMBER).description("좋아요 수"),
                                        fieldWithPath("numberOfDislike").type(JsonFieldType.NUMBER).description("싫어요 수")
                                )
                        ));
    }

    private List<FieldDescriptor> getArticleCommentsDescriptor(FieldDescriptor... descriptors) {
        List<FieldDescriptor> fieldDescriptors = new ArrayList<>(
                Arrays.asList(
                        fieldWithPath("comments").type(JsonFieldType.ARRAY).description("댓글 목록"),
                        fieldWithPath("comments.[].id").type(JsonFieldType.STRING).description("댓글 ID"),
                        subsectionWithPath("comments.[].article").type(JsonFieldType.OBJECT).description("연동 글"),
                        subsectionWithPath("comments.[].writer").type(JsonFieldType.OBJECT).description("글쓴이"),
                        fieldWithPath("comments.[].content").type(JsonFieldType.STRING).description("댓글 내용"),
                        fieldWithPath("comments.[].numberOfLike").type(JsonFieldType.NUMBER).description("좋아요 수"),
                        fieldWithPath("comments.[].numberOfDislike").type(JsonFieldType.NUMBER).description("싫어요 수"),
                        fieldWithPath("comments.[].myFeeling").type(JsonFieldType.STRING).description("나의 감정 상태. 인증 쿠키가 있고, 감정 표현을 한 경우 포함 된다."),
                        subsectionWithPath("comments.[].galleries").type(JsonFieldType.ARRAY).description("그림 목록"),
                        subsectionWithPath("comments.[].logs").type(JsonFieldType.ARRAY).description("로그 기록 목록")
                )
        );

        CollectionUtils.mergeArrayIntoCollection(descriptors, fieldDescriptors);

        return fieldDescriptors;
    }

    private FieldDescriptor[] getWriteArticleCommentFormDescriptor() {
        ConstraintDescriptions userConstraints = new ConstraintDescriptions(WriteArticleComment.class);

        return new FieldDescriptor[] {
                fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용. " + userConstraints.descriptionsForProperty("content")),
                subsectionWithPath("galleries").type(JsonFieldType.ARRAY).description("(optional) 그림 목록")
        };
    }

    private FieldDescriptor[] getWriteArticleCommentDescriptor() {
        return new FieldDescriptor[] {
                fieldWithPath("id").type(JsonFieldType.STRING).description("댓글 ID"),
                subsectionWithPath("article").type(JsonFieldType.OBJECT).description("연동 글"),
                fieldWithPath("article.id").type(JsonFieldType.STRING).description("글 ID"),
                fieldWithPath("article.seq").type(JsonFieldType.NUMBER).description("글 번호"),
                fieldWithPath("article.board").type(JsonFieldType.STRING).description("게시판"),
                subsectionWithPath("writer").type(JsonFieldType.OBJECT).description("글쓴이"),
                fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용"),
                subsectionWithPath("usersLiking").type(JsonFieldType.ARRAY).description("좋아요를 한 회원 목록"),
                subsectionWithPath("usersDisliking").type(JsonFieldType.ARRAY).description("싫어요를 한 회원 목록"),
                fieldWithPath("linkedGallery").type(JsonFieldType.BOOLEAN).description("연동 그림 존재 여부"),
                subsectionWithPath("logs").type(JsonFieldType.ARRAY).description("로그 기록 목록")
        };
    }

}
