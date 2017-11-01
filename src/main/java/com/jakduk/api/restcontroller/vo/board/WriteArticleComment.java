package com.jakduk.api.restcontroller.vo.board;

import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * 댓글 달기 / 댓글 고치기 폼
 *
 * @author pyohwan
 * 16. 3. 13 오후 11:05
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class WriteArticleComment {

    @NotEmpty
    @Size(min = 1, max=800)
    private String content; // 댓글 내용

    private List<GalleryOnBoard> galleries; // 사진 목록

}
