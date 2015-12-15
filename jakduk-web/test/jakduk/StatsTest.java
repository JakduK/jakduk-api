package jakduk;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jakduk.dao.JakdukDAO;
import com.jakduk.model.db.AttendanceClub;
import com.jakduk.model.db.FootballClub;
import com.jakduk.model.embedded.FootballClubName;
import com.jakduk.model.etc.SupporterCount;
import com.jakduk.repository.AttendanceClubRepository;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 3. 13.
 * @desc     :
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
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
		
		Sort sort = new Sort(Sort.Direction.ASC, Arrays.asList("average"));
		List<AttendanceClub> attendances = attendanceClubRepository.findBySeasonAndLeague(2013, "KLCL", sort);
		
		System.out.println("attendances=" + attendances);
	}
	
	@Test
	public void getAttendance02() {
		List<FootballClub> footballClubs = jakdukDAO.getFootballClubList("en");
		Map<String, List<FootballClubName>> results = footballClubs.stream()
				.collect(Collectors.toMap(FootballClub::getId, FootballClub::getNames));
		
		System.out.println("footballClubs=" + footballClubs);
		System.out.println("results=" + results);
	}

}
