package jakduk;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 19.
 * @desc     :
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class ImageTest {
	
	@Value("${file.server.real.path}")
	private String fileServerRealPath;
	
	@Test
	public void convertDateTime01() {
		ObjectId objId = new ObjectId("54bd00393d969532bfb7ddc9");
		Instant instant = Instant.ofEpochMilli(objId.getDate().getTime());
		LocalDateTime timePoint = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		
		System.out.println("convertDateTime01=" + timePoint);
		System.out.println("convertDateTime01=" + timePoint.getDayOfYear());
		System.out.println("convertDateTime01=" + timePoint.getDayOfMonth());
		System.out.println("convertDateTime01=" + timePoint.getYear());
		System.out.println("convertDateTime01=" + timePoint.getMonthValue());
		
	}
	
	@Test
	public void createDirectory01() {
		
		Path newDirectoryPath = Paths.get(fileServerRealPath, "ddd/a/b/s");
		
		if (Files.notExists(newDirectoryPath, LinkOption.NOFOLLOW_LINKS)) {
		    try {
		        Files.createDirectories(newDirectoryPath);
		    } catch (IOException e) {
		        System.err.println(e);
		    }
		}
	}
	
	@Test
	public void inputOutputFile01() {
		
		Path fromPath = Paths.get(fileServerRealPath, "54bbb8cc3d96aab3f4760acd");
		Path toPath = Paths.get(fileServerRealPath, "phjang.jpg");
		
		if (Files.exists(fromPath)) {
			try {
				InputStream is = new FileInputStream(fromPath.toFile());
				Files.copy(is, toPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}

}
