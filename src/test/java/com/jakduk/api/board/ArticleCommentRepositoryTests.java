package com.jakduk.api.board;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.util.CollectionUtils;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.aggregate.CommonCount;
import com.jakduk.api.model.db.ArticleComment;
import com.jakduk.api.model.embedded.ArticleItem;
import com.jakduk.api.model.embedded.BoardLog;
import com.jakduk.api.model.embedded.CommonFeelingUser;
import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.model.embedded.SimpleWriter;
import com.jakduk.api.model.embedded.UserPictureInfo;
import com.jakduk.api.repository.article.ArticleCommentRepository;

/**
 * Created by pyohwanjang on 2017. 3. 27..
 */
@DataMongoTest
public class ArticleCommentRepositoryTests {

	@Autowired
	private ArticleCommentRepository repository;

	private ArticleComment sameArticleComment1;
	private ArticleComment sameArticleComment2;
	private ArticleComment articleComment3;
	private String articleId = "5c740b7f32e37f13ef3beead";

	@BeforeEach
	public void setUp() {
		sameArticleComment1 = ArticleComment.builder()
			.article(new ArticleItem(articleId, 364, Constants.BOARD_TYPE.FREE.name()))
			.writer(CommonWriter.builder()
				.userId("571ccf50ccbfc325b20711c5")
				.username("test07")
				.providerId(Constants.ACCOUNT_TYPE.JAKDUK)
				.picture(UserPictureInfo.builder()
					.id("597a0d53807d710f57420aa5")
					.smallPictureUrl("http://localhost:8080/user/picture/small/597a0d53807d710f57420aa5")
					.largePictureUrl("http://localhost:8080/user/picture/597a0d53807d710f57420aa5")
					.build())
				.build())
			.content("<p>맹꽁이 천둥 번개 결혼식</p>")
			.logs(new ArrayList<BoardLog>() {{
				add(BoardLog.builder()
					.id("5c740bd432e37f13ef3beeaf")
					.type(Constants.ARTICLE_LOG_TYPE.CREATE.name())
					.writer(new SimpleWriter("571ccf50ccbfc325b20711c5", "test07"))
					.build());
			}})
			.build();

		sameArticleComment2 = ArticleComment.builder()
			.article(new ArticleItem(articleId, 364, Constants.BOARD_TYPE.FREE.name()))
			.writer(CommonWriter.builder()
				.userId("566d68d5e4b0dfaaa5b98685")
				.username("test05")
				.providerId(Constants.ACCOUNT_TYPE.JAKDUK)
				.build())
			.content("<p><img src=\"https://staging.jakduk.com:8080/gallery/590749e5807d7112b1463ee3\"></p><p>?</p>")
			.linkedGallery(true)
			.logs(new ArrayList<BoardLog>() {{
				add(BoardLog.builder()
					.id("5c740bd432e37f13ef3beeaf")
					.type(Constants.ARTICLE_LOG_TYPE.CREATE.name())
					.writer(new SimpleWriter("571ccf50ccbfc325b20711c5", "test07"))
					.build());
			}})
			.build();

		articleComment3 = ArticleComment.builder()
			.article(new ArticleItem("59de27ce5f39915de30fce49", 302, Constants.BOARD_TYPE.FREE.name()))
			.writer(CommonWriter.builder()
				.userId("566d68d5e4b0dfaaa5b98685")
				.username("test05")
				.providerId(Constants.ACCOUNT_TYPE.JAKDUK)
				.build())
			.content("<p>Hello</p>")
			.usersLiking(new ArrayList<CommonFeelingUser>() {{
				add(new CommonFeelingUser("58c54bb4807d7150abbc1996", "57f4e864c8fa335cb4134330", "silverprize"));
			}})
			.logs(new ArrayList<BoardLog>() {{
				add(BoardLog.builder()
					.id("5c740bd432e37f13ef3beeaf")
					.type(Constants.ARTICLE_LOG_TYPE.CREATE.name())
					.writer(new SimpleWriter("571ccf50ccbfc325b20711c5", "test07"))
					.build());
			}})
			.build();

		repository.saveAll(Arrays.asList(sameArticleComment1, sameArticleComment2, articleComment3));
	}

	@Test
	public void findCommentsCountByIds() {
		List<CommonCount> commentCounts = repository.findCommentsCountByIds(Arrays.asList(new ObjectId(articleId)));
		assertEquals(2, commentCounts.stream().filter(commonCount -> commonCount.getId().getId().equals(articleId)).findFirst().get().getCount());
	}

	@Test
	public void findByBoardSeqAndGTId() {
		List<ArticleComment> comments = repository.findByBoardSeqAndGTId(sameArticleComment1.getArticle().getBoard(),
			sameArticleComment1.getArticle().getSeq(), null);

		assertEquals(2, comments.size());
		assertTrue(CollectionUtils.contains(comments.iterator(), sameArticleComment1));
		assertTrue(CollectionUtils.contains(comments.iterator(), sameArticleComment2));
	}

	@AfterEach
	public void after() {
		repository.deleteById(sameArticleComment1.getId());
		assertFalse(repository.findById(sameArticleComment1.getId()).isPresent());
		repository.deleteById(sameArticleComment2.getId());
		repository.deleteById(articleComment3.getId());
		assertEquals(0, repository.findAll().size());
	}

}
