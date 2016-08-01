package com.jakduk.core.model.db;

import com.jakduk.core.model.embedded.BoardCommentStatus;
import com.jakduk.core.model.embedded.CommonFeelingUser;
import com.jakduk.core.model.embedded.CommonWriter;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.List;

/**
 * @author pyohwan
 * 16. 3. 13 오후 10:56
 */

@Document
@Data
public class JakduComment {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private String id;

    private String jakduScheduleId;

    private CommonWriter writer;

    @NotEmpty
    private String contents;

    private List<CommonFeelingUser> usersLiking;

    private List<CommonFeelingUser> usersDisliking;

    private BoardCommentStatus status;
}
