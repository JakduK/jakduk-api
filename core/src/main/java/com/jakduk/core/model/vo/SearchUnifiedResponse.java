package com.jakduk.core.model.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchUnifiedResponse {

	private SearchBoardResult postResult;

	private SearchCommentResult commentResult;

	private SearchGalleryResult galleryResult;
}
