package com.jakduk.batch.processor;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.common.util.DateUtils;
import com.jakduk.core.model.db.BoardFreeComment;
import org.bson.types.ObjectId;
import org.springframework.batch.item.ItemProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BoardFreeCommentAddLastUpdatedProcessor implements ItemProcessor<BoardFreeComment, BoardFreeComment> {

    @Override
    public BoardFreeComment process(BoardFreeComment item) throws Exception {

        // item ID에서 date를 뽑아온다.
        ObjectId objectId = new ObjectId(item.getId());
        item.setLastUpdated(DateUtils.dateToLocalDateTime(objectId.getDate()));

        List<CoreConst.BATCH_TYPE> batchList = Optional.ofNullable(item.getBatch())
                .orElseGet(ArrayList::new);

        if (batchList.stream().noneMatch(batch -> batch.equals(CoreConst.BATCH_TYPE.BOARD_FREE_COMMENT_ADD_LAST_UPDATED_01))) {
            batchList.add(CoreConst.BATCH_TYPE.BOARD_FREE_COMMENT_ADD_LAST_UPDATED_01);
            item.setBatch(batchList);
        }

        return item;
    }
}
