package com.jakduk.core.model.db;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.embedded.BoardItem;
import com.jakduk.core.model.embedded.CommonFeelingUser;
import com.jakduk.core.model.embedded.CommonWriter;
import com.jakduk.core.model.embedded.GalleryStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.List;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 18.
 * @desc     :
 */

@Getter
@Setter
@Document
public class Gallery {
	
	@Id  @GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	private String name;
	
	private String fileName;
	
	private List<BoardItem> posts;
	
	private CommonWriter writer;
	
	private long size;
	
	private long fileSize;
	
	private String contentType;
	
	private GalleryStatus status;
	
	private int views = 0;
	
	private List<CommonFeelingUser> usersLiking;
	
	private List<CommonFeelingUser> usersDisliking;

	private List<CoreConst.BATCH_TYPE> batch;

}
