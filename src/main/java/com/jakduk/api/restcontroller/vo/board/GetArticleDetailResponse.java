package com.jakduk.api.restcontroller.vo.board;

import com.jakduk.api.model.simple.ArticleSimple;

import java.util.List;

/**
 * @author pyohwan
 *         16. 7. 16 오후 7:28
 */

public class GetArticleDetailResponse {

    private ArticleDetail article; // 글 상세
    private ArticleSimple prevArticle; // 앞 글
    private ArticleSimple nextArticle; // 뒷 글
    private List<LatestArticle> latestArticlesByWriter; // 작성자의 최근 글

    public ArticleDetail getArticle() {
        return article;
    }

    public void setArticle(ArticleDetail article) {
        this.article = article;
    }

    public ArticleSimple getPrevArticle() {
        return prevArticle;
    }

    public void setPrevArticle(ArticleSimple prevArticle) {
        this.prevArticle = prevArticle;
    }

    public ArticleSimple getNextArticle() {
        return nextArticle;
    }

    public void setNextArticle(ArticleSimple nextArticle) {
        this.nextArticle = nextArticle;
    }

    public List<LatestArticle> getLatestArticlesByWriter() {
        return latestArticlesByWriter;
    }

    public void setLatestArticlesByWriter(List<LatestArticle> latestArticlesByWriter) {
        this.latestArticlesByWriter = latestArticlesByWriter;
    }
}
