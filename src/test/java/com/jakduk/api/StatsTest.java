package com.jakduk.api;


import com.jakduk.api.dao.JakdukDAO;
import com.jakduk.api.model.aggregate.SupporterCount;
import com.jakduk.api.model.db.AttendanceClub;
import com.jakduk.api.repository.AttendanceClubRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 3. 13.
 * @desc     :
 */

@SpringBootTest
public class StatsTest {
	
	@Autowired
	JakdukDAO jakdukDAO;
	
	@Autowired
	AttendanceClubRepository attendanceClubRepository;
	
	@Test
	public void getSupporters01() {
		
		List<SupporterCount> supporters = jakdukDAO.getSupportFCCount("ko");
		
		Stream<SupporterCount> tests = supporters.stream();
		Integer sum = tests.mapToInt(SupporterCount::getCount).sum();
		
		System.out.println("getSupporters01=" + sum);
	}
	
	@Test
	public void getAttendance01() {
		
		Sort sort = Sort.by(Sort.Direction.ASC, "average");
		List<AttendanceClub> attendances = attendanceClubRepository.findBySeasonAndLeague(2013, "KLCL", sort);
		
		System.out.println("attendances=" + attendances);
	}

}
