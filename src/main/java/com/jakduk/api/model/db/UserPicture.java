package com.jakduk.api.model.db;

import com.jakduk.api.common.Constants;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * Created by pyohwan on 17. 2. 16.
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class UserPicture {

    @Id
    private String id;
    @Setter private Constants.GALLERY_STATUS_TYPE status;
    private String contentType;

}
