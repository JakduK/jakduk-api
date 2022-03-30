package com.jakduk.api.model.db;

import com.jakduk.api.model.embedded.CommonFeelingUser;

import java.util.List;

public interface UsersFeeling {

    List<CommonFeelingUser> getUsersLiking();
    List<CommonFeelingUser> getUsersDisliking();
    void setUsersLiking(List<CommonFeelingUser> usersLiking);
    void setUsersDisliking(List<CommonFeelingUser> usersDisliking);

}
