package com.jakduk.api.model.db;

import com.jakduk.api.model.embedded.CommonFeelingUser;

import java.util.List;

public interface UsersFeeling {

	List<CommonFeelingUser> getUsersLiking();

	void setUsersLiking(List<CommonFeelingUser> usersLiking);

	List<CommonFeelingUser> getUsersDisliking();

	void setUsersDisliking(List<CommonFeelingUser> usersDisliking);

}
