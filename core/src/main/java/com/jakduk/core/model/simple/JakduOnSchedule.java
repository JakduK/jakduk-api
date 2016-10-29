package com.jakduk.core.model.simple;

import com.jakduk.core.model.embedded.CommonWriter;
import lombok.Data;

/**
 * @author pyohwan
 * 16. 5. 10 오후 10:57
 */

@Data
public class JakduOnSchedule {

    private String id;

    private CommonWriter writer;

    private int homeScore;

    private int awayScore;
}
