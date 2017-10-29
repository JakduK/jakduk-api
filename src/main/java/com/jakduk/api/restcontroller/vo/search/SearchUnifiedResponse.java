package com.jakduk.api.restcontroller.vo.search;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SearchUnifiedResponse {
	private SearchArticleResult articleResult; // 매칭된 글 객체
	private SearchCommentResult commentResult; // 매칭된 댓글 객체
	private SearchGalleryResult galleryResult; // 매칭된 사진 객체
}
