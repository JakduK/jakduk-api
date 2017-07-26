package com.jakduk.api.model.db;

import com.jakduk.api.model.embedded.LocalName;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


/**
 * @author pyohwan
 * 15. 12. 26 오후 11:06
 */

@Data
@Document
public class Competition {

    @Id
    private String id;

    private String code;

    private List<LocalName> names;
}
