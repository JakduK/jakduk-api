package com.jakduk.model.elasticsearch;

import com.jakduk.model.embedded.BoardItem;
import com.jakduk.model.embedded.CommonWriter;
import io.searchbox.annotations.JestId;
import lombok.Data;

/**
 * Created by pyohwan on 16. 3. 13.
 */

@Data
public class JakduCommentOnES {

    @JestId
    private String id;
    private String jakduScheduleId;
    private CommonWriter writer;
    private String contents;
}
