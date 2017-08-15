package com.jakduk.api.board;

import com.jakduk.api.common.Constants;
import com.jakduk.api.common.util.DateUtils;
import com.jakduk.api.configuration.JakdukProperties;
import com.jakduk.api.configuration.MongodbConfig;
import com.jakduk.api.model.db.BoardFree;
import com.jakduk.api.model.etc.BoardFeelingCount;
import com.jakduk.api.model.simple.BoardFreeOnList;
import com.jakduk.api.model.simple.BoardFreeOnRSS;
import com.jakduk.api.model.simple.BoardFreeOnSitemap;
import com.jakduk.api.repository.board.free.BoardFreeRepository;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RunWith(SpringRunner.class)
@DataMongoTest
@Import({JakdukProperties.class, MongodbConfig.class})
public class BoardFreeRepositoryTests {

    @Autowired
    private BoardFreeRepository repository;

    private BoardFree randomBoardFree;
    private Constants.BOARD_TYPE board;

    @Before
    public void setUp(){
        BoardFree firstBoardFree = repository.findTopByOrderByIdAsc().get();
        LocalDateTime localDateTime = DateUtils.dateToLocalDateTime(new ObjectId(firstBoardFree.getId()).getDate());
        Long hours = ChronoUnit.MINUTES.between(localDateTime, LocalDateTime.now());
        LocalDateTime randomDate = localDateTime.plusMinutes(new Random().nextInt((int) (hours + 1)));
        randomBoardFree = repository.findTopByIdLessThanEqualOrderByIdDesc(new ObjectId(DateUtils.localDateTimeToDate(randomDate))).get();

        board = Constants.BOARD_TYPE.FOOTBALL;
    }

    @Test
    public void findOneById() {
        BoardFree boardFree = repository.findOneById(randomBoardFree.getId()).get();

        Assert.assertTrue(randomBoardFree.getId().equals(boardFree.getId()));
    }

    @Test
    public void findOneBySeq() {
        BoardFree boardFree = repository.findOneBySeq(randomBoardFree.getSeq()).get();

        Assert.assertTrue(randomBoardFree.getSeq().equals(boardFree.getSeq()));
    }

    @Test
    public void findPostsOnRss() {
        List<BoardFreeOnRSS> posts = repository.findPostsOnRss(null,
                new Sort(Sort.Direction.DESC, Collections.singletonList("_id")), 10);

        Assert.assertTrue(! CollectionUtils.isEmpty(posts));
    }

    @Test
    public void findNotices() {
        Sort sort = new Sort(Sort.Direction.DESC, Collections.singletonList("_id"));
        List<BoardFreeOnList> notices = repository.findNotices(board, sort);

        System.out.println("notices=" + notices);
    }

    @Test
    public void findPostsOnSitemap() {

        List<BoardFreeOnSitemap> posts = repository.findPostsOnSitemap(null,
                new Sort(Sort.Direction.DESC, Collections.singletonList("_id")), 10);

        Assert.assertTrue(! CollectionUtils.isEmpty(posts));
    }

    @Test
    public void findByIdInAndLinkedGalleryIsTrue() {
        List<BoardFree> posts = repository.findByIdInAndLinkedGalleryIsTrue(Arrays.asList(randomBoardFree.getId()));
    }

    @Test
    public void findUsersFeelingCountTest() {
        List<BoardFeelingCount> feelingCounts = repository.findUsersFeelingCount(Arrays.asList(new ObjectId(randomBoardFree.getId())));

        BoardFeelingCount boardFeelingCount = feelingCounts.stream()
                .findFirst()
                .orElseThrow(NoSuchElementException::new);

        Assert.assertTrue(boardFeelingCount.getId().equals(randomBoardFree.getId()));
        Assert.assertTrue(boardFeelingCount.getUsersLikingCount().equals(CollectionUtils.isEmpty(randomBoardFree.getUsersLiking()) ? 0 : randomBoardFree.getUsersLiking().size()));
        Assert.assertTrue(boardFeelingCount.getUsersDislikingCount().equals(CollectionUtils.isEmpty(randomBoardFree.getUsersDisliking()) ? 0 : randomBoardFree.getUsersDisliking().size()));
    }

}
