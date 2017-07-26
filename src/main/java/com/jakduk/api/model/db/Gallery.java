package com.jakduk.api.model.db;

import com.jakduk.api.common.JakdukConst;
import com.jakduk.api.model.embedded.CommonFeelingUser;
import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.model.embedded.GalleryStatus;
import com.jakduk.api.model.embedded.LinkedItem;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 18.
 * @desc     :
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Document
public class Gallery {
	
	@Id
	private String id;

	private String name;
	
	private String fileName;

	private List<LinkedItem> linkedItems;
	
	private CommonWriter writer;

	private long size;
	
	private long fileSize;
	
	private String contentType;

	private GalleryStatus status;

	private int views;

	private List<CommonFeelingUser> usersLiking;

	private List<CommonFeelingUser> usersDisliking;

	private List<JakdukConst.BATCH_TYPE> batch;

	private String hash;

}
