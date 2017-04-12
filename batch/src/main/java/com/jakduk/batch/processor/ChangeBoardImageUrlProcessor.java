package com.jakduk.batch.processor;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.db.BoardFree;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Jang, Pyohwan
 * @since 2016. 9. 5.
 */

public class ChangeBoardImageUrlProcessor implements ItemProcessor<BoardFree, BoardFree> {

	@Resource
	private Environment environment;

	@Override public BoardFree process(BoardFree item) throws Exception {

		final String PROFILE_STAGING_BEFORE_IMAGE_URL_01 = "src=\"/jakduk/gallery/";
		final String PROFILE_STAGING_BEFORE_IMAGE_URL_02 = "src=\"/jakduk-web/gallery/";
		final String PROFILE_STAGING_AFTER_IMAGE_URL = "src=\"https://staging.jakduk.com:8080/gallery/";
		final String PROFILE_PRODUCTION_BEFORE_IMAGE_URL_01 = "src=\"/gallery/";
		final String PROFILE_PRODUCTION_AFTER_IMAGE_URL = "src=\"https://api.jakduk.com/gallery/";

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

			List<CoreConst.BATCH_TYPE> batchList = Optional.ofNullable(item.getBatch())
					.orElseGet(ArrayList::new);

			if (batchList.stream().noneMatch(batch -> batch.equals(CoreConst.BATCH_TYPE.CHANGE_BOARD_CONTENT_IMAGE_URL_01))) {
				batchList.add(CoreConst.BATCH_TYPE.CHANGE_BOARD_CONTENT_IMAGE_URL_01);
				item.setBatch(batchList);
			}

		} else if (StringUtils.contains(item.getContent(), beforeImageUrl02)) {
			String newContent = StringUtils.replace(item.getContent(), beforeImageUrl02, afterImageUrl);
			item.setContent(newContent);

			List<CoreConst.BATCH_TYPE> batchList = Optional.ofNullable(item.getBatch())
					.orElseGet(ArrayList::new);

			if (batchList.stream().noneMatch(batch -> batch.equals(CoreConst.BATCH_TYPE.CHANGE_BOARD_CONTENT_IMAGE_URL_01))) {
				batchList.add(CoreConst.BATCH_TYPE.CHANGE_BOARD_CONTENT_IMAGE_URL_01);
				item.setBatch(batchList);
			}
		}

		System.out.println("BoardFree ID = " + item.getId());

		return item;
	}
}
