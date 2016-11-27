package com.jakduk.core.service;

import com.jakduk.core.model.db.Sequence;
import com.jakduk.core.model.db.Token;
import com.jakduk.core.repository.SequenceRepository;
import com.jakduk.core.repository.TokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 5. 24.
 * @desc     : 공통으로 쓰이는 서비스
 */

@Slf4j
@Service
public class CommonService {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private SequenceRepository sequenceRepository;

	@Autowired
	private TokenRepository tokenRepository;

	/**
	 * 차기 SEQUENCE를 가져온다.
	 * @param name 게시판 ID
	 * @return 다음 글번호
	 */
	public Integer getNextSequence(String name) {
		
		Integer nextSeq = 1;
		
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(name));
		
		Update update = new Update();
		update.inc("seq", 1);
		
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);
		
		Sequence sequence = mongoTemplate.findAndModify(query, update, options, Sequence.class);
		
		if (sequence == null) {
			Sequence newSequence = new Sequence();
			newSequence.setName(name);
			sequenceRepository.save(newSequence);
			log.debug("sequence is Null. Insert new Sequence.");
			
			return nextSeq;
		} else {
			nextSeq = sequence.getSeq();
			return nextSeq;
		}
	}

	// 토큰 가져오기.
	public Optional<Token> getTokenByCode(String code) {
		return tokenRepository.findOneByCode(code);
	}

}
