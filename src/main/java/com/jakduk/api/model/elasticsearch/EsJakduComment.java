package com.jakduk.api.model.elasticsearch;

import com.jakduk.api.model.embedded.CommonWriter;
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
public class EsJakduComment {

    private String id;
    private String jakduScheduleId;
    private CommonWriter writer;
    private String contents;
}
