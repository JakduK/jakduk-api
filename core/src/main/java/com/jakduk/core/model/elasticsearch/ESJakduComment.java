package com.jakduk.core.model.elasticsearch;

import com.jakduk.core.model.embedded.CommonWriter;
import lombok.*;

/**
 * @author pyohwan
 * 16. 3. 13 오후 10:56
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ESJakduComment {

    private String id;
    private String jakduScheduleId;
    private CommonWriter writer;
    private String contents;
}
