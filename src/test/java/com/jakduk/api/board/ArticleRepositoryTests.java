package com.jakduk.api.board;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Sort;

import com.jakduk.api.common.Constants;
import com.jakduk.api.common.util.DateUtils;
import com.jakduk.api.model.aggregate.BoardFeelingCount;
import com.jakduk.api.model.aggregate.BoardTop;
import com.jakduk.api.model.db.Article;
import com.jakduk.api.model.embedded.ArticleStatus;
import com.jakduk.api.model.embedded.BoardLog;
import com.jakduk.api.model.embedded.CommonFeelingUser;
import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.model.embedded.SimpleWriter;
import com.jakduk.api.model.embedded.UserPictureInfo;
import com.jakduk.api.model.simple.ArticleOnList;
import com.jakduk.api.model.simple.ArticleOnRSS;
import com.jakduk.api.model.simple.ArticleOnSitemap;
import com.jakduk.api.repository.article.ArticleRepository;

@DataMongoTest
public class ArticleRepositoryTests {

	@Autowired
	private ArticleRepository repository;

	private Article normalArticle;
	private Article deletedArticle;
	private Article noticeArticle;
	private Article linkedGalleryArticle;

	@BeforeEach
	public void before() {
		normalArticle = Article.builder()
			.seq(372)
			.board(Constants.BOARD_TYPE.FREE.name())
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
			.subject("제목입니다.")
			.content("내용입니다.")
			.views(65)
			.logs(new ArrayList<BoardLog>() {{
				add(BoardLog.builder()
					.id("5991c456acd2b30380b6b3e4")
					.type(Constants.ARTICLE_LOG_TYPE.CREATE.name())
					.writer(new SimpleWriter("571ccf50ccbfc325b20711c5", "test07"))
					.build());
			}})
			.shortContent("내용입니다")
			.lastUpdated(LocalDateTime.now())
			.linkedGallery(false)
			.build();

		deletedArticle = Article.builder()
			.seq(325)
			.board(Constants.BOARD_TYPE.FREE.name())
			.status(new ArticleStatus(false, true))
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
			.subject("지워진 글 제목")
			.content("지워진 글 내용")
			.views(14)
			.logs(new ArrayList<BoardLog>() {{
				add(BoardLog.builder()
					.id("5991c456acd2b30380b6b3e4")
					.type(Constants.ARTICLE_LOG_TYPE.CREATE.name())
					.writer(new SimpleWriter("571ccf50ccbfc325b20711c5", "test07"))
					.build());
			}})
			.shortContent("지워진 글 제목")
			.lastUpdated(LocalDateTime.now())
			.linkedGallery(false)
			.build();

		noticeArticle = Article.builder()
			.seq(362)
			.board(Constants.BOARD_TYPE.FREE.name())
			.status(new ArticleStatus(true, false))
			.writer(CommonWriter.builder()
				.userId("579b6c10807d715e1f833749")
				.username("test1")
				.providerId(Constants.ACCOUNT_TYPE.JAKDUK)
				.picture(UserPictureInfo.builder()
					.id("5c657ee132e37f6f39bfcdd7")
					.smallPictureUrl("https://dev-api.jakduk.dev/user/picture/small/5c657ee132e37f6f39bfcdd7")
					.largePictureUrl("https://dev-api.jakduk.dev/user/picture/5c657ee132e37f6f39bfcdd7")
					.build())
				.build())
			.subject("공지된 글 제목")
			.content("공지된 글 내용")
			.views(65)
			.usersLiking(new ArrayList<CommonFeelingUser>() {{
				add(new CommonFeelingUser("5c69775532e37f7f2ae721c5", "54abd1c63d96d076dd18d401", "admin"));
			}})
			.logs(new ArrayList<BoardLog>() {{
				add(BoardLog.builder()
					.id("5c657ef432e37f6f39bfcdd9")
					.type(Constants.ARTICLE_LOG_TYPE.CREATE.name())
					.writer(new SimpleWriter("579b6c10807d715e1f833749", "test1"))
					.build());
				add(BoardLog.builder()
					.id("5c6976f332e37f7f2ae721bc")
					.type(Constants.ARTICLE_LOG_TYPE.ENABLE_NOTICE.name())
					.writer(new SimpleWriter("54abd1c63d96d076dd18d401", "admin"))
					.build());
			}})
			.shortContent("지워진 글 제목")
			.lastUpdated(LocalDateTime.now())
			.linkedGallery(false)
			.build();

		linkedGalleryArticle = Article.builder()
			.seq(363)
			.board(Constants.BOARD_TYPE.FREE.name())
			.writer(CommonWriter.builder()
				.userId("579b6c10807d715e1f833749")
				.username("test1")
				.providerId(Constants.ACCOUNT_TYPE.JAKDUK)
				.picture(UserPictureInfo.builder()
					.id("5c657ee132e37f6f39bfcdd7")
					.smallPictureUrl("https://dev-api.jakduk.dev/user/picture/small/5c657ee132e37f6f39bfcdd7")
					.largePictureUrl("https://dev-api.jakduk.dev/user/picture/5c657ee132e37f6f39bfcdd7")
					.build())
				.build())
			.subject("갤러리 있는 글 제목")
			.content("갤러리 있는 글 내용.<p><img src=\\\"https://dev-api.jakduk.dev/gallery/5c6977bd32e37f7f2ae721c7\\\"></p>")
			.views(52)
			.logs(new ArrayList<BoardLog>() {{
				add(BoardLog.builder()
					.id("5c657ef432e37f6f39bfcdd9")
					.type(Constants.ARTICLE_LOG_TYPE.CREATE.name())
					.writer(new SimpleWriter("579b6c10807d715e1f833749", "test1"))
					.build());
			}})
			.shortContent("갤러리 있는 글 내용.")
			.lastUpdated(LocalDateTime.now())
			.linkedGallery(true)
			.build();

		repository.save(normalArticle);
		repository.save(deletedArticle);
		repository.save(noticeArticle);
		repository.save(linkedGalleryArticle);
	}

