package com.jakduk.batch.processor;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.db.Gallery;
import com.jakduk.core.model.embedded.BoardItem;
import com.jakduk.core.model.embedded.LinkedItem;
import org.springframework.batch.item.ItemProcessor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by pyohwanjang on 2017. 4. 11..
 */
public class ChangeGalleryPostsToLinkedItemsProcessor implements ItemProcessor<Gallery, Gallery> {

    @Override
    public Gallery process(Gallery item) throws Exception {

        List<BoardItem> posts = item.getPosts();

        if (Objects.nonNull(posts)) {

            List<LinkedItem> linkedItems = posts.stream()
                    .map(post -> LinkedItem.builder()
                            .id(post.getId())
                            .from(CoreConst.GALLERY_FROM_TYPE.BOARD_FREE)
                            .build())
                    .collect(Collectors.toList());

            item.setLinkedItems(linkedItems);

            List<CoreConst.BATCH_TYPE> batchList = Optional.ofNullable(item.getBatch())
                    .orElseGet(Collections::emptyList);

            if (batchList.stream().noneMatch(batch -> batch.equals(CoreConst.BATCH_TYPE.CHANGE_GALLERY_POSTS_TO_LINKED_ITEMS_01))) {
                batchList.add(CoreConst.BATCH_TYPE.CHANGE_GALLERY_POSTS_TO_LINKED_ITEMS_01);
                item.setBatch(batchList);
            }
        }

        return item;
    }
}
