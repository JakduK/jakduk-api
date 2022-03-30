package com.jakduk.api.restcontroller.vo.board;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 댓글 달기 / 댓글 고치기 폼
 *
 * @author pyohwan
 * 16. 3. 13 오후 11:05
 */

public class WriteArticleComment {

    @NotEmpty
    @Size(min = 1, max=800)
    private String content; // 댓글 내용

    private List<GalleryOnBoard> galleries; // 사진 목록

    public String getContent() {
        return content;
    }

    public List<GalleryOnBoard> getGalleries() {
        return galleries;
    }

    @Override
    public String toString() {
        return "WriteArticleComment{" +
                "content='" + content + '\'' +
                ", galleries=" + galleries +
                '}';
    }
}
