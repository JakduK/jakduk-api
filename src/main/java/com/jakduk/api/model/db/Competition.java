package com.jakduk.api.model.db;

import com.jakduk.api.model.embedded.LocalName;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


/**
 * @author pyohwan
 * 15. 12. 26 오후 11:06
 */

@Document
public class Competition {

    @Id
    private String id;
    private String code;
    private List<LocalName> names;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<LocalName> getNames() {
        return names;
    }

    public void setNames(List<LocalName> names) {
        this.names = names;
    }
}
