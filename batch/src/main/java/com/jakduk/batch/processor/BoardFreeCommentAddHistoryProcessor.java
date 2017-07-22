package com.jakduk.batch.processor;

import com.jakduk.batch.common.BatchConst;
import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.db.BoardFreeComment;
import com.jakduk.core.model.embedded.BoardHistory;
import org.bson.types.ObjectId;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BoardFreeCommentAddHistoryProcessor implements ItemProcessor<BoardFreeComment, BoardFreeComment> {

    @Override
    public BoardFreeComment process(BoardFreeComment item) throws Exception {

        List<BoardHistory> histories = item.getHistory();

        if (CollectionUtils.isEmpty(histories))
            histories = new ArrayList<>();

        ObjectId boardFreeCommentId = new ObjectId(item.getId());
        ObjectId historyId = new ObjectId(boardFreeCommentId.getDate());

        histories.add(new BoardHistory(historyId.toHexString(), BatchConst.BOARD_FREE_COMMENT_HISTORY_TYPE.CREATE.name(), item.getWriter()));
        item.setHistory(histories);

        List<CoreConst.BATCH_TYPE> batchList = Optional.ofNullable(item.getBatch())
                .orElseGet(ArrayList::new);

        if (batchList.stream().noneMatch(batch -> batch.equals(CoreConst.BATCH_TYPE.BOARD_FREE_COMMENT_ADD_HISTORY_01))) {
            batchList.add(CoreConst.BATCH_TYPE.BOARD_FREE_COMMENT_ADD_HISTORY_01);
            item.setBatch(batchList);
        }

        return item;
    }
}
