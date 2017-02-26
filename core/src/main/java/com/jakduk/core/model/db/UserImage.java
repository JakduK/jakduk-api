package com.jakduk.core.model.db;

import com.jakduk.core.common.CoreConst;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 * Created by pyohwan on 17. 2. 16.
 */

@Document
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserImage {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private String id;

    @DBRef
    @Setter
    private User user;

    @Setter private CoreConst.GALLERY_STATUS_TYPE status;

    private String contentType;

    private CoreConst.ACCOUNT_TYPE sourceType;

}
