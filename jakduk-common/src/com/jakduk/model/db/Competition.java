package com.jakduk.model.db;

import com.jakduk.model.embedded.Name;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.List;

/**
 * Created by pyohwan on 15. 12. 26.
 */

@Document
public class Competition {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private String id;

    private String code;

    private List<Name> names;

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

    public List<Name> getNames() {
        return names;
    }

    public void setNames(List<Name> names) {
        this.names = names;
    }

    @Override
    public String toString() {
        return "Competition{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", names=" + names +
                '}';
    }
}
