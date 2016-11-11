package com.jakduk.core.model.db;

import com.jakduk.core.model.embedded.LocalName;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.List;

/**
 * @author pyohwan
 * 15. 12. 26 오후 11:06
 */

@Data
@Document
public class Competition {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private String id;

    private String code;

    private List<LocalName> names;
}
