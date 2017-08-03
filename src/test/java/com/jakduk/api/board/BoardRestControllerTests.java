package com.jakduk.api.board;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jakduk.api.common.JakdukConst;
import com.jakduk.api.common.util.ObjectMapperUtils;
import com.jakduk.api.model.db.User;
import com.jakduk.api.restcontroller.BoardRestController;
import com.jakduk.api.restcontroller.vo.board.FreePostForm;
import com.jakduk.api.restcontroller.vo.user.UserForm;
import com.jakduk.api.service.BoardCategoryService;
import com.jakduk.api.service.BoardFreeService;
import com.jakduk.api.service.GalleryService;
import org.elasticsearch.common.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceWebArgumentResolver;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BoardRestController.class)
public class BoardRestControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean private BoardFreeService boardFreeService;
    @MockBean private BoardCategoryService boardCategoryService;
    @MockBean private GalleryService galleryService;

    @MockBean
    private Device device;

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
        FreePostForm form = FreePostForm.builder()
                .subject("제목입니다.")
                .content("내용입니다.")
                .categoryCode(JakdukConst.BOARD_CATEGORY_TYPE.FOOTBALL)
                .build();

//        User expectUser = User.builder()
//                .id("abcdef")
//                .email(form.getEmail())
//                .username(form.getUsername())
//                .build();
//
//        given(userService.addJakdukUser(form.getEmail(), form.getUsername(), passwordEncoder.encode(form.getPassword()), null, null, null))
//                .willReturn(expectUser);

        mvc.perform(post("/api/board/free")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapperUtils.writeValueAsString(form)))
                .andExpect(status().isOk());
    }
}
