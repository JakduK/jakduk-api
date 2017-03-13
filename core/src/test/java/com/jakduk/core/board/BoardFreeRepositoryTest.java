package com.jakduk.core.board;

import com.jakduk.core.CoreApplicationTests;
import com.jakduk.core.model.db.BoardFree;
import com.jakduk.core.model.simple.BoardFreeOfMinimum;
import com.jakduk.core.model.simple.BoardFreeOnRSS;
import com.jakduk.core.model.simple.BoardFreeOnSitemap;
import com.jakduk.core.repository.board.free.BoardFreeRepository;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by pyohwan on 16. 9. 11.
 */

public class BoardFreeRepositoryTest extends CoreApplicationTests {

    @Autowired
    private BoardFreeRepository sut;

    @Test
    public void findOneById() {
        BoardFree boardFree = sut.findOneById("58c55b770fc0bc04fe5e5db6").orElse(new BoardFree());
        boardFree.setBatch(null);
        //sut.save(boardFree);

        Assert.assertTrue(! ObjectUtils.isEmpty(boardFree));
    }

    @Test
    public void findOneBySeq() {
        BoardFree boardFree = sut.findOneBySeq(187).orElse(new BoardFree());

        Assert.assertTrue(Objects.nonNull(boardFree));
    }

    @Test
    public void findBoardFreeOfMinimumBySeq() {
        BoardFreeOfMinimum boardFreeOnComment = sut.findBoardFreeOfMinimumBySeq(58);

//        Assert.assertTrue(! ObjectUtils.isEmpty(boardFreeOnComment));
    }

    @Test
    public void findPostsWithRss() {
        List<BoardFreeOnRSS> posts = sut.findPostsOnRss();

        Assert.assertTrue(posts.size() > 0);
    }

    @Test
    public void findNotices() {

        Sort sort = new Sort(Sort.Direction.DESC, Collections.singletonList("_id"));
        sut.findNotices(sort);
    }

    @Test
    public void findPostsOnSitemap() {

        List<BoardFreeOnSitemap> posts = sut.findPostsOnSitemap(new ObjectId("58c0189e807d711ab5e1a72d"),
                new Sort(Sort.Direction.DESC, Collections.singletonList("_id")), 10);

        Assert.assertTrue(! ObjectUtils.isEmpty(posts));
    }

}
