package com.jakduk.api.board;

import com.jakduk.api.TestMvcConfig;
import com.jakduk.api.common.AuthHelper;
import com.jakduk.api.common.Constants;
import com.jakduk.api.common.board.category.BoardCategory;
import com.jakduk.api.common.board.category.BoardCategoryGenerator;
import com.jakduk.api.common.util.DateUtils;
import com.jakduk.api.common.util.JakdukUtils;
import com.jakduk.api.common.util.ObjectMapperUtils;
import com.jakduk.api.model.aggregate.BoardTop;
import com.jakduk.api.model.db.Article;
import com.jakduk.api.model.db.Gallery;
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
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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
    @MockBean private AuthHelper authHelper;

    private CommonWriter commonWriter;
    private Article article;
    private List<BoardCategory> categories;
    private BoardCategory boardCategory;
    private Map<String, String> categoriesMap;
    private WriteArticle writeArticleform;
    private List<Gallery> galleries;
    private List<BoardGallerySimple> simpleGalleires;

    @Before
    public void setUp(){
        commonWriter = CommonWriter.builder()
                .userId("571ccf50ccbfc325b20711c5")
                .username("test07")
                .providerId(Constants.ACCOUNT_TYPE.JAKDUK)
                .build();

        categories = BoardCategoryGenerator.getCategories(Constants.BOARD_TYPE.FOOTBALL, JakdukUtils.getLocale());

        boardCategory = categories.get(0);

        categoriesMap = categories.stream()
                .collect(Collectors.toMap(BoardCategory::getCode, boardCategory -> boardCategory.getNames().get(0).getName()));

        categoriesMap.put("ALL", JakdukUtils.getResourceBundleMessage("messages.board", "board.category.all"));

        article = Article.builder()
                .id("59c8879fa2b594c5d33e6ac4")
                .seq(2)
                .writer(commonWriter)
                .subject("글 제목입니다.")
                .content("내용입니다. 아주 길 수도 있음.")
                .board(Constants.BOARD_TYPE.FOOTBALL.name())
                .category(boardCategory.getCode())
                .views(15)
                .usersLiking(Arrays.asList(new CommonFeelingUser("58ee4993807d713fa7735f1d", "566d68d5e4b0dfaaa5b98685", "test05")))
                .usersDisliking(Arrays.asList(new CommonFeelingUser("58ee4993807d713fa7735f1d", "566d68d5e4b0dfaaa5b98685", "test05")))
                .status(new ArticleStatus(false, false, Constants.DEVICE_TYPE.NORMAL))
                .logs(Arrays.asList(new BoardLog("58e9959b807d71113a999c6d", Constants.ARTICLE_LOG_TYPE.CREATE.name(), new SimpleWriter("58ee4993807d713fa7735f1d", "test05"))))
                .shortContent("본문입니다. (100자)")
                .lastUpdated(LocalDateTime.parse("2017-09-27T23:42:44.810"))
                .linkedGallery(true)
                .build();

        GalleryOnBoard galleryOnBoard = new GalleryOnBoard("59c2945bbe3eb62dfca3ed97", "공차는사진");

        writeArticleform = WriteArticle.builder()
                .subject("제목입니다.")
                .content("내용입니다.")
                .categoryCode(boardCategory.getCode())
                .galleries(Arrays.asList(galleryOnBoard))
                .build();

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

        simpleGalleires = Arrays.asList(
                new BoardGallerySimple("58b9050b807d714eaf50a111", "https://dev-api.jakduk.com//gallery/thumbnail/58b9050b807d714eaf50a111"));

    }

    @Test
    @WithMockUser
    public void getArticlesTest() throws Exception {

        GetArticle getArticle = new GetArticle();
        BeanUtils.copyProperties(article, getArticle);
        getArticle.setGalleries(simpleGalleires);
        getArticle.setCommentCount(5);
        getArticle.setLikingCount(article.getUsersLiking().size());
        getArticle.setDislikingCount(article.getUsersDisliking().size());

        GetArticle notice = GetArticle.builder()
                .id("58b7b9dd716dce06b10e449a")
                .board(Constants.BOARD_TYPE.FOOTBALL.name())
                .writer(commonWriter)
                .subject("공지글 제목입니다.")
                .seq(3)
                .category(boardCategory.getCode())
                .views(15)
                .status(new ArticleStatus(true, false, Constants.DEVICE_TYPE.NORMAL))
                .galleries(simpleGalleires)
                .shortContent("본문입니다. (100자)")
                .commentCount(8)
                .likingCount(10)
                .dislikingCount(2)
                .build();

        GetArticlesResponse expectResponse = GetArticlesResponse.builder()
                .categories(categoriesMap)
                .articles(Arrays.asList(getArticle))
                .notices(Arrays.asList(notice))
                .last(false)
                .first(true)
                .totalPages(50)
                .size(20)
                .number(0)
                .numberOfElements(20)
                .totalElements(1011L)
                .build();

        when(articleService.getArticles(any(Constants.BOARD_TYPE.class), anyString(), anyInt(), anyInt()))
                .thenReturn(expectResponse);

        mvc.perform(
                get("/api/board/{board}/articles", Constants.BOARD_TYPE.FOOTBALL.name().toLowerCase())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(expectResponse)))
                .andDo(
                        document("getArticles",
                                pathParameters(
                                        parameterWithName("board").description("게시판 " +
                                                Stream.of(Constants.BOARD_TYPE.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.toList()))
                                ),
                                requestParameters(
                                        parameterWithName("page").description("(optional, default 1) 페이지 번호. 1부터 시작.").optional(),
                                        parameterWithName("size").description("(optional, default 20) 페이지 크기.").optional(),
                                        parameterWithName("categoryCode").description("(optional, default ALL) 말머리. board가 FREE 일때에는 무시된다. FOOTBALL, DEVELOPER일 때에는 필수다.").optional()
                                ),
                                responseFields(
                                        fieldWithPath("categories").type(JsonFieldType.OBJECT).description("말머리 맵. key는 말머리코드, value는 표시되는 이름(Locale 지원)"),
                                        fieldWithPath("articles").type(JsonFieldType.ARRAY).description("글 목록"),
                                        fieldWithPath("articles.[].id").type(JsonFieldType.STRING).description("글 ID"),
                                        fieldWithPath("articles.[].board").type(JsonFieldType.STRING).description("게시판"),
                                        fieldWithPath("articles.[].writer").type(JsonFieldType.OBJECT).description("글쓴이"),
                                        fieldWithPath("articles.[].subject").type(JsonFieldType.STRING).description("글제목"),
                                        fieldWithPath("articles.[].seq").type(JsonFieldType.NUMBER).description("글번호"),
                                        fieldWithPath("articles.[].category").type(JsonFieldType.STRING).description("말머리"),
                                        fieldWithPath("articles.[].views").type(JsonFieldType.NUMBER).description("읽음 수"),
                                        fieldWithPath("articles.[].status").type(JsonFieldType.OBJECT).description("글 상태"),
                                        fieldWithPath("articles.[].galleries").type(JsonFieldType.ARRAY).description("그림 목록"),
                                        fieldWithPath("articles.[].shortContent").type(JsonFieldType.STRING).description("본문 100자"),
                                        fieldWithPath("articles.[].commentCount").type(JsonFieldType.NUMBER).description("댓글 수"),
                                        fieldWithPath("articles.[].likingCount").type(JsonFieldType.NUMBER).description("좋아요 수"),
                                        fieldWithPath("articles.[].dislikingCount").type(JsonFieldType.NUMBER).description("싫어요 수"),
                                        fieldWithPath("notices").type(JsonFieldType.ARRAY).description("공지글 목록. json 형식은 articles와 같음."),
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
                BoardTop.builder()
                        .id("58b7b9dd716dce06b10e449a")
                        .seq(1)
                        .subject("인기있는 글 제목")
                        .count(5)
                        .views(100)
                        .build()
        );

        when(articleService.getArticlesTopLikes(any(Constants.BOARD_TYPE.class), any(ObjectId.class)))
                .thenReturn(expectTopLikes);

        List<BoardTop> expectTopComments = Arrays.asList(
                BoardTop.builder()
                        .id("58b7b9dd716dce06b10e449a")
                        .seq(2)
                        .subject("댓글많은 글 제목")
                        .count(10)
                        .views(150)
                        .build()
        );

        when(articleService.getArticlesTopComments(any(Constants.BOARD_TYPE.class), any(ObjectId.class)))
                .thenReturn(expectTopComments);

        GetArticlesTopsResponse expectResponse = GetArticlesTopsResponse.builder()
                .topLikes(expectTopLikes)
                .topComments(expectTopComments)
                .build();

        mvc.perform(
                get("/api/board/{board}/tops", Constants.BOARD_TYPE.FOOTBALL.name().toLowerCase())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(expectResponse)))
                .andDo(
                        document("getArticlesTops",
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
    @WithMockUser
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
                        ArticleGallery.builder()
                                .id("58b9050b807d714eaf50a111")
                                .name("성남FC 시즌권 사진")
                                .imageUrl("https://dev-api.jakduk.com//gallery/58b9050b807d714eaf50a111")
                                .thumbnailUrl("https://dev-api.jakduk.com//gallery/thumbnail/58b9050b807d714eaf50a111")
                                .build()
                )
        );
        articleDetail.setMyFeeling(Constants.FEELING_TYPE.LIKE);

        ArticleSimple prevPost = ArticleSimple.builder()
                .id("59c88b2ea2b594ca18fecf05")
                .seq(286)
                .subject("이전 글 제목")
                .writer(commonWriter)
                .board(Constants.BOARD_TYPE.FOOTBALL.name())
                .build();

        ArticleSimple nextPost = ArticleSimple.builder()
                .id("59c8879fa2b594c5d33e6ac4")
                .seq(285)
                .subject("다음 글 제목")
                .writer(commonWriter)
                .board(Constants.BOARD_TYPE.FOOTBALL.name())
                .build();

        LatestArticle latestArticle = LatestArticle.builder()
                .id("58e9959b807d71113a999c6e")
                .seq(216)
                .writer(commonWriter)
                .subject("작성자의 최근 글 제목")
                .galleries(simpleGalleires)
                .build();

        GetArticleDetailResponse expectResponse = GetArticleDetailResponse.builder()
                .article(articleDetail)
                .prevArticle(prevPost)
                .nextArticle(nextPost)
                .latestArticlesByWriter(Arrays.asList(latestArticle))
                .build();

        when(articleService.getArticleDetail(any(CommonWriter.class), any(Constants.BOARD_TYPE.class), anyInt(), anyBoolean()))
                .thenReturn(ResponseEntity.ok().body(expectResponse));

        mvc.perform(
                get("/api/board/{board}/{seq}", article.getBoard().toLowerCase(), article.getSeq())
                        .header("Cookie", "JSESSIONID=3F0E029648484BEAEF6B5C3578164E99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(expectResponse)))
                .andDo(
                        document("getArticleDetail",
                                requestHeaders(
                                        headerWithName("Cookie").description("(optional) 인증 쿠키. value는 JESSIONID=키값")
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
                                        fieldWithPath("article.writer").type(JsonFieldType.OBJECT).description("글쓴이"),
                                        fieldWithPath("article.subject").type(JsonFieldType.STRING).description("글제목"),
                                        fieldWithPath("article.seq").type(JsonFieldType.NUMBER).description("글번호"),
                                        fieldWithPath("article.category").type(JsonFieldType.OBJECT).description("말머리"),
                                        fieldWithPath("article.views").type(JsonFieldType.NUMBER).description("읽음 수"),
                                        fieldWithPath("article.numberOfLike").type(JsonFieldType.NUMBER).description("좋아요 수"),
                                        fieldWithPath("article.numberOfDislike").type(JsonFieldType.NUMBER).description("싫어요 수"),
                                        fieldWithPath("article.status").type(JsonFieldType.OBJECT).description("글 상태"),
                                        fieldWithPath("article.logs").type(JsonFieldType.ARRAY).description("로그 기록 목록"),
                                        fieldWithPath("article.galleries").type(JsonFieldType.ARRAY).description("그림 목록"),
                                        fieldWithPath("article.myFeeling").type(JsonFieldType.STRING).description("나의 감정 상태. 인증 쿠키가 있고, 감정 표현을 한 경우 포함 된다."),
                                        fieldWithPath("prevArticle").type(JsonFieldType.OBJECT).description("이전 글"),
                                        fieldWithPath("prevArticle.id").type(JsonFieldType.STRING).description("글 ID"),
                                        fieldWithPath("prevArticle.seq").type(JsonFieldType.NUMBER).description("글번호"),
                                        fieldWithPath("prevArticle.writer").type(JsonFieldType.OBJECT).description("글쓴이"),
                                        fieldWithPath("prevArticle.board").type(JsonFieldType.STRING).description("게시판"),
                                        fieldWithPath("nextArticle").type(JsonFieldType.OBJECT).description("다음 글. json 형식은 prevArticle과 같음."),
                                        fieldWithPath("latestArticlesByWriter").type(JsonFieldType.ARRAY).description("글쓴이의 최근 글."),
                                        fieldWithPath("latestArticlesByWriter.[].id").type(JsonFieldType.STRING).description("글 ID"),
                                        fieldWithPath("latestArticlesByWriter.[].seq").type(JsonFieldType.NUMBER).description("글번호"),
                                        fieldWithPath("latestArticlesByWriter.[].writer").type(JsonFieldType.OBJECT).description("글쓴이"),
                                        fieldWithPath("latestArticlesByWriter.[].subject").type(JsonFieldType.STRING).description("글제목"),
                                        fieldWithPath("latestArticlesByWriter.[].galleries").type(JsonFieldType.ARRAY).description("그림 목록")
                                )
                        ));
    }

    @Test
    @WithMockUser
    public void writeArticleTest() throws Exception {

        when(galleryService.findByIdIn(any()))
                .thenReturn(galleries);

        when(articleService.insertArticle(any(CommonWriter.class), any(Constants.BOARD_TYPE.class), anyString(), anyString(), anyString(),
                anyListOf(Gallery.class), any(Constants.DEVICE_TYPE.class)))
                .thenReturn(article);

        doNothing().when(galleryService)
                .processLinkedGalleries(anyListOf(Gallery.class), anyListOf(GalleryOnBoard.class), anyListOf(String.class),
                        any(Constants.GALLERY_FROM_TYPE.class), anyString());

        WriteArticleResponse expectResponse = WriteArticleResponse.builder()
                .seq(article.getSeq())
                .build();

        mvc.perform(
                post("/api/board/{board}", article.getBoard().toLowerCase())
                        .header("Cookie", "JSESSIONID=3F0E029648484BEAEF6B5C3578164E99")
                        //.cookie(new Cookie("JSESSIONID", "3F0E029648484BEAEF6B5C3578164E99")) TODO 이걸로 바꾸고 싶다.
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(ObjectMapperUtils.writeValueAsString(writeArticleform)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(expectResponse)))
                .andDo(
                        document("writeArticle",
                                requestHeaders(
                                        headerWithName("Cookie").description("인증 쿠키. value는 JESSIONID=키값")
                                ),
                                requestFields(this.getWriteArticleFormDescriptor()),
                                pathParameters(
                                        parameterWithName("board").description("게시판 " +
                                                Stream.of(Constants.BOARD_TYPE.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.toList()))
                                ),
                                responseFields(
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
    @WithMockUser
    public void editArticleTest() throws Exception {

        when(galleryService.findByIdIn(any()))
                .thenReturn(galleries);

        when(articleService.updateArticle(any(CommonWriter.class), any(Constants.BOARD_TYPE.class), anyInt(), anyString(), anyString(), anyString(),
                anyListOf(String.class), any(Constants.DEVICE_TYPE.class)))
                .thenReturn(article);

        doNothing().when(galleryService)
                .processLinkedGalleries(anyListOf(Gallery.class), anyListOf(GalleryOnBoard.class), anyListOf(String.class),
                        any(Constants.GALLERY_FROM_TYPE.class), anyString());

        WriteArticleResponse expectResponse = WriteArticleResponse.builder()
                .seq(article.getSeq())
                .build();

        mvc.perform(
                put("/api/board/{board}/{seq}", article.getBoard().toLowerCase(), article.getSeq())
                        .header("Cookie", "JSESSIONID=3F0E029648484BEAEF6B5C3578164E99")
                        //.cookie(new Cookie("JSESSIONID", "3F0E029648484BEAEF6B5C3578164E99")) TODO 이걸로 바꾸고 싶다.
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(ObjectMapperUtils.writeValueAsString(writeArticleform)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(expectResponse)))
                .andDo(
                        document("editArticle",
                                requestHeaders(
                                        headerWithName("Cookie").description("인증 쿠키. value는 JESSIONID=키값")
                                ),
                                requestFields(this.getWriteArticleFormDescriptor()),
                                pathParameters(
                                        parameterWithName("board").description("게시판 " +
                                                Stream.of(Constants.BOARD_TYPE.values()).map(Enum::name).map(String::toLowerCase).collect(Collectors.toList())),
                                        parameterWithName("seq").description("글번호")
                                ),
                                responseFields(
                                        fieldWithPath("seq").type(JsonFieldType.NUMBER).description("글 번호")
                                )
                        ));
    }

    @Test
    @WithMockUser
    public void deleteArticleTest() throws Exception {

        Constants.ARTICLE_DELETE_TYPE expectDeleteType = Constants.ARTICLE_DELETE_TYPE.ALL;

        when(articleService.deleteArticle(any(CommonWriter.class), any(Constants.BOARD_TYPE.class), anyInt()))
                .thenReturn(expectDeleteType);

        DeleteArticleResponse expectResponse = DeleteArticleResponse.builder()
                .result(expectDeleteType)
                .build();

        mvc.perform(
                delete("/api/board/{board}/{seq}", article.getBoard().toLowerCase(), article.getSeq())
                        .header("Cookie", "JSESSIONID=3F0E029648484BEAEF6B5C3578164E99")
                        //.cookie(new Cookie("JSESSIONID", "3F0E029648484BEAEF6B5C3578164E99")) TODO 이걸로 바꾸고 싶다.
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(expectResponse)))
                .andDo(
                        document("deleteArticle",
                                requestHeaders(
                                        headerWithName("Cookie").description("인증 쿠키. value는 JESSIONID=키값")
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

    private FieldDescriptor[] getWriteArticleFormDescriptor() {
        ConstraintDescriptions userConstraints = new ConstraintDescriptions(WriteArticle.class);

        return new FieldDescriptor[] {
                fieldWithPath("subject").type(JsonFieldType.STRING).description("글 제목. " + userConstraints.descriptionsForProperty("subject")),
                fieldWithPath("content").type(JsonFieldType.STRING).description("글 내용. " + userConstraints.descriptionsForProperty("content")),
                fieldWithPath("categoryCode").type(JsonFieldType.STRING)
                        .description("(optional, default ALL) 말머리. board가 FREE 일때에는 무시된다. FOOTBALL, DEVELOPER일 때에는 필수다."),
                fieldWithPath("galleries").type(JsonFieldType.ARRAY).description("(optional) 그림 목록")
        };
    }

}
