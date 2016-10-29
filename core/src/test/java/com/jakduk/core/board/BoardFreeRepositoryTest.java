package com.jakduk.core.board;

import com.jakduk.core.model.db.BoardFree;
import com.jakduk.core.model.db.User;
import com.jakduk.core.model.simple.BoardFreeSimple;
import com.jakduk.core.repository.board.free.BoardFreeRepository;
import com.jakduk.core.repository.user.UserRepository;
import com.jakduk.core.util.AbstractSpringTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by pyohwan on 16. 9. 11.
 */

public class BoardFreeRepositoryTest extends AbstractSpringTest {

    @Autowired
    private BoardFreeRepository sut;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void test() {
        BoardFree boardFree = sut.findOneById("54c4df933d96600d7f55a04b").orElse(new BoardFree());

        boardFree.setBatch(null);
        //sut.save(boardFree);
    }

    @Test
    public void findByWriter() {

        User user = userRepository.findByEmail("test05@test.com");

        List<BoardFreeSimple> boardFrees = sut.findByUserId(user.getId(), 3);

        Assert.assertTrue(boardFrees.size() > 0);
    }
}
