package com.jakduk.api.board;

import com.jakduk.api.TestMvcConfig;
import com.jakduk.api.common.AuthHelper;
import com.jakduk.api.common.JakdukConst;
import com.jakduk.api.common.util.ObjectMapperUtils;
import com.jakduk.api.model.db.BoardFree;
import com.jakduk.api.model.db.Gallery;
import com.jakduk.api.model.embedded.BoardCommentStatus;
import com.jakduk.api.model.embedded.BoardStatus;
import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.model.jongo.BoardFreeOnBest;
import com.jakduk.api.model.simple.BoardFreeOnSearch;
import com.jakduk.api.restcontroller.BoardRestController;
import com.jakduk.api.restcontroller.vo.board.*;
import com.jakduk.api.service.BoardCategoryService;
import com.jakduk.api.service.BoardFreeService;
import com.jakduk.api.service.GalleryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BoardRestController.class)
@Import(TestMvcConfig.class)
public class BoardRestControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean private BoardFreeService boardFreeService;
    @MockBean private BoardCategoryService boardCategoryService;
    @MockBean private GalleryService galleryService;
    @MockBean private AuthHelper authHelper;

    private CommonWriter commonWriter;
    private BoardFree boardFree;

    @Before
    public void setUp(){
        commonWriter = CommonWriter.builder()
                .userId("userid01")
                .username("user01")
                .providerId(JakdukConst.ACCOUNT_TYPE.JAKDUK)
                .build();

        boardFree = BoardFree.builder()
                .id("boardFreeId01")
                .seq(1)
                .writer(commonWriter)
                .subject("제목입니다.")
                .content("내용입니다.")
                .category(JakdukConst.BOARD_CATEGORY_TYPE.FOOTBALL)
                .linkedGallery(true)
                .status(BoardStatus.builder().notice(false).delete(false).device(JakdukConst.DEVICE_TYPE.NORMAL).build())
                .build();
    }

    /**
     * TODO content json 이 맞는지 검증은 안했는데 넣는게 나을듯
     */
    @Test
    @WithMockUser
    public void getFreePostsTest() throws Exception {
        mvc.perform(get("/api/board/free/posts")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser
    public void getFreePostsTopsTest() throws Exception {

        List<BoardFreeOnBest> expectTopLikes = Arrays.asList(
                BoardFreeOnBest.builder()
                        .id("boardFreeId01")
                        .seq(1)
                        .subject("인기있는 글 제목")
                        .count(5)
                        .views(100)
                        .build()
        );

        when(boardFreeService.getFreeTopLikes())
                .thenReturn(expectTopLikes);

        List<BoardFreeOnBest> expectTopComments = Arrays.asList(
                BoardFreeOnBest.builder()
                        .id("boardFreeId02")
                        .seq(2)
                        .subject("댓글많은 글 제목")
                        .count(10)
                        .views(150)
                        .build()
        );

        when(boardFreeService.getFreeTopComments())
                .thenReturn(expectTopComments);

        FreeTopsResponse response = FreeTopsResponse.builder()
                .topLikes(expectTopLikes)
                .topComments(expectTopComments)
                .build();

        mvc.perform(get("/api/board/free/tops")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(response)));
    }

    @Test
    @WithMockUser
    public void getFreeCommentsTest() throws Exception {

        FreePostCommentsResponse response = FreePostCommentsResponse.builder()
                .comments(
                        Arrays.asList(
                                FreePostComment.builder()
                                        .id("boardFreeCommentId01")
                                        .boardItem(
                                                BoardFreeOnSearch.builder()
                                                        .id(boardFree.getId())
                                                        .seq(boardFree.getSeq())
                                                        .subject(boardFree.getSubject())
                                                        .build())
                                        .writer(commonWriter)
                                        .content("댓글 내용입니다.")
                                        .status(new BoardCommentStatus(JakdukConst.DEVICE_TYPE.NORMAL))
                                        .numberOfDislike(5)
                                        .numberOfDislike(3)
                                        .build()
                        )
                )
                .last(true)
                .first(true)
                .totalPages(1)
                .size(10)
                .number(0)
                .numberOfElements(1)
                .totalElements(1)
                .build();

        when(boardFreeService.getBoardFreeComments(anyInt(), anyInt()))
                .thenReturn(response);

        mvc.perform(get("/api/board/free/comments")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser
    public void addFreePostTest() throws Exception {

        GalleryOnBoard galleryOnBoard = GalleryOnBoard.builder().id("galleryid01").name("공차는사진").build();

        FreePostForm form = FreePostForm.builder()
                .subject("제목입니다.")
                .content("내용입니다.")
                .categoryCode(JakdukConst.BOARD_CATEGORY_TYPE.FOOTBALL)
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

        when(boardFreeService.insertFreePost(any(CommonWriter.class), anyString(), anyString(), any(JakdukConst.BOARD_CATEGORY_TYPE.class),
                anyListOf(Gallery.class), any(JakdukConst.DEVICE_TYPE.class)))
                .thenReturn(boardFree);

        doNothing().when(galleryService)
                .processLinkedGalleries(anyListOf(Gallery.class), anyListOf(GalleryOnBoard.class), anyListOf(String.class),
                        any(JakdukConst.GALLERY_FROM_TYPE.class), anyString());

        FreePostWriteResponse expectResponse = FreePostWriteResponse.builder()
                .seq(boardFree.getSeq())
                .build();

        mvc.perform(post("/api/board/free")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapperUtils.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(expectResponse)));
    }

}
