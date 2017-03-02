package com.jakduk.batch.processor;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.common.util.CoreUtils;
import com.jakduk.core.model.db.BoardFree;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by pyohwanjang on 2017. 3. 2..
 */
public class AddShortContentProcessor implements ItemProcessor<BoardFree, BoardFree> {

    @Override
    public BoardFree process(BoardFree item) throws Exception {

        String stripHtmlContent = StringUtils.defaultIfBlank(CoreUtils.stripHtmlTag(item.getContent()), StringUtils.EMPTY);
        stripHtmlContent = StringUtils.truncate(stripHtmlContent, CoreConst.BOARD_SHORT_CONTENT_LENGTH);

        item.setShortContent(stripHtmlContent);

        List<CoreConst.BATCH_TYPE> batchList = Optional.ofNullable(item.getBatch())
                .orElseGet(ArrayList::new);

        if (batchList.stream().noneMatch(batch -> batch.equals(CoreConst.BATCH_TYPE.ADD_SHORT_CONTENT_01))) {
            batchList.add(CoreConst.BATCH_TYPE.ADD_SHORT_CONTENT_01);
            item.setBatch(batchList);
        }

        return item;
    }
}
