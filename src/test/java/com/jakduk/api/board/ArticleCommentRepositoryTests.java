package com.jakduk.api.board;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.util.CollectionUtils;

import com.jakduk.api.common.util.DateUtils;
import com.jakduk.api.model.aggregate.CommonCount;
import com.jakduk.api.model.db.ArticleComment;
import com.jakduk.api.repository.article.ArticleCommentRepository;

/**
 * Created by pyohwanjang on 2017. 3. 27..
 */
@DataMongoTest
public class ArticleCommentRepositoryTests {

	@Autowired
	private ArticleCommentRepository repository;

	private ArticleComment randomArticleComment;

	@BeforeEach
	public void setUp() {
		ArticleComment articleComment = repository.findTopByOrderByIdAsc().get();
		LocalDateTime localDateTime = DateUtils.dateToLocalDateTime(new ObjectId(articleComment.getId()).getDate());
		Long hours = ChronoUnit.MINUTES.between(localDateTime, LocalDateTime.now());
		LocalDateTime randomDate = localDateTime.plusMinutes(new Random().nextInt((int)(hours + 1)));
		randomArticleComment = repository.findTopByIdLessThanEqualOrderByIdDesc(
			new ObjectId(DateUtils.localDateTimeToDate(randomDate))).get();
	}

	@Test
	public void findCommentsCountByIds() {
		List<CommonCount> commentCounts = repository.findCommentsCountByIds(
			Arrays.asList(new ObjectId(randomArticleComment.getArticle().getId())));

		assertTrue(!CollectionUtils.isEmpty(commentCounts));
	}

	@Test
	public void findByBoardSeqAndGTId() {

		List<ArticleComment> comments = repository.findByBoardSeqAndGTId(randomArticleComment.getArticle().getBoard(),
			randomArticleComment.getArticle().getSeq(), null);

		assertTrue(!CollectionUtils.isEmpty(comments));

	}

}
