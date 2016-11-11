package com.jakduk.api;

import com.jakduk.api.util.AbstractSpringTest;
import com.jakduk.core.dao.JakdukDAO;
import com.jakduk.core.model.db.AttendanceClub;
import com.jakduk.core.model.etc.SupporterCount;
import com.jakduk.core.repository.AttendanceClubRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 3. 13.
 * @desc     :
 */

public class StatsTest extends AbstractSpringTest {
	
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
		
		Sort sort = new Sort(Sort.Direction.ASC, Arrays.asList("average"));
		List<AttendanceClub> attendances = attendanceClubRepository.findBySeasonAndLeague(2013, "KLCL", sort);
		
		System.out.println("attendances=" + attendances);
	}

}
