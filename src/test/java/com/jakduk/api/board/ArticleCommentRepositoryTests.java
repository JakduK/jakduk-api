package com.jakduk.api.board;


import com.jakduk.api.common.util.DateUtils;
import com.jakduk.api.configuration.JakdukProperties;
import com.jakduk.api.configuration.MongodbConfig;
import com.jakduk.api.model.aggregate.CommonCount;
import com.jakduk.api.model.db.ArticleComment;
import com.jakduk.api.repository.article.ArticleCommentRepository;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by pyohwanjang on 2017. 3. 27..
 */
@RunWith(SpringRunner.class)
@DataMongoTest
@EnableConfigurationProperties
@Import({JakdukProperties.class, MongodbConfig.class})
public class ArticleCommentRepositoryTests {

    @Autowired
    private ArticleCommentRepository repository;

    private ArticleComment randomArticleComment;

    @Before
    public void setUp(){
        ArticleComment articleComment = repository.findTopByOrderByIdAsc().get();
        LocalDateTime localDateTime = DateUtils.dateToLocalDateTime(new ObjectId(articleComment.getId()).getDate());
        Long hours = ChronoUnit.MINUTES.between(localDateTime, LocalDateTime.now());
        LocalDateTime randomDate = localDateTime.plusMinutes(new Random().nextInt((int) (hours + 1)));
        randomArticleComment = repository.findTopByIdLessThanEqualOrderByIdDesc(new ObjectId(DateUtils.localDateTimeToDate(randomDate))).get();
    }

    @Test
    public void findCommentsCountByIds() {
        List<CommonCount> commentCounts = repository.findCommentsCountByIds(Arrays.asList(new ObjectId(randomArticleComment.getArticle().getId())));

        Assert.assertTrue(! CollectionUtils.isEmpty(commentCounts));
    }

    @Test
    public void findByBoardSeqAndGTId() {

        List<ArticleComment> comments = repository.findByBoardSeqAndGTId(randomArticleComment.getArticle().getBoard(), randomArticleComment.getArticle().getSeq(), null);

        Assert.assertTrue(! CollectionUtils.isEmpty(comments));

    }

}
