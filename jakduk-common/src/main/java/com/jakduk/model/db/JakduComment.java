package com.jakduk.model.db;

import com.jakduk.model.embedded.BoardCommentStatus;
import com.jakduk.model.embedded.BoardItem;
import com.jakduk.model.embedded.CommonFeelingUser;
import com.jakduk.model.embedded.CommonWriter;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.List;

/**
 * Created by pyohwan on 16. 3. 13.
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
