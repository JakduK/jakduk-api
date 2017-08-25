package com.jakduk.api.repository.article;

import com.jakduk.api.model.db.Article;
import com.jakduk.api.model.simple.ArticleSimple;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends MongoRepository<Article, String>, ArticleRepositoryCustom {

	Optional<Article> findOneById(String id);
	Optional<Article> findOneBySeq(Integer seq);
	Optional<Article> findOneByBoardAndSeq(String board, Integer seq);

	List<Article> findByIdInAndLinkedGalleryIsTrue(List<String> ids);
	List<Article> findByIdInAndBoard(List<String> ids, String board);

	@Query(value="{'seq' : ?0}")
    ArticleSimple findBoardFreeOfMinimumBySeq(Integer seq);

	// for JUnit
	Optional<Article> findTopByOrderByIdAsc();
	Optional<Article> findTopByIdLessThanEqualOrderByIdDesc(ObjectId id);

}
