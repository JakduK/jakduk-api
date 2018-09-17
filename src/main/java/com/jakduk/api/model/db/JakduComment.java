package com.jakduk.api.model.db;

import com.jakduk.api.model.embedded.CommonFeelingUser;
import com.jakduk.api.model.embedded.CommonWriter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author pyohwan
 * 16. 3. 13 오후 10:56
 */

@Document
public class JakduComment {

    @Id
    private String id;
    private String jakduScheduleId;
    private CommonWriter writer;
    @NotEmpty
    private String contents;
    private List<CommonFeelingUser> usersLiking;
    private List<CommonFeelingUser> usersDisliking;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJakduScheduleId() {
        return jakduScheduleId;
    }

    public void setJakduScheduleId(String jakduScheduleId) {
        this.jakduScheduleId = jakduScheduleId;
    }

    public CommonWriter getWriter() {
        return writer;
    }

    public void setWriter(CommonWriter writer) {
        this.writer = writer;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public List<CommonFeelingUser> getUsersLiking() {
        return usersLiking;
    }

    public void setUsersLiking(List<CommonFeelingUser> usersLiking) {
        this.usersLiking = usersLiking;
    }

    public List<CommonFeelingUser> getUsersDisliking() {
        return usersDisliking;
    }

    public void setUsersDisliking(List<CommonFeelingUser> usersDisliking) {
        this.usersDisliking = usersDisliking;
    }
}
