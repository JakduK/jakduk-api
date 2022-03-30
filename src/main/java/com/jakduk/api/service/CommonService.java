package com.jakduk.api.service;


import com.jakduk.api.model.db.Sequence;
import com.jakduk.api.repository.SequenceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 5. 24.
 * @desc     : 공통으로 쓰이는 서비스
 */

@Service
public class CommonService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired private MongoTemplate mongoTemplate;
	@Autowired private SequenceRepository sequenceRepository;

	/**
	 * 차기 SEQUENCE를 가져온다.
	 *
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

}
