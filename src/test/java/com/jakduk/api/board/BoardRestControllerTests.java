package com.jakduk.api.board;

import com.jakduk.api.common.JakdukConst;
import com.jakduk.api.common.util.JakdukUtils;
import com.jakduk.api.common.util.ObjectMapperUtils;
import com.jakduk.api.model.db.BoardFree;
import com.jakduk.api.model.db.Gallery;
import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.restcontroller.BoardRestController;
import com.jakduk.api.restcontroller.vo.board.FreePostForm;
import com.jakduk.api.restcontroller.vo.board.FreePostWriteResponse;
import com.jakduk.api.restcontroller.vo.board.GalleryOnBoard;
import com.jakduk.api.service.BoardCategoryService;
import com.jakduk.api.service.BoardFreeService;
import com.jakduk.api.service.GalleryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
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
@Import(CustomizationConfiguration.class)
public class BoardRestControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean private BoardFreeService boardFreeService;
    @MockBean private BoardCategoryService boardCategoryService;
    @MockBean private GalleryService galleryService;

    @Test
    @WithMockUser
    public void getFreePostsTest() throws Exception {
        mvc.perform(get("/api/board/free/posts")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

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

        CommonWriter writer = CommonWriter.builder().userId("userid01").username("user01").providerId(JakdukConst.ACCOUNT_TYPE.JAKDUK).build();

        List<Gallery> expectGalleries = Arrays.asList(
                Gallery.builder()
                        .id(galleryOnBoard.getId())
                        .name(galleryOnBoard.getName())
                        .fileName("galleryFileName01")
                        .contentType("image/jpeg")
                        .writer(writer)
                        .hash("HEXVALUE")
                        .build()
        );


        when(galleryService.findByIdIn(any()))
                .thenReturn(expectGalleries);

        BoardFree expectBoardFree = BoardFree.builder()
                .id("abcdef")
                .seq(1)
                .writer(writer)
                .subject(form.getSubject())
                .content(form.getContent())
                .category(form.getCategoryCode())
                .linkedGallery(true)
                .build();

        when(boardFreeService.insertFreePost(any(), anyString(), anyString(), any(JakdukConst.BOARD_CATEGORY_TYPE.class),
                anyListOf(Gallery.class), any(JakdukConst.DEVICE_TYPE.class)))
                .thenReturn(expectBoardFree);

        doNothing().when(galleryService)
                .processLinkedGalleries(anyListOf(Gallery.class), anyListOf(GalleryOnBoard.class), anyListOf(String.class),
                        any(JakdukConst.GALLERY_FROM_TYPE.class), anyString());

        FreePostWriteResponse expectResponse = FreePostWriteResponse.builder()
                .seq(expectBoardFree.getSeq())
                .build();

        mvc.perform(post("/api/board/free")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapperUtils.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(expectResponse)));
    }

}
