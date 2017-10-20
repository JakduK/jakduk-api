package com.jakduk.api.restcontroller.vo.home;

import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.model.simple.ArticleSimple;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class HomeArticleComment {
    private String id;
    private ArticleSimple article;
    private CommonWriter writer;
    private String content;
}
