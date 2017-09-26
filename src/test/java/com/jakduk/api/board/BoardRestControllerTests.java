package com.jakduk.api.board;

import com.jakduk.api.TestMvcConfig;
import com.jakduk.api.common.AuthHelper;
import com.jakduk.api.common.Constants;
import com.jakduk.api.common.board.category.BoardCategory;
import com.jakduk.api.common.board.category.BoardCategoryGenerator;
import com.jakduk.api.common.util.JakdukUtils;
import com.jakduk.api.common.util.ObjectMapperUtils;
import com.jakduk.api.model.aggregate.BoardTop;
import com.jakduk.api.model.db.Article;
import com.jakduk.api.model.db.Gallery;
import com.jakduk.api.model.embedded.ArticleCommentStatus;
import com.jakduk.api.model.embedded.ArticleStatus;
import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.model.simple.ArticleOnSearch;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BoardRestController.class)
@Import(TestMvcConfig.class)
@AutoConfigureRestDocs(outputDir = "build/snippets")
public class BoardRestControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean private RestTemplateBuilder restTemplateBuilder;
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
                .status(ArticleStatus.builder().notice(false).delete(false).device(Constants.DEVICE_TYPE.NORMAL).build())
                .build();

        List<BoardCategory> categories = BoardCategoryGenerator.getCategories(Constants.BOARD_TYPE.FOOTBALL, JakdukUtils.getLocale());

        boardCategory = categories.get(0);

        categoriesMap = categories.stream()
                .collect(Collectors.toMap(BoardCategory::getCode, boardCategory -> boardCategory.getNames().get(0).getName()));

        categoriesMap.put("ALL", JakdukUtils.getResourceBundleMessage("messages.board", "board.category.all"));
    }

    @Test
    @WithMockUser
    public void getArticlesTest() throws Exception {

        BoardGallerySimple gallerySimple = BoardGallerySimple.builder()
                .id("58b9050b807d714eaf50a111")
                .thumbnailUrl("https://dev-api.jakduk.com//gallery/thumbnail/58b9050b807d714eaf50a111")
                .build();

        GetArticle article = GetArticle.builder()
                .id("58b7b9dd716dce06b10e449a")
                .board(Constants.BOARD_TYPE.FOOTBALL.name())
                .writer(commonWriter)
                .subject("글 제목입니다.")
                .seq(2)
                .category(boardCategory.getCode())
                .views(10)
                .status(new ArticleStatus(false, false, Constants.DEVICE_TYPE.NORMAL))
                .galleries(Arrays.asList(gallerySimple))
                .shortContent("본문입니다. (100자)")
                .commentCount(5)
                .likingCount(3)
                .dislikingCount(1)
                .build();

        GetArticle notice = GetArticle.builder()
                .id("58b7b9dd716dce06b10e449a")
                .board(Constants.BOARD_TYPE.FOOTBALL.name())
                .writer(commonWriter)
                .subject("공지글 제목입니다.")
                .seq(3)
                .category(boardCategory.getCode())
                .views(15)
                .status(new ArticleStatus(true, false, Constants.DEVICE_TYPE.NORMAL))
                .galleries(Arrays.asList(gallerySimple))
                .shortContent("본문입니다. (100자)")
                .commentCount(8)
                .likingCount(10)
                .dislikingCount(2)
                .build();

        GetArticlesResponse expectResponse = GetArticlesResponse.builder()
                .categories(categoriesMap)
                .articles(Arrays.asList(article))
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
                                        parameterWithName("categoryCode").description("(optional, default ALL) 말머리. board가 FREE 일때에는 무시한다. FOOTBALL, DEVELOPER일 때에는 필수다.").optional()
                                ),
                                responseFields(
                                        fieldWithPath("categories").type(JsonFieldType.OBJECT).description("말머리 맵. key는 말머리코드, value는 표시되는 이름(다국어 지원)"),
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
    public void getFreePostsTopsTest() throws Exception {

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
    public void getFreeCommentsTest() throws Exception {

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

    @Test
    @WithMockUser
    public void getFreePostTest() throws Exception {

        ArticleDetail articleDetail = new ArticleDetail();
        BeanUtils.copyProperties(article, articleDetail);
        articleDetail.setCategory(boardCategory);
        articleDetail.setNumberOfLike(5);
        articleDetail.setNumberOfDislike(4);
        articleDetail.setGalleries(
                Arrays.asList(
                        ArticleGallery.builder()
                        .id("boardGalleryId01")
                        .name("성남FC 시즌권 사진")
                        .imageUrl("https://dev-api.jakduk.com//gallery/58b9050b807d714eaf50a111")
                        .thumbnailUrl("https://dev-api.jakduk.com//gallery/thumbnail/58b9050b807d714eaf50a111")
                        .build()
                )
        );

        ArticleSimple prevPost = ArticleSimple.builder()
                .id("boardFreeId02")
                .seq(2)
                .subject("이전 글 제목")
                .writer(commonWriter)
                .build();

        ArticleSimple nextPost = ArticleSimple.builder()
                .id("boardFreeId03")
                .seq(3)
                .subject("다음 글 제목")
                .writer(commonWriter)
                .build();

        GetArticleDetailResponse response = GetArticleDetailResponse.builder()
                .article(articleDetail)
                .prevArticle(prevPost)
                .nextArticle(nextPost)
                .build();

        when(articleService.getArticleDetail(anyString(), anyInt(), anyBoolean()))
                .thenReturn(ResponseEntity.ok().body(response));

        mvc.perform(get("/api/board/free/{seq}", article.getSeq())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(response)));
    }

    @Test
    @WithMockUser
    public void addFreePostTest() throws Exception {

        GalleryOnBoard galleryOnBoard = GalleryOnBoard.builder().id("galleryid01").name("공차는사진").build();

        WriteArticle form = WriteArticle.builder()
                .subject("제목입니다.")
                .content("내용입니다.")
                .categoryCode("CLASSIC")
                .galleries(Arrays.asList(galleryOnBoard))
                .build();

        when(authHelper.getCommonWriter(any(Authentication.class)))
                .thenReturn(commonWriter);

        List<Gallery> expectGalleries = Arrays.asList(
                Gallery.builder()
                        .id(galleryOnBoard.getId())
                        .name(galleryOnBoard.getName())
                        .fileName("galleryFileName01")
                        .contentType("image/jpeg")
                        .writer(commonWriter)
                        .hash("HEXVALUE")
                        .build()
        );

        when(galleryService.findByIdIn(any()))
                .thenReturn(expectGalleries);

        when(articleService.insertArticle(any(CommonWriter.class), any(Constants.BOARD_TYPE.class), anyString(), anyString(), anyString(),
                anyListOf(Gallery.class), any(Constants.DEVICE_TYPE.class)))
                .thenReturn(article);

        doNothing().when(galleryService)
                .processLinkedGalleries(anyListOf(Gallery.class), anyListOf(GalleryOnBoard.class), anyListOf(String.class),
                        any(Constants.GALLERY_FROM_TYPE.class), anyString());

        WriteArticleResponse expectResponse = WriteArticleResponse.builder()
                .seq(article.getSeq())
                .build();

        mvc.perform(post("/api/board/free")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapperUtils.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(expectResponse)));
    }

}
