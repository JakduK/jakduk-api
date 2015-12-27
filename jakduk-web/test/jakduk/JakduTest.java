package jakduk;

import com.jakduk.dao.JakdukDAO;
import com.jakduk.model.db.Competition;
import com.jakduk.model.db.JakduSchedule;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pyohwan on 15. 12. 27.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class JakduTest {

    @Autowired
    JakdukDAO jakdukDAO;

    @Test
    public void getSchedulesTest01() {
        List<ObjectId> ids = new ArrayList<ObjectId>();
        ids.add(new ObjectId("567e9af6e4b0c12e43d76157"));

        List<Competition> competitions = jakdukDAO.getCompetitionList(ids, "ko");

        System.out.println("competitions=" + competitions);
    }
}
