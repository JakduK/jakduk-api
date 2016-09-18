package com.jakduk.batch.processor;

import com.jakduk.core.common.CommonConst;
import com.jakduk.core.model.db.BoardFree;
import com.jakduk.core.model.etc.CommonCount;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Jang, Pyohwan
 * @since 2016. 9. 5.
 */

@Slf4j
public class ChangeBoardImageUrlProcessor implements ItemProcessor<BoardFree, BoardFree> {

	private final String PROFILE_STAGING_BEFORE_IMAGE_URL_01 = "src=\"/jakduk/gallery/";
	private final String PROFILE_STAGING_BEFORE_IMAGE_URL_02 = "src=\"/jakduk-web/gallery/";
	private final String PROFILE_STAGING_AFTER_IMAGE_URL = "src=\"https://staging.jakduk.com:8080/gallery/";
	private final String PROFILE_PRODUCTION_BEFORE_IMAGE_URL_01 = "src=\"/gallery/";
	private final String PROFILE_PRODUCTION_AFTER_IMAGE_URL = "src=\"https://api.jakduk.com/gallery/";

	@Autowired
	private Environment environment;

	/**
	 * Process the provided item, returning a potentially modified or new item for continued
	 * processing.  If the returned result is null, it is assumed that processing of the item
	 * should not continue.
	 *
	 * @param item to be processed
	 * @return potentially modified or new item for continued processing, null if processing of the
	 * provided item should not continue.
	 * @throws Exception
	 */
	@Override public BoardFree process(BoardFree item) throws Exception {

		String beforeImageUrl01 = PROFILE_STAGING_BEFORE_IMAGE_URL_01;
		String beforeImageUrl02 = PROFILE_STAGING_BEFORE_IMAGE_URL_02;
		String afterImageUrl = PROFILE_STAGING_AFTER_IMAGE_URL;

		if (Stream.of(environment.getActiveProfiles()).anyMatch("production"::equals)) {
			beforeImageUrl01 = PROFILE_PRODUCTION_BEFORE_IMAGE_URL_01;
			afterImageUrl = PROFILE_PRODUCTION_AFTER_IMAGE_URL;
		}

		if (StringUtils.contains(item.getContent(), beforeImageUrl01)) {
			String newContent = StringUtils.replace(item.getContent(), beforeImageUrl01, afterImageUrl);
			item.setContent(newContent);

			List<CommonConst.BATCH_TYPE> batchList = Optional.ofNullable(item.getBatch()).orElseGet(ArrayList::new);

			if (batchList.stream().noneMatch(batch -> batch.equals(CommonConst.BATCH_TYPE.CHANGE_BOARD_CONTENT_IMAGE_URL_01))) {
				batchList.add(CommonConst.BATCH_TYPE.CHANGE_BOARD_CONTENT_IMAGE_URL_01);
				item.setBatch(batchList);
			}

		} else if (StringUtils.contains(item.getContent(), beforeImageUrl02)) {
			String newContent = StringUtils.replace(item.getContent(), beforeImageUrl02, afterImageUrl);
			item.setContent(newContent);

			List<CommonConst.BATCH_TYPE> batchList = Optional.ofNullable(item.getBatch()).orElseGet(ArrayList::new);

			if (batchList.stream().noneMatch(batch -> batch.equals(CommonConst.BATCH_TYPE.CHANGE_BOARD_CONTENT_IMAGE_URL_01))) {
				batchList.add(CommonConst.BATCH_TYPE.CHANGE_BOARD_CONTENT_IMAGE_URL_01);
				item.setBatch(batchList);
			}
		}

		log.debug("resultItemId=" + item.getId());

		return item;
	}
}
