package com.jakduk.core.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.jakduk.core.model.simple.BoardFreeCommentOnHome;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 2. 27.
 * @desc     :
 */
public interface BoardFreeCommentOnHomeRepository extends MongoRepository<BoardFreeCommentOnHome, String>{

}
