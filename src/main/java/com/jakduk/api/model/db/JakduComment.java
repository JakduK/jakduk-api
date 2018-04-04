package com.jakduk.api.model.db;

import com.jakduk.api.model.embedded.ArticleCommentStatus;
import com.jakduk.api.model.embedded.CommonFeelingUser;
import com.jakduk.api.model.embedded.CommonWriter;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author pyohwan
 * 16. 3. 13 오후 10:56
 */

@Document
@Data
public class JakduComment {

    @Id
    private String id;

    private String jakduScheduleId;

    private CommonWriter writer;

    @NotEmpty
    private String contents;

    private List<CommonFeelingUser> usersLiking;

    private List<CommonFeelingUser> usersDisliking;

    private ArticleCommentStatus status;
}
