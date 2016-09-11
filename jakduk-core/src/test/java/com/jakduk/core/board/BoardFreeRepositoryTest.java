package com.jakduk.core.board;

import com.jakduk.core.common.CommonConst;
import com.jakduk.core.model.db.BoardFree;
import com.jakduk.core.repository.BoardFreeRepository;
import com.jakduk.core.util.AbstractSpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * Created by pyohwan on 16. 9. 11.
 */

public class BoardFreeRepositoryTest extends AbstractSpringTest {

    @Autowired
    private BoardFreeRepository sut;

    @Test
    public void test() {
        BoardFree boardFree = sut.findOneById("54c4df933d96600d7f55a04b").orElse(new BoardFree());

        boardFree.setBatch(null);
        //sut.save(boardFree);
    }

}
