package com.jakduk.api.model.db;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.jakduk.api.model.embedded.ArticleItem;
import com.jakduk.api.model.embedded.BoardLog;
import com.jakduk.api.model.embedded.CommonFeelingUser;
import com.jakduk.api.model.embedded.CommonWriter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 11. 16.
 * @desc     :
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document
public class ArticleComment implements UsersFeeling {

	@Id
	private String id;
	private ArticleItem article;
	private CommonWriter writer;
	private String content;
	private List<CommonFeelingUser> usersLiking;
	private List<CommonFeelingUser> usersDisliking;
	private Boolean linkedGallery;
	private List<BoardLog> logs;
	private List<String> batch;

	@Override
	public List<CommonFeelingUser> getUsersLiking() {
		return usersLiking;
	}

	@Override
	public void setUsersLiking(List<CommonFeelingUser> usersLiking) {
		this.usersLiking = usersLiking;
	}

	@Override
	public List<CommonFeelingUser> getUsersDisliking() {
		return usersDisliking;
	}

	@Override
	public void setUsersDisliking(List<CommonFeelingUser> usersDisliking) {
		this.usersDisliking = usersDisliking;
	}

}
