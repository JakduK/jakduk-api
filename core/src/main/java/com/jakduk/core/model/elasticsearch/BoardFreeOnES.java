package com.jakduk.core.model.elasticsearch;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.db.BoardFree;
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
* @date     : 2015. 8. 3.
* @desc     :
*/

@Getter
@Setter
@NoArgsConstructor
public class BoardFreeOnES {
	
	@JestId
    private String id;
	
	private CommonWriter writer;
	
	private String subject;
	
	private String content;
	
	private int seq;
	
	private String categoryName;

	public BoardFreeOnES(BoardFree boardFree) {

		String subjectEL = Optional.ofNullable(boardFree.getSubject()).orElse("");
		subjectEL = StringUtils.replacePattern(subjectEL, CoreConst.REGEX_FIND_HTML_TAG, "");
		subjectEL = StringUtils.replacePattern(subjectEL, CoreConst.REGEX_FIND_HTML_WHITESPACE, "");

		String contentEL = Optional.ofNullable(boardFree.getContent()).orElse("");
		contentEL = StringUtils.replacePattern(contentEL, CoreConst.REGEX_FIND_HTML_TAG, "");
		contentEL = StringUtils.replacePattern(contentEL, CoreConst.REGEX_FIND_HTML_WHITESPACE, "");

		this.id = boardFree.getId();
		this.writer = boardFree.getWriter();
		this.subject = subjectEL;
		this.content = contentEL;
		this.seq = boardFree.getSeq();
		this.categoryName = boardFree.getCategory().name();
	}
}
