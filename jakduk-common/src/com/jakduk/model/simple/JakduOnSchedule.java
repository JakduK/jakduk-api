package com.jakduk.model.simple;

import com.jakduk.model.db.JakduSchedule;
import com.jakduk.model.embedded.CommonWriter;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * Created by pyohwan on 16. 5. 10.
 */

@Data
public class JakduOnSchedule {

    private String id;

    private CommonWriter writer;

    private int homeScore;

    private int awayScore;
}
