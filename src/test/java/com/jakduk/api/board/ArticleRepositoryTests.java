package com.jakduk.api.board;

import com.jakduk.api.common.Constants;
import com.jakduk.api.common.util.DateUtils;
import com.jakduk.api.configuration.JakdukProperties;
import com.jakduk.api.configuration.MongodbConfig;
import com.jakduk.api.model.aggregate.BoardFeelingCount;
import com.jakduk.api.model.aggregate.BoardTop;
import com.jakduk.api.model.db.Article;
import com.jakduk.api.model.simple.ArticleOnList;
import com.jakduk.api.model.simple.ArticleOnRSS;
import com.jakduk.api.model.simple.ArticleOnSitemap;
import com.jakduk.api.repository.article.ArticleRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
@EnableConfigurationProperties
@Import({JakdukProperties.class, MongodbConfig.class})
public class ArticleRepositoryTests {

    @Autowired
    private ArticleRepository repository;

    private Article randomArticle;
    private Constants.BOARD_TYPE board;

    @BeforeEach
    public void setUp(){
        Article firstArticle = repository.findTopByOrderByIdAsc().get();
        LocalDateTime localDateTime = DateUtils.dateToLocalDateTime(new ObjectId(firstArticle.getId()).getDate());
        Long hours = ChronoUnit.MINUTES.between(localDateTime, LocalDateTime.now());
        LocalDateTime randomDate = localDateTime.plusMinutes(new Random().nextInt((int) (hours + 1)));
        randomArticle = repository.findTopByIdLessThanEqualOrderByIdDesc(new ObjectId(DateUtils.localDateTimeToDate(randomDate))).get();

        board = Constants.BOARD_TYPE.FOOTBALL;
    }

    @Test
    public void findOneById() {
        Article article = repository.findOneById(randomArticle.getId()).get();

        assertTrue(randomArticle.getId().equals(article.getId()));
    }

    @Test
    public void findOneBySeq() {
        Article article = repository.findOneByBoardAndSeq(randomArticle.getBoard(), randomArticle.getSeq()).get();

        assertTrue(randomArticle.getSeq().equals(article.getSeq()));
    }

    @Test
    public void findPostsOnRss() {
        List<ArticleOnRSS> posts = repository.findPostsOnRss(null, Constants.SORT_BY_ID_DESC, 10);

        assertTrue(! CollectionUtils.isEmpty(posts));
    }

    @Test
    public void findNotices() {
        Sort sort = Sort.by(Sort.Direction.DESC, "_id");
        List<ArticleOnList> notices = repository.findNotices(sort);
    }

    @Test
    public void findPostsOnSitemap() {

        List<ArticleOnSitemap> posts = repository.findSitemapArticles(null,
                Sort.by(Sort.Direction.DESC, "_id"), 10);

        assertTrue(! CollectionUtils.isEmpty(posts));
    }

    @Test
    public void findByIdInAndLinkedGalleryIsTrue() {
        List<Article> posts = repository.findByIdInAndLinkedGalleryIsTrue(Arrays.asList(randomArticle.getId()));
    }

    @Test
    public void findUsersFeelingCountTest() {
        List<BoardFeelingCount> feelingCounts = repository.findUsersFeelingCount(Arrays.asList(new ObjectId(randomArticle.getId())));

        BoardFeelingCount boardFeelingCount = feelingCounts.stream()
                .findFirst()
                .orElseThrow(NoSuchElementException::new);

        assertTrue(boardFeelingCount.getId().equals(randomArticle.getId()));
        assertTrue(boardFeelingCount.getUsersLikingCount().equals(CollectionUtils.isEmpty(randomArticle.getUsersLiking()) ? 0 : randomArticle.getUsersLiking().size()));
        assertTrue(boardFeelingCount.getUsersDislikingCount().equals(CollectionUtils.isEmpty(randomArticle.getUsersDisliking()) ? 0 : randomArticle.getUsersDisliking().size()));
    }

    @Test
    public void findTopLikes() {
        LocalDate localDate = LocalDate.now().minusWeeks(1);

        List<BoardTop> topLikes = repository.findTopLikes(Constants.BOARD_TYPE.FOOTBALL, new ObjectId(DateUtils.localDateToDate(localDate)));
    }

}
