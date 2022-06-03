package com.jakduk.api.home;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.jakduk.api.model.db.HomeDescription;
import com.jakduk.api.repository.HomeDescriptionRepository;

@DataMongoTest
public class HomeDescriptionRepositoryTests {

	@Autowired
	private HomeDescriptionRepository repository;

	private HomeDescription kleagueHomeDescription;
	private HomeDescription noticeHomeDescription;

	@BeforeEach
	public void before() {
		kleagueHomeDescription = HomeDescription.builder()
			.desc("<h4>2015 K리그 클래식 17R</h4>\\r\\n<ul class=\\\"list-unstyled\\\">\\r\\n<li>2015-06-20(토)</li>\\r\\n"
				+ "<li>\\r\\n<ul>\\r\\n<li>16:00 성남-광주 @탄천종합</li>\\r\\n<li>19:00 전남-서울 @광양전용</li>\\r\\n<li>19:00 부산-포항 @부산아시아드</li>\\r\\n</ul>\\r\\n</li>\\r\\n"
				+ "<li>2015-06-21(일)</li>\\r\\n<li>\\r\\n<ul>\\r\\n<li>18:00 수원-전북 @수원월드컵</li>\\r\\n<li>18:00 울산-인천 @울산문수</li>\\r\\n"
				+ "<li>19:00 대전-제주 @대전월드컵</li>\\r\\n</ul>\\r\\n</li>\\r\\n</ul>\\r\\n\\r\\n<h4>2015 K리그 챌린지 17R</h4>\\r\\n<ul class=\\\"list-unstyled\\\">\\r\\n<li>2015-06-20(토)</li>\\r\\n<li>\\r\\n<ul>\\r\\n<li>16:00 대구-부천 @대구스타디움</li>\\r\\n<li>19:00 서울E-상주 @잠실올림픽</li>\\r\\n<li>19:00 강원-수원FC @속초종합</li>\\r\\n</ul>\\r\\n</li>\\r\\n<li>2015-06-21(일)</li>\\r\\n<li>\\r\\n<ul>\\r\\n<li>19:00 안산-안양 @안산와스타디움</li>\\r\\n<li>19:00 고양-충주 @고양종합</li>\\r\\n</ul>\\r\\n</li>\\r\\n</ul>")
			.priority(19)
			.build();

		noticeHomeDescription = HomeDescription.builder()
			.desc("<h4>알림판</h4>\\n<ul>\\n<li><a href=\\\"https://jakduk.com/board/free/1018\\\">Daum 계정으로 로그인 중단 안내</a></li>\\n<li><a href=\\\"https://jakduk.com/stats/attendance/league\\\">2018년 관중수가 통계 페이지에 업데이트 되었습니다</a></li>\\n</ul>")
			.priority(20)
			.build();

		repository.save(kleagueHomeDescription);
		repository.save(noticeHomeDescription);
	}
	@Test
	public void findOneByOrderByPriorityDesc() {
		HomeDescription homeDescription = repository.findFirstByOrderByPriorityDesc().get();
		assertEquals(noticeHomeDescription, homeDescription);
	}

	@AfterEach
	public void after() {
		repository.deleteById(kleagueHomeDescription.getId());
		assertFalse(repository.findById(kleagueHomeDescription.getId()).isPresent());
		repository.deleteById(noticeHomeDescription.getId());
		assertEquals(0, repository.findAll().size());
	}

}
