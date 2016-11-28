package com.jakduk.core.model.elasticsearch;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.db.BoardFreeComment;
import com.jakduk.core.model.embedded.BoardItem;
import com.jakduk.core.model.embedded.CommonWriter;
import io.searchbox.annotations.JestId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
* @company  : http://jakduk.com
* @date     : 2015. 8. 23.
* @desc     :
*/

@Getter
@Setter
@NoArgsConstructor
public class CommentOnES {
	
	@JestId
    private String id;
	
	private BoardItem boardItem;
	
	private CommonWriter writer;
	
	private String content;

	public CommentOnES(BoardFreeComment boardFreeComment) {

		String contentES = Optional.ofNullable(boardFreeComment.getContent()).orElse("");
		contentES = StringUtils.replacePattern(contentES, CoreConst.REGEX_FIND_HTML_TAG, "");
		contentES = StringUtils.replacePattern(contentES, CoreConst.REGEX_FIND_HTML_WHITESPACE, "");

		this.id = boardFreeComment.getId();
		this.boardItem = boardFreeComment.getBoardItem();
		this.writer = boardFreeComment.getWriter();
		this.content = contentES;
	}
}
