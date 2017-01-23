package com.jakduk.core.board;

import com.jakduk.core.model.etc.CommonCount;
import com.jakduk.core.repository.board.free.BoardFreeCommentRepository;
import com.jakduk.core.util.AbstractSpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pyohwan on 17. 1. 23.
 */
public class BoardFreeCommentRepositoryTest extends AbstractSpringTest {

    @Autowired
    private BoardFreeCommentRepository sut;

    @Test
    public void findCommentsCountBySeqs() {
        ArrayList<Integer> arrTemp = new ArrayList<>();
        arrTemp.add(178);
        arrTemp.add(180);

        List<CommonCount> numberOfItems = sut.findCommentsCountBySeqs(arrTemp);
    }
}