	@Test
	public void findOneById() {
		Article article = repository.findOneById(normalArticle.getId()).get();
		assertEquals(normalArticle.getSeq(), article.getSeq());
		assertEquals(normalArticle.getWriter(), article.getWriter());
		assertEquals(normalArticle.getSubject(), article.getSubject());
	}

	@Test
	public void findOneByBoardAndSeq() {
		Article article = repository.findOneByBoardAndSeq(normalArticle.getBoard(), normalArticle.getSeq()).get();
		assertEquals(normalArticle.getId(), article.getId());
		assertEquals(normalArticle.getWriter(), article.getWriter());
		assertEquals(normalArticle.getViews(), article.getViews());
	}

	@Test
	public void findPostsOnRss() {
		List<ArticleOnRSS> articles = repository.findPostsOnRss(null, Constants.SORT_BY_ID_DESC, 10);
		assertEquals(3, articles.size());
	}

	@Test
	public void findNotices() {
		List<ArticleOnList> articles = repository.findNotices(Sort.by(Sort.Direction.DESC, "_id"));
		assertEquals(1, articles.size());
	}

	@Test
	public void findPostsOnSitemap() {
		List<ArticleOnSitemap> articles = repository.findSitemapArticles(null, Sort.by(Sort.Direction.DESC, "_id"), 10);
		assertEquals(3, articles.size());
	}

	@Test
	public void findByIdInAndLinkedGalleryIsTrue() {
		List<Article> articles = repository.findByIdInAndLinkedGalleryIsTrue(Arrays.asList(normalArticle.getId(), normalArticle.getId(), linkedGalleryArticle.getId()));
		assertEquals(1, articles.size());
	}

	@Test
	public void findUsersFeelingCountTest() {
		List<ObjectId> ids = Arrays.asList(new ObjectId(normalArticle.getId()), new ObjectId(noticeArticle.getId()));
		List<BoardFeelingCount> feelingCounts = repository.findUsersFeelingCount(ids);

		assertEquals(ids.size(), feelingCounts.size());

		BoardFeelingCount maxUserLiking = feelingCounts.stream()
			.max(Comparator.comparing(x -> x.getUsersLikingCount()))
			.get();

		assertEquals(maxUserLiking.getId(), noticeArticle.getId());
		assertEquals(maxUserLiking.getUsersLikingCount(), noticeArticle.getUsersLiking().size());
	}

	@Test
	public void findTopLikes() {
		LocalDate localDate = LocalDate.now().minusWeeks(1);

		List<BoardTop> articles = repository.findTopLikes(Constants.BOARD_TYPE.FREE,
			new ObjectId(DateUtils.localDateToDate(localDate)));

		assertEquals(3, articles.size());

		assertEquals(normalArticle.getId(), articles.get(0).getId());
		assertEquals(noticeArticle.getId(), articles.get(1).getId());
	}

	@AfterEach
	public void after() {
		repository.deleteById(normalArticle.getId());
		assertFalse(repository.findById(normalArticle.getId()).isPresent());
		repository.deleteById(deletedArticle.getId());
		repository.deleteById(noticeArticle.getId());
		repository.deleteById(linkedGalleryArticle.getId());
		assertEquals(0, repository.findAll().size());
	}

}
