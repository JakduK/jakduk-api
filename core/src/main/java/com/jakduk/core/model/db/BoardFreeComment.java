package com.jakduk.core.model.db;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.embedded.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.List;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 11. 16.
 * @desc     :
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Document
public class BoardFreeComment {

	@Id  @GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	private BoardItem boardItem;

	private BoardCommentStatus status;

	private CommonWriter writer;

	private String content;

	private List<CommonFeelingUser> usersLiking;

	private List<CommonFeelingUser> usersDisliking;

	private Boolean linkedGallery;

	private List<BoardHistory> history;

    private List<CoreConst.BATCH_TYPE> batch;

}
