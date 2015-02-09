package jakduk;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bson.types.ObjectId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.jakduk.common.CommonConst;
import com.jakduk.dao.BoardFreeCount;
import com.jakduk.dao.JakdukDAO;
import com.jakduk.model.db.Gallery;
import com.jakduk.model.embedded.BoardItem;
import com.jakduk.repository.GalleryRepository;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 19.
 * @desc     :
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class GalleryTest {
	
	@Value("${storage.image.path}")
	private String storageImagePath;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private GalleryRepository galleryRepository;
	
	@Autowired
	JakdukDAO jakdukDAO;
	
	@Test
	public void convertDateTime01() {
		ObjectId objId = new ObjectId("54bd00393d969532bfb7ddc9");
		Instant instant = Instant.ofEpochMilli(objId.getDate().getTime());
		LocalDateTime timePoint = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		
		System.out.println("convertDateTime01=" + timePoint);
//		System.out.println("convertDateTime01=" + timePoint.getDayOfYear());
//		System.out.println("convertDateTime01=" + timePoint.getDayOfMonth());
//		System.out.println("convertDateTime01=" + timePoint.getYear());
//		System.out.println("convertDateTime01=" + timePoint.getMonthValue());
		
	}
	
	@Test
	public void createDirectory01() {
		
		Path newDirectoryPath = Paths.get(storageImagePath, "test");
		
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
		
		Path fromPath = Paths.get("/home/Pyohwan/사진", "t1.search.daumcdn.net.jpeg");
		Path toPath = Paths.get(storageImagePath + "/test", "phjang.jpg");
		
		if (Files.exists(fromPath)) {
			try {
				InputStream is = new FileInputStream(fromPath.toFile());
				//Files.copy(is, toPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void deleteFile01() {
		Path filePath = Paths.get(storageImagePath + "/test", "phjang.jpg");
		
		if (Files.exists(filePath)) {
			try {
				Files.delete(filePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void json01() throws JsonParseException, JsonMappingException, IOException {		
		String tempJson = "[{\"uid\":\"54c4bf443d96ae38d537c5bf\",\"name\":\"t1.search.daumcdn.net.jpeg\",\"size\":7599,\"thumbUrl\":\"/jakduk/gallery/thumbnail/54c4bf443d96ae38d537c5bf\"}, {\"uid\":\"54c4bf443d96ae38d537c5bf\",\"name\":\"t1.search.daumcdn.net.jpeg\",\"size\":7599,\"thumbUrl\":\"/jakduk/gallery/thumbnail/54c4bf443d96ae38d537c5bf\"}]";
		
		JSONParser jsonParser = new JSONParser();
		try {
			JSONArray jsonArray = (JSONArray) jsonParser.parse(tempJson);
			
			for (int i = 0 ; i < jsonArray.size() ; i++) {
				JSONObject obj = (JSONObject)jsonArray.get(i);
				System.out.println("obj=" + obj.get("thumbUrl"));
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("error");
			e.printStackTrace();
		}
	}
	
	@Test
	public void arrayToJson() {
		
		List<String> images = new ArrayList<String>();

		Map<String, String> image01 = new HashMap<String, String>();
		image01.put("uid", "123123");
		image01.put("name", "test01");
		image01.put("size", String.valueOf(100));
		images.add(JSONObject.toJSONString(image01));
		Map<String, String> image02 = new HashMap<String, String>();
		image02.put("uid", "123123");
		image02.put("name", "test01");
		image02.put("size", String.valueOf(100));
		images.add(JSONObject.toJSONString(image02));
		
		System.out.println(images);
	}
	
	@Test
	public void findById() {
		
		ArrayList<ObjectId> arrTemp = new ArrayList<ObjectId>();
		arrTemp.add(new ObjectId("54c4df893d96600d7f55a048"));
		arrTemp.add(new ObjectId("54c4e4833d96deb0f8592907"));
		
		AggregationOperation match = Aggregation.match(Criteria.where("_id").in(arrTemp));
		//AggregationOperation group = Aggregation.group("boardItem").count().as("count");
		AggregationOperation sort = Aggregation.sort(Direction.ASC, "_id");
		//AggregationOperation limit = Aggregation.limit(CommonConst.BOARD_LINE_NUMBER);
		Aggregation aggregation = Aggregation.newAggregation(match, /*group, */ sort /*, limit*/);
		AggregationResults<Gallery> results = mongoTemplate.aggregate(aggregation, "gallery", Gallery.class);
		
		System.out.println("findById=" + results.getMappedResults());
	}
	
	@Test
	public void splitString() {
		String sample = "image/jpeg";
		String[] result = sample.split("/");
		System.out.println(result[0]);
		System.out.println(result[1]);
	}
	
	@Test
	public void getGalleryList() {
		Sort sort = new Sort(Sort.Direction.DESC, Arrays.asList("_id"));
		Pageable pageable = new PageRequest(0, CommonConst.BOARD_SIZE_LINE_NUMBER, sort);
		
		System.out.println("getGalleryList=" + galleryRepository.findAll(pageable).getContent());
		
		ArrayList<ObjectId> arrTemp = new ArrayList<ObjectId>();
		arrTemp.add(new ObjectId("54c4d4313d9675d9e50e1bf0"));
		arrTemp.add(new ObjectId("54c4df933d96600d7f55a04b"));
		
		System.out.println("getGalleryList=" + jakdukDAO.getBoardFreeOnGallery(arrTemp)
				);
	}
	
	@Test
	public void streamAPITest01() {
		/*
		Gallery gallery = galleryRepository.findOne("54d623828bf8513a58f41b60");
		System.out.println("streamAPITest01=" + gallery);
		Stream<BoardItem> tests = gallery.getPosts().stream();
		long count = tests.filter(item -> item.getId().equals("54d6238a8bf8513a58f4b62")).count();
		System.out.println("streamAPITest01=" + count);
		*/
	}
	
	@Test
	public void findOneById01() {
		
		System.out.println("findOneById01 gallery01=" + jakdukDAO.getGalleryByIdGreaterThan(new ObjectId("54d8a5f58bf84ef38e6b25f6")));
	}

}

