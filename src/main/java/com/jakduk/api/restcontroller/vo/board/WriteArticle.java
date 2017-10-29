package com.jakduk.api.restcontroller.vo.board;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * 글쓰기/글고치기 폼"
 *
 * @author pyohwan
 *         16. 7. 16 오후 7:55
 */

@Getter
@Builder
public class WriteArticle {

    @Size(min = 1, max=60)
    @NotEmpty
    private String subject; // 글 제목

    @Size(min = 5)
    @NotEmpty
    private String content; // 글 내용

    private String categoryCode; // 말머리 코드
    private List<GalleryOnBoard> galleries; // 사진 목록

}
