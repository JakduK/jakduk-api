package com.jakduk.api.model.db;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.model.embedded.GalleryStatus;
import com.jakduk.api.model.embedded.LinkedItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 18.
 * @desc     :
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document
public class Gallery {

	@Id
	private String id;
	private GalleryStatus status;
	private CommonWriter writer;
	private String contentType;
	private String name;
	private String fileName;
	private Long size;
	private Long fileSize;
	private String hash;
	private List<LinkedItem> linkedItems;
	private List<String> batch;

}
