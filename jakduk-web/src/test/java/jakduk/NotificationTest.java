package jakduk;

import com.jakduk.configuration.AppConfig;
import com.jakduk.notification.SlackService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by pyohwan on 16. 5. 22.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@WebAppConfiguration
public class NotificationTest {

    @Value("${slack.board.webhook}")
    private String boardWebhook;

    @Value("${slack.board.channel}")
    private String boardChannel;

    @Autowired
    SlackService slackService;

    @Ignore
    @Test
    public void 슬랙알림() {
        slackService.send("test01", "hello");
    }
}
