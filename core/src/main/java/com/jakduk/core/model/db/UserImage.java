package com.jakduk.core.model.db;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.embedded.CommonWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
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

    private CommonWriter writer;

    private CoreConst.GALLERY_STATUS_TYPE status;

    private String contentType;

    private CoreConst.USER_IMAGE_SOURCE_TYPE sourceType;

    private String externalUrl;

}
