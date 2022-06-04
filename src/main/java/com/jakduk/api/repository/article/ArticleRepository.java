package com.jakduk.api.repository.article;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.jakduk.api.model.db.Article;
import com.jakduk.api.model.simple.ArticleSimple;

public interface ArticleRepository extends MongoRepository<Article, String>, ArticleRepositoryCustom {

	Optional<Article> findOneById(String id);

	Optional<Article> findOneBySeq(Integer seq);

	Optional<Article> findOneByBoardAndSeq(String board, Integer seq);

	List<Article> findByIdInAndLinkedGalleryIsTrue(List<String> ids);

	List<Article> findByIdInAndBoard(List<String> ids, String board);

	List<Article> findBySeqIn(List<Integer> seqs);

	@Query(value = "{'seq' : ?0}")
	ArticleSimple findBoardFreeOfMinimumBySeq(Integer seq);

	Optional<Article> findTopByIdLessThanEqualOrderByIdDesc(ObjectId id);

}
