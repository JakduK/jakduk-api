package com.jakduk.core.board;

import com.jakduk.core.CoreApplicationTests;
import com.jakduk.core.model.etc.CommonCount;
import com.jakduk.core.repository.board.free.BoardFreeCommentRepository;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by pyohwanjang on 2017. 3. 27..
 */
public class BoardFreeCommentRepositoryTest extends CoreApplicationTests {

    @Autowired
    private BoardFreeCommentRepository sut;

    @Test
    public void findCommentsCountByIds() {
        List<CommonCount> commentCounts = sut.findCommentsCountByIds(Arrays.asList(new ObjectId("58d62a6c807d714ce35a75ba")));

        Assert.assertTrue(Objects.nonNull(commentCounts));
    }
}
