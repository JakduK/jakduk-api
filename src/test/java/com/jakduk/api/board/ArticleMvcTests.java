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
import com.jakduk.api.model.aggregate.BoardTop;
import com.jakduk.api.model.db.Article;
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
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
public class ArticleMvcTests {

    @Autowired
    private MockMvc mvc;

    @MockBean private RestTemplateBuilder restTemplateBuilder;
    @MockBean private ArticleService articleService;
    @MockBean private GalleryService galleryService;
    @MockBean private RabbitMQPublisher rabbitMQPublisher;

    private CommonWriter commonWriter;
    private Article article;
    private List<BoardCategory> categories;
    private BoardCategory boardCategory;
    private Map<String, String> categoriesMap;
    private WriteArticle writeArticleForm;
    private List<Gallery> galleries;
    private List<BoardGallerySimple> simpleGalleries;

    @Before
    public void setUp(){
        commonWriter = new CommonWriter();
        commonWriter.setUserId("571ccf50ccbfc325b20711c5");
        commonWriter.setUsername("test07");
        commonWriter.setProviderId(Constants.ACCOUNT_TYPE.JAKDUK);

        categories = BoardCategoryGenerator.getCategories(Constants.BOARD_TYPE.FOOTBALL, JakdukUtils.getLocale());

        boardCategory = categories.get(0);

        categoriesMap = categories.stream()
                .collect(Collectors.toMap(BoardCategory::getCode, boardCategory -> boardCategory.getNames().get(0).getName()));

        categoriesMap.put("ALL", JakdukUtils.getMessageSource("board.category.all"));

        article = new Article();
        article.setId("59c8879fa2b594c5d33e6ac4");
        article.setSeq(2);
        article.setWriter(commonWriter);
        article.setSubject("글 제목입니다.");
        article.setContent("내용입니다. 아주 길 수도 있음.");
        article.setBoard(Constants.BOARD_TYPE.FOOTBALL.name());
        article.setCategory(boardCategory.getCode());
        article.setViews(15);
        article.setUsersLiking(Arrays.asList(new CommonFeelingUser("58ee4993807d713fa7735f1d", "566d68d5e4b0dfaaa5b98685", "test05")));
        article.setUsersDisliking(Arrays.asList(new CommonFeelingUser("58ee4993807d713fa7735f1d", "566d68d5e4b0dfaaa5b98685", "test05")));
        article.setStatus(new ArticleStatus(false, false));
        article.setLogs(Arrays.asList(new BoardLog("58e9959b807d71113a999c6d", Constants.ARTICLE_LOG_TYPE.CREATE.name(), new SimpleWriter("58ee4993807d713fa7735f1d", "test05"))));
        article.setShortContent("본문입니다. (100자)");
        article.setLastUpdated(LocalDateTime.parse("2017-09-27T23:42:44.810"));
        article.setLinkedGallery(true);

        GalleryOnBoard galleryOnBoard = new GalleryOnBoard("59c2945bbe3eb62dfca3ed97", "공차는사진");

        writeArticleForm = new WriteArticle("제목입니다.", "내용입니다.", boardCategory.getCode(), Arrays.asList(galleryOnBoard));

        galleries = Arrays.asList(
                new Gallery() {{
                    setId(galleryOnBoard.getId());
                    setName(galleryOnBoard.getName());
                    setFileName("Cat Profile-48.png");
                    setContentType("image/png");
                    setWriter(commonWriter);
                    setSize(1149L);
                    setFileSize(1870L);
                    setStatus(new GalleryStatus(Constants.GALLERY_STATUS_TYPE.TEMP));
                    setHash("7eb65b85521d247ab4c5f79e279c03db");
                }}
        );

        simpleGalleries = Arrays.asList(
                new BoardGallerySimple() {{
                    setId("58b9050b807d714eaf50a111");
                    setThumbnailUrl("https://dev-api.jakduk.com//gallery/thumbnail/58b9050b807d714eaf50a111");
                }}
        );
    }

