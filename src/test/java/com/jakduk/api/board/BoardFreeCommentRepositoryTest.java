package com.jakduk.api.board;


import com.jakduk.api.ApiApplicationTests;
import com.jakduk.api.model.db.BoardFreeComment;
import com.jakduk.api.model.aggregate.CommonCount;
import com.jakduk.api.repository.board.free.comment.BoardFreeCommentRepository;
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
public class BoardFreeCommentRepositoryTest extends ApiApplicationTests {

    @Autowired
    private BoardFreeCommentRepository sut;

    @Test
    public void findCommentsCountByIds() {
        List<CommonCount> commentCounts = sut.findCommentsCountByIds(Arrays.asList(new ObjectId("58d62a6c807d714ce35a75ba")));

        Assert.assertTrue(Objects.nonNull(commentCounts));
    }

    @Test
    public void findByBoardSeqAndGTId() {

        Integer boardSeq = 13;
        ObjectId commentId = new ObjectId("54b916d73d965cb1dbdd4af6");

        List<BoardFreeComment> comments = sut.findByBoardSeqAndGTId(boardSeq, null);

        Assert.assertTrue(Objects.nonNull(comments));

    }

}
