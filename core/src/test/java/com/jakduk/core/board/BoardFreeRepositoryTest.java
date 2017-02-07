package com.jakduk.core.board;

import com.jakduk.core.CoreApplicationTests;
import com.jakduk.core.model.db.BoardFree;
import com.jakduk.core.model.db.User;
import com.jakduk.core.model.simple.BoardFreeOfMinimum;
import com.jakduk.core.model.simple.BoardFreeOnRSS;
import com.jakduk.core.model.simple.BoardFreeSimple;
import com.jakduk.core.repository.board.free.BoardFreeRepository;
import com.jakduk.core.repository.user.UserRepository;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * Created by pyohwan on 16. 9. 11.
 */

public class BoardFreeRepositoryTest extends CoreApplicationTests {

    @Autowired
    private BoardFreeRepository sut;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findOneById() {
        BoardFree boardFree = sut.findOneById("54c4df933d96600d7f55a04b").orElse(new BoardFree());
        boardFree.setBatch(null);
        //sut.save(boardFree);

        Assert.assertTrue(! ObjectUtils.isEmpty(boardFree));
    }

    /**
     * PRD 테스트 안됨
     */
    @Ignore
    @Test
    public void findByIdAndUserId() {

        User user = userRepository.findByEmail("test05@test.com");

        List<BoardFreeSimple> boardFrees = sut.findByIdAndUserId(new ObjectId("54c4df933d96600d7f55a04b"), user.getId(), 3);

        Assert.assertTrue(boardFrees.size() > 0);
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
}