    @Test
    @WithMockUser
    public void getArticlesTest() throws Exception {

        GetArticle getArticle = new GetArticle();
        BeanUtils.copyProperties(article, getArticle);
        getArticle.setGalleries(simpleGalleries);
        getArticle.setCommentCount(5);
        getArticle.setLikingCount(article.getUsersLiking().size());
        getArticle.setDislikingCount(article.getUsersDisliking().size());

        GetArticle notice = new GetArticle();
        notice.setId("58b7b9dd716dce06b10e449a");
        notice.setBoard(Constants.BOARD_TYPE.FOOTBALL.name());
        notice.setWriter(commonWriter);
        notice.setSubject("공지글 제목입니다.");
        notice.setSeq(3);
        notice.setCategory(boardCategory.getCode());
        notice.setViews(15);
        notice.setStatus(new ArticleStatus(true, false));
        notice.setGalleries(simpleGalleries);
        notice.setShortContent("본문입니다. (100자)");
        notice.setCommentCount(8);
        notice.setLikingCount(10);
        notice.setDislikingCount(2);

        GetArticlesResponse expectResponse = new GetArticlesResponse();
        expectResponse.setCategories(categoriesMap);
        expectResponse.setArticles(Arrays.asList(getArticle));
        expectResponse.setNotices(Arrays.asList(notice));
        expectResponse.setLast(false);
        expectResponse.setFirst(true);
        expectResponse.setTotalPages(50);
        expectResponse.setSize(20);
        expectResponse.setNumber(0);
        expectResponse.setNumberOfElements(20);
        expectResponse.setTotalElements(1011L);

        when(articleService.getArticles(any(Constants.BOARD_TYPE.class), anyString(), anyInt(), anyInt()))
                .thenReturn(expectResponse);

        mvc.perform(
                get("/api/board/{board}/articles", Constants.BOARD_TYPE.FOOTBALL.name().toLowerCase())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(expectResponse)))
                .andDo(document("getArticles",
                        pathParameters(
                                parameterWithName("board").description("게시판 " +
                                        Stream.of(Constants.BOARD_TYPE.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.toList()))
                        ),
                        requestParameters(
                                parameterWithName("page").description("(optional, default 1) 페이지 번호. 1부터 시작.").optional(),
                                parameterWithName("size").description("(optional, default 20) 페이지 크기.").optional(),
                                parameterWithName("categoryCode").description("(optional, default ALL) 말머리. board가 FREE 일때에는 무시된다. FOOTBALL, DEVELOPER 일 때에는 필수다.").optional()
                        ),
                        responseFields(
                                subsectionWithPath("categories").type(JsonFieldType.OBJECT).description("말머리 맵. key는 말머리코드, value는 표시되는 이름(Locale 지원)"),
                                fieldWithPath("articles").type(JsonFieldType.ARRAY).description("글 목록"),
                                fieldWithPath("articles.[].id").type(JsonFieldType.STRING).description("글 ID"),
                                fieldWithPath("articles.[].board").type(JsonFieldType.STRING).description("게시판"),
                                subsectionWithPath("articles.[].writer").type(JsonFieldType.OBJECT).description("글쓴이"),
                                fieldWithPath("articles.[].subject").type(JsonFieldType.STRING).description("글제목"),
                                fieldWithPath("articles.[].seq").type(JsonFieldType.NUMBER).description("글번호"),
                                fieldWithPath("articles.[].category").type(JsonFieldType.STRING).description("말머리"),
                                fieldWithPath("articles.[].views").type(JsonFieldType.NUMBER).description("읽음 수"),
                                subsectionWithPath("articles.[].status").type(JsonFieldType.OBJECT).description("글 상태"),
                                subsectionWithPath("articles.[].galleries").type(JsonFieldType.ARRAY).description("그림 목록"),
                                fieldWithPath("articles.[].shortContent").type(JsonFieldType.STRING).description("본문 100자"),
                                fieldWithPath("articles.[].commentCount").type(JsonFieldType.NUMBER).description("댓글 수"),
                                fieldWithPath("articles.[].likingCount").type(JsonFieldType.NUMBER).description("좋아요 수"),
                                fieldWithPath("articles.[].dislikingCount").type(JsonFieldType.NUMBER).description("싫어요 수"),
                                subsectionWithPath("notices").type(JsonFieldType.ARRAY).description("공지글 목록. json 형식은 articles와 같음."),
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

    @Test
    @WithMockUser
    public void getTopsTest() throws Exception {

        List<BoardTop> expectTopLikes = Arrays.asList(
                new BoardTop() {{
                    setId("58b7b9dd716dce06b10e449a");
                    setSeq(1);
                    setSubject("인기있는 글 제목");
                    setCount(5);
                    setViews(100);
                }}
        );

        when(articleService.getArticlesTopLikes(any(Constants.BOARD_TYPE.class), any(ObjectId.class)))
                .thenReturn(expectTopLikes);

        List<BoardTop> expectTopComments = Arrays.asList(
                new BoardTop() {{
                    setId("58b7b9dd716dce06b10e449a");
                    setSeq(2);
                    setSubject("댓글많은 글 제목");
                    setCount(10);
                    setViews(150);
                }}
        );

        when(articleService.getArticlesTopComments(any(Constants.BOARD_TYPE.class), any(ObjectId.class)))
                .thenReturn(expectTopComments);

        GetArticlesTopsResponse expectResponse = new GetArticlesTopsResponse(expectTopLikes, expectTopComments);

        mvc.perform(
                get("/api/board/{board}/tops", Constants.BOARD_TYPE.FOOTBALL.name().toLowerCase())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(expectResponse)))
                .andDo(document("getArticlesTops",
                        pathParameters(
                                parameterWithName("board").description("게시판 " +
                                        Stream.of(Constants.BOARD_TYPE.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.toList()))
                        ),
                        responseFields(
                                fieldWithPath("topLikes").type(JsonFieldType.ARRAY).description("주간 좋아요수 선두 목록"),
                                fieldWithPath("topLikes.[].id").type(JsonFieldType.STRING).description("글 ID"),
                                fieldWithPath("topLikes.[].seq").type(JsonFieldType.NUMBER).description("글번호"),
                                fieldWithPath("topLikes.[].subject").type(JsonFieldType.STRING).description("글제목"),
                                fieldWithPath("topLikes.[].count").type(JsonFieldType.NUMBER).description("좋아요 수"),
                                fieldWithPath("topLikes.[].views").type(JsonFieldType.NUMBER).description("읽음 수"),
                                fieldWithPath("topComments").type(JsonFieldType.ARRAY).description("주간 댓글수 선두 목록"),
                                fieldWithPath("topComments.[].id").type(JsonFieldType.STRING).description("글 ID"),
                                fieldWithPath("topComments.[].seq").type(JsonFieldType.NUMBER).description("글번호"),
                                fieldWithPath("topComments.[].subject").type(JsonFieldType.STRING).description("글제목"),
                                fieldWithPath("topComments.[].count").type(JsonFieldType.NUMBER).description("댓글 수"),
                                fieldWithPath("topComments.[].views").type(JsonFieldType.NUMBER).description("읽음 수")
                        )
                ));
    }

    @Test
    @WithMockJakdukUser
    public void getArticleDetailTest() throws Exception {

        ArticleDetail articleDetail = new ArticleDetail();
        BeanUtils.copyProperties(article, articleDetail);
        articleDetail.setCategory(boardCategory);
        articleDetail.setNumberOfLike(article.getUsersLiking().size());
        articleDetail.setNumberOfDislike(article.getUsersDisliking().size());
        articleDetail.setLogs(
                article.getLogs().stream()
                        .map(boardLog -> {
                            ArticleLog articleLog = new ArticleLog();
                            BeanUtils.copyProperties(boardLog, articleLog);
                            LocalDateTime timestamp = DateUtils.dateToLocalDateTime(new ObjectId(articleLog.getId()).getDate());
                            articleLog.setType(Constants.ARTICLE_LOG_TYPE.valueOf(boardLog.getType()));
                            articleLog.setTimestamp(timestamp);

                            return articleLog;
                        })
                        .sorted(Comparator.comparing(ArticleLog::getId).reversed())
                        .collect(Collectors.toList())
        );
        articleDetail.setGalleries(
                Arrays.asList(
                        new ArticleGallery() {{
                            setId("58b9050b807d714eaf50a111");
                            setName("성남FC 시즌권 사진");
                            setImageUrl("https://dev-api.jakduk.com//gallery/58b9050b807d714eaf50a111");
                            setThumbnailUrl("https://dev-api.jakduk.com//gallery/thumbnail/58b9050b807d714eaf50a111");
                        }}
                )
        );
        articleDetail.setMyFeeling(Constants.FEELING_TYPE.LIKE);

        ArticleSimple prevPost = new ArticleSimple();
        prevPost.setId("59c88b2ea2b594ca18fecf05");
        prevPost.setSeq(286);
        prevPost.setSubject("이전 글 제목");
        prevPost.setWriter(commonWriter);
        prevPost.setBoard(Constants.BOARD_TYPE.FOOTBALL.name());

        ArticleSimple nextPost = new ArticleSimple();
        nextPost.setId("59c8879fa2b594c5d33e6ac4");
        nextPost.setSeq(285);
        nextPost.setSubject("다음 글 제목");
        nextPost.setWriter(commonWriter);
        nextPost.setBoard(Constants.BOARD_TYPE.FOOTBALL.name());

        LatestArticle latestArticle = new LatestArticle();
        latestArticle.setId("58e9959b807d71113a999c6e");
        latestArticle.setSeq(216);
        latestArticle.setWriter(commonWriter);
        latestArticle.setSubject("작성자의 최근 글 제목");
        latestArticle.setGalleries(simpleGalleries);

        GetArticleDetailResponse expectResponse = new GetArticleDetailResponse();
        expectResponse.setArticle(articleDetail);
        expectResponse.setPrevArticle(prevPost);
        expectResponse.setNextArticle(nextPost);
        expectResponse.setLatestArticlesByWriter(Arrays.asList(latestArticle));

        when(articleService.getArticleDetail(any(CommonWriter.class), any(Constants.BOARD_TYPE.class), anyInt(), anyBoolean()))
                .thenReturn(ResponseEntity.ok().body(expectResponse));

        mvc.perform(
                get("/api/board/{board}/{seq}", article.getBoard().toLowerCase(), article.getSeq())
                        .header("Cookie", "JSESSIONID=3F0E029648484BEAEF6B5C3578164E99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(expectResponse)))
                .andDo(document("getArticleDetail",
                        requestHeaders(
                                headerWithName("Cookie").description("(optional) 인증 쿠키. value는 JSESSIONID=키값")
                        ),
                        pathParameters(
                                parameterWithName("board").description("게시판 " +
                                        Stream.of(Constants.BOARD_TYPE.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.toList())),
                                parameterWithName("seq").description("글 번호")
                        ),
                        responseFields(
                                fieldWithPath("article").type(JsonFieldType.OBJECT).description("글 상세"),
                                fieldWithPath("article.id").type(JsonFieldType.STRING).description("글 ID"),
                                fieldWithPath("article.board").type(JsonFieldType.STRING).description("게시판"),
                                subsectionWithPath("article.writer").type(JsonFieldType.OBJECT).description("글쓴이"),
                                fieldWithPath("article.subject").type(JsonFieldType.STRING).description("글제목"),
                                fieldWithPath("article.seq").type(JsonFieldType.NUMBER).description("글번호"),
                                fieldWithPath("article.content").type(JsonFieldType.STRING).description("글 내용"),
                                subsectionWithPath("article.category").type(JsonFieldType.OBJECT).description("말머리"),
                                fieldWithPath("article.views").type(JsonFieldType.NUMBER).description("읽음 수"),
                                fieldWithPath("article.numberOfLike").type(JsonFieldType.NUMBER).description("좋아요 수"),
                                fieldWithPath("article.numberOfDislike").type(JsonFieldType.NUMBER).description("싫어요 수"),
                                subsectionWithPath("article.status").type(JsonFieldType.OBJECT).description("글 상태"),
                                subsectionWithPath("article.logs").type(JsonFieldType.ARRAY).description("로그 기록 목록"),
                                subsectionWithPath("article.galleries").type(JsonFieldType.ARRAY).description("그림 목록"),
                                fieldWithPath("article.myFeeling").type(JsonFieldType.STRING).description("나의 감정 상태. 인증 쿠키가 있고, 감정 표현을 한 경우 포함 된다."),
                                fieldWithPath("prevArticle").type(JsonFieldType.OBJECT).description("이전 글"),
                                fieldWithPath("prevArticle.id").type(JsonFieldType.STRING).description("글 ID"),
                                fieldWithPath("prevArticle.seq").type(JsonFieldType.NUMBER).description("글번호"),
                                fieldWithPath("prevArticle.subject").type(JsonFieldType.STRING).description("글제목"),
                                subsectionWithPath("prevArticle.writer").type(JsonFieldType.OBJECT).description("글쓴이"),
                                fieldWithPath("prevArticle.board").type(JsonFieldType.STRING).description("게시판"),
                                subsectionWithPath("nextArticle").type(JsonFieldType.OBJECT).description("다음 글. json 형식은 prevArticle과 같음."),
                                fieldWithPath("latestArticlesByWriter").type(JsonFieldType.ARRAY).description("글쓴이의 최근 글."),
                                fieldWithPath("latestArticlesByWriter.[].id").type(JsonFieldType.STRING).description("글 ID"),
                                fieldWithPath("latestArticlesByWriter.[].seq").type(JsonFieldType.NUMBER).description("글번호"),
                                subsectionWithPath("latestArticlesByWriter.[].writer").type(JsonFieldType.OBJECT).description("글쓴이"),
                                fieldWithPath("latestArticlesByWriter.[].subject").type(JsonFieldType.STRING).description("글제목"),
                                subsectionWithPath("latestArticlesByWriter.[].galleries").type(JsonFieldType.ARRAY).description("그림 목록")
                        )
                ));
    }

    @Test
    @WithMockJakdukUser
    public void writeArticleTest() throws Exception {

        when(galleryService.findByIdIn(any()))
                .thenReturn(galleries);

        when(articleService.insertArticle(any(CommonWriter.class), any(Constants.BOARD_TYPE.class), anyString(), anyString(), anyString(),
                anyBoolean()))
                .thenReturn(article);

        doNothing().when(galleryService)
                .processLinkedGalleries(anyString(), anyList(), anyList(), anyList(), any(Constants.GALLERY_FROM_TYPE.class), anyString());

        WriteArticleResponse expectResponse = new WriteArticleResponse(article.getBoard(), article.getSeq());

        mvc.perform(
                post("/api/board/{board}", article.getBoard().toLowerCase())
                        .cookie(new Cookie("JSESSIONID", "3F0E029648484BEAEF6B5C3578164E99"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(ObjectMapperUtils.writeValueAsString(writeArticleForm)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(expectResponse)))
                .andDo(document("writeArticle",
                        requestHeaders(
                                headerWithName("Cookie").optional().description("인증 쿠키. value는 JSESSIONID=키값")
                        ),
                        requestFields(this.getWriteArticleFormDescriptor()),
                        pathParameters(
                                parameterWithName("board").description("게시판 " +
                                        Stream.of(Constants.BOARD_TYPE.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.toList()))
                        ),
                        responseFields(
                                fieldWithPath("board").type(JsonFieldType.STRING).description("게시판"),
                                fieldWithPath("seq").type(JsonFieldType.NUMBER).description("글 번호")
                        )
                ));
    }

    @Test
    @WithMockUser
    public void getBoardCategories() throws Exception {
        mvc.perform(
                get("/api/board/{board}/categories", article.getBoard().toLowerCase())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(
                        document("getBoardCategories",
                                pathParameters(
                                        parameterWithName("board").description("게시판 " +
                                                Stream.of(Constants.BOARD_TYPE.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.toList()))
                                ),
                                responseFields(
                                        fieldWithPath("categories").type(JsonFieldType.ARRAY).description("말머리 목록"),
                                        fieldWithPath("categories.[].code").type(JsonFieldType.STRING).description("말머리 코드"),
                                        fieldWithPath("categories.[].names").type(JsonFieldType.ARRAY).description("말머리 이름 목록(Locale 지원)"),
                                        fieldWithPath("categories.[].names.[].language").type(JsonFieldType.STRING).description("언어"),
                                        fieldWithPath("categories.[].names.[].name").type(JsonFieldType.STRING).description("이름")
                                )
                        ));
    }

    @Test
    @WithMockJakdukUser
    public void editArticleTest() throws Exception {

        when(galleryService.findByIdIn(any()))
                .thenReturn(galleries);

        when(articleService.updateArticle(any(CommonWriter.class), any(Constants.BOARD_TYPE.class), anyInt(), anyString(), anyString(), anyString(),
                anyBoolean()))
                .thenReturn(article);

        doNothing().when(galleryService)
                .processLinkedGalleries(anyString(), anyList(), anyList(), anyList(), any(Constants.GALLERY_FROM_TYPE.class), anyString());

        WriteArticleResponse expectResponse = new WriteArticleResponse(article.getBoard(), article.getSeq());

        mvc.perform(
                put("/api/board/{board}/{seq}", article.getBoard().toLowerCase(), article.getSeq())
                        .cookie(new Cookie("JSESSIONID", "3F0E029648484BEAEF6B5C3578164E99"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(ObjectMapperUtils.writeValueAsString(writeArticleForm)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(expectResponse)))
                .andDo(
                        document("editArticle",
                                requestHeaders(
                                        headerWithName("Cookie").optional().description("인증 쿠키. value는 JSESSIONID=키값")
                                ),
                                requestFields(this.getWriteArticleFormDescriptor()),
                                pathParameters(
                                        parameterWithName("board").description("게시판 " +
                                                Stream.of(Constants.BOARD_TYPE.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.toList())),
                                        parameterWithName("seq").description("글번호")
                                ),
                                responseFields(
                                        fieldWithPath("board").type(JsonFieldType.STRING).description("게시판"),
                                        fieldWithPath("seq").type(JsonFieldType.NUMBER).description("글 번호")
                                )
                        ));
    }

    @Test
    @WithMockJakdukUser
    public void deleteArticleTest() throws Exception {

        Constants.ARTICLE_DELETE_TYPE expectDeleteType = Constants.ARTICLE_DELETE_TYPE.ALL;

        when(articleService.deleteArticle(any(CommonWriter.class), any(Constants.BOARD_TYPE.class), anyInt()))
                .thenReturn(expectDeleteType);

        DeleteArticleResponse expectResponse = new DeleteArticleResponse(expectDeleteType);

        mvc.perform(
                delete("/api/board/{board}/{seq}", article.getBoard().toLowerCase(), article.getSeq())
                        .cookie(new Cookie("JSESSIONID", "3F0E029648484BEAEF6B5C3578164E99"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(expectResponse)))
                .andDo(
                        document("deleteArticle",
                                requestHeaders(
                                        headerWithName("Cookie").optional().description("인증 쿠키. value는 JSESSIONID=키값")
                                ),
                                pathParameters(
                                        parameterWithName("board").description("게시판 " +
                                                Stream.of(Constants.BOARD_TYPE.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.toList())),
                                        parameterWithName("seq").description("글번호")
                                ),
                                responseFields(
                                        fieldWithPath("result").type(JsonFieldType.STRING).description("결과 타입. [ALL : 모두 지움, CONTENT : 글 본문만 지움(댓글 유지)]")
                                )
                        ));
    }

    @Test
    @WithMockJakdukUser
    public void setArticleFeeling() throws Exception {

        when(articleService.setArticleFeelings(any(CommonWriter.class), any(Constants.BOARD_TYPE.class), anyInt(),
                any(Constants.FEELING_TYPE.class)))
                .thenReturn(article);

        List<CommonFeelingUser> usersLiking = article.getUsersLiking();
        List<CommonFeelingUser> usersDisliking = article.getUsersDisliking();

        UserFeelingResponse expectResponse = new UserFeelingResponse();
        expectResponse.setMyFeeling(JakdukUtils.getMyFeeling(commonWriter, usersLiking, usersDisliking));
        expectResponse.setNumberOfLike(CollectionUtils.isEmpty(usersLiking) ? 0 : usersLiking.size());
        expectResponse.setNumberOfDislike(CollectionUtils.isEmpty(usersDisliking) ? 0 : usersDisliking.size());

        mvc.perform(
                post("/api/board/{board}/{seq}/{feeling}", article.getBoard().toLowerCase(), article.getSeq(),
                        Constants.FEELING_TYPE.LIKE.name().toLowerCase())
                        .cookie(new Cookie("JSESSIONID", "3F0E029648484BEAEF6B5C3578164E99"))
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(expectResponse)))
                .andDo(
                        document("setArticleFeeling",
                                requestHeaders(
                                        headerWithName("Cookie").optional().description("인증 쿠키. value는 JSESSIONID=키값")
                                ),
                                pathParameters(
                                        parameterWithName("board").description("게시판 " +
                                                Stream.of(Constants.BOARD_TYPE.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.toList())),
                                        parameterWithName("seq").description("글번호"),
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

    @Test
    @WithMockUser
    public void getArticleFeelingUsersTest() throws Exception {

        when(articleService.findOneBySeq(any(Constants.BOARD_TYPE.class), anyInt()))
                .thenReturn(article);

        GetArticleFeelingUsersResponse expectResponse = new GetArticleFeelingUsersResponse(article.getSeq(), article.getUsersLiking(), article.getUsersDisliking());

        mvc.perform(
                get("/api/board/{board}/{seq}/feeling/users",
                        Constants.BOARD_TYPE.FOOTBALL.name().toLowerCase(), article.getSeq())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(expectResponse)))
                .andDo(
                        document("getArticleFeelingUsers",
                                pathParameters(
                                        parameterWithName("board").description("게시판 " +
                                                Stream.of(Constants.BOARD_TYPE.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.toList())),
                                        parameterWithName("seq").description("글번호")
                                ),
                                responseFields(
                                        fieldWithPath("seq").type(JsonFieldType.NUMBER).description("글번호"),
                                        subsectionWithPath("usersLiking").type(JsonFieldType.ARRAY).description("좋아요 회원 목록"),
                                        subsectionWithPath("usersDisliking").type(JsonFieldType.ARRAY).description("싫어요 회원 목록")
                                )
                        ));
    }

    @Test
    @WithMockJakdukUser
    public void enableArticleNoticeTest() throws Exception {

        doNothing().when(articleService)
                .enableArticleNotice(any(CommonWriter.class), any(Constants.BOARD_TYPE.class), anyInt());

        mvc.perform(
                post("/api/board/{board}/{seq}/notice", article.getBoard().toLowerCase(), article.getSeq())
                        .cookie(new Cookie("JSESSIONID", "3F0E029648484BEAEF6B5C3578164E99"))
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(EmptyJsonResponse.newInstance())))
                .andDo(
                        document("enableArticleNotice",
                                requestHeaders(
                                        headerWithName("Cookie").optional().description("인증 쿠키. value는 JSESSIONID=키값")
                                ),
                                pathParameters(
                                        parameterWithName("board").description("게시판 " +
                                                Stream.of(Constants.BOARD_TYPE.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.toList())),
                                        parameterWithName("seq").description("글번호")
                                )
                        ));
    }

    @Test
    @WithMockJakdukUser
    public void disableArticleNoticeTest() throws Exception {

        doNothing().when(articleService)
                .disableArticleNotice(any(CommonWriter.class), any(Constants.BOARD_TYPE.class), anyInt());

        mvc.perform(
                delete("/api/board/{board}/{seq}/notice", article.getBoard().toLowerCase(), article.getSeq())
                        .cookie(new Cookie("JSESSIONID", "3F0E029648484BEAEF6B5C3578164E99"))
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(EmptyJsonResponse.newInstance())))
                .andDo(
                        document("disableArticleNotice",
                                requestHeaders(
                                        headerWithName("Cookie").optional().description("인증 쿠키. value는 JSESSIONID=키값")
                                ),
                                pathParameters(
                                        parameterWithName("board").description("게시판 " +
                                                Stream.of(Constants.BOARD_TYPE.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.toList())),
                                        parameterWithName("seq").description("글번호")
                                )
                        ));
    }

    private FieldDescriptor[] getWriteArticleFormDescriptor() {
        ConstraintDescriptions userConstraints = new ConstraintDescriptions(WriteArticle.class);

        return new FieldDescriptor[] {
                fieldWithPath("subject").type(JsonFieldType.STRING).description("글 제목. " + userConstraints.descriptionsForProperty("subject")),
                fieldWithPath("content").type(JsonFieldType.STRING).description("글 내용. " + userConstraints.descriptionsForProperty("content")),
                fieldWithPath("categoryCode").type(JsonFieldType.STRING)
                        .description("(optional, default ALL) 말머리. board가 FREE 일때에는 무시된다. FOOTBALL, DEVELOPER일 때에는 필수다."),
                subsectionWithPath("galleries").type(JsonFieldType.ARRAY).description("(optional) 그림 목록")
        };
    }

}
