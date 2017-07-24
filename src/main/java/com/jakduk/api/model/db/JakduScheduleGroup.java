package com.jakduk.api.model.db;

import com.jakduk.api.common.CoreConst;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author pyohwan
 * 16. 1. 10 오후 11:07
 */

@NoArgsConstructor
@Getter
@Setter
@Document
public class JakduScheduleGroup {

    @Id
    private String id;

    private int seq;

    private CoreConst.JAKDU_GROUP_STATE state;

    private Date openDate;

}
