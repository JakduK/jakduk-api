package com.jakduk.model.db;

import com.jakduk.model.embedded.LocalName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.List;

/**
 * Created by pyohwan on 15. 12. 26.
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
