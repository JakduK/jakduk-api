package com.jakduk.api.restcontroller.vo.home;

import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.model.simple.ArticleSimple;

public class HomeArticleComment {
    private String id;
    private ArticleSimple article;
    private CommonWriter writer;
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArticleSimple getArticle() {
        return article;
    }

    public void setArticle(ArticleSimple article) {
        this.article = article;
    }

    public CommonWriter getWriter() {
        return writer;
    }

    public void setWriter(CommonWriter writer) {
        this.writer = writer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
