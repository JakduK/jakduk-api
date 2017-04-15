package com.jakduk.batch.processor;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.db.BoardFree;
import com.jakduk.core.model.embedded.BoardImage;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by pyohwanjang on 2017. 4. 15..
 */
public class BoardFreeAddLinkedGalleryProcessor implements ItemProcessor<BoardFree, BoardFree> {

    @Override
    public BoardFree process(BoardFree item) throws Exception {

        List<BoardImage> boardItems = item.getGalleries();

        if (! ObjectUtils.isEmpty(boardItems)) {
            item.setLinkedGallery(true);

            List<CoreConst.BATCH_TYPE> batchList = Optional.ofNullable(item.getBatch())
                    .orElseGet(ArrayList::new);

            if (batchList.stream().noneMatch(batch -> batch.equals(CoreConst.BATCH_TYPE.BOARD_FREE_ADD_LINKED_GALLERY_01))) {
                batchList.add(CoreConst.BATCH_TYPE.BOARD_FREE_ADD_LINKED_GALLERY_01);
                item.setBatch(batchList);
            }
        }

        return item;
    }
}
