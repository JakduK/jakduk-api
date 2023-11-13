package com.jakduk.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakduk.api.common.Constants;
import com.jakduk.api.configuration.JakdukProperties;
import com.jakduk.api.model.db.Gallery;
import com.jakduk.api.repository.gallery.GalleryRepository;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;

import javax.annotation.Resource;

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
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 19.
 * @desc     :
 */

@SpringBootTest
public class GalleryOnListTest {

	@Resource
	private JakdukProperties.Storage storageProperties;

	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private GalleryRepository galleryRepository;

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

		Path newDirectoryPath = Paths.get(storageProperties.getImagePath(), "test");

		if (Files.notExists(newDirectoryPath, LinkOption.NOFOLLOW_LINKS)) {
			try {
				Files.createDirectories(newDirectoryPath);
			} catch (IOException e) {
				System.err.println(e);
			}
		}
	}

	// 파일이 없음.
	@Disabled
	@Test
	public void inputOutputFile01() {

		Path fromPath = Paths.get("/home/Pyohwan/사진", "t1.search.daumcdn.net.jpeg");
		Path toPath = Paths.get(storageProperties.getImagePath() + "/test", "phjang.jpg");

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
		Path filePath = Paths.get(storageProperties.getImagePath() + "/test", "phjang.jpg");

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
	public void JSON객체_문자열을_자바객체로변환() throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		String jsn = "{\"age\":\"32\",\"name\":\"steave\",\"job\":\"baker\"}";

		Map<String, String> map = mapper.readValue(jsn, new TypeReference<HashMap<String, String>>() {
		});

		assertEquals(map.get("age"), "32");
		assertEquals(map.get("name"), "steave");
		assertEquals(map.get("job"), "baker");
	}

	@Test
	public void JSON배열_문자열을_자바객체로변환() throws IOException {
		String tempJson =
			"[{\"uid\":\"54c4bf443d96ae38d537c5bf\",\"name\":\"t1.search.daumcdn.net.jpeg\",\"size\":7599,\"thumbUrl\":\"/jakduk/gallery/thumbnail/54c4bf443d96ae38d537c5bf\"}, "
				+
				"{\"uid\":\"54c4bf443d96ae38d537c5bf\",\"name\":\"t1.search.daumcdn.net.jpeg\",\"size\":7599,\"thumbUrl\":\"/jakduk/gallery/thumbnail/54c4bf443d96ae38d537c5bf\"}]";

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(tempJson);

		if (root.isArray()) {
			JsonNode node = root.get(0);

			assertEquals("54c4bf443d96ae38d537c5bf", node.path("uid").asText());
			assertEquals("t1.search.daumcdn.net.jpeg", node.path("name").asText());
			assertEquals(7599, node.path("size").asInt());

		} else {
			fail();
		}
	}

	@Test
	public void 자바배열객체를_JSON문자열로변환() throws JsonProcessingException {

		ObjectMapper mapper = new ObjectMapper();

		String expectResult = "[{\"uid\":\"a1\",\"name\":\"test01\",\"size\":100},{\"uid\":\"a2\",\"name\":\"test02\",\"size\":200}]";

		List<Map<String, Object>> images = new ArrayList<>();

		Map<String, Object> image01 = new LinkedHashMap<>();
		image01.put("uid", "a1");
		image01.put("name", "test01");
		image01.put("size", 100);
		images.add(image01);

		Map<String, Object> image02 = new LinkedHashMap<>();
		image02.put("uid", "a2");
		image02.put("name", "test02");
		image02.put("size", 200);
		images.add(image02);

		String result = mapper.writeValueAsString(images);

		assertEquals(expectResult, result);
	}

	@Test
	public void findById() {

		ArrayList<ObjectId> arrTemp = new ArrayList<ObjectId>();
		arrTemp.add(new ObjectId("54c4df893d96600d7f55a048"));
		arrTemp.add(new ObjectId("54c4e4833d96deb0f8592907"));

		AggregationOperation match = Aggregation.match(Criteria.where("_id").in(arrTemp));
		//AggregationOperation group = Aggregation.group("article").count().as("count");
		AggregationOperation sort = Aggregation.sort(Direction.ASC, "_id");
		//AggregationOperation limit = Aggregation.limit(Constants.BOARD_LINE_NUMBER);
		Aggregation aggregation = Aggregation.newAggregation(match, /*group, */ sort /*, limit*/);
		AggregationResults<Gallery> results = mongoTemplate.aggregate(aggregation, "gallery", Gallery.class);

		System.out.println("findOneById=" + results.getMappedResults());
	}

	@Test
	public void splitString() {
		String sample = "image/jpeg";
		String[] result = sample.split("/");
		System.out.println(result[0]);
		System.out.println(result[1]);
	}

	@Test
	public void streamAPITest01() {
		/*
		Gallery gallery = galleryRepository.findOne("54d623828bf8513a58f41b60");
		System.out.println("streamAPITest01=" + gallery);
		Stream<ArticleItem> tests = gallery.getFreePosts().stream();
		long count = tests.filter(item -> item.getId().equals("54d6238a8bf8513a58f4b62")).count();
		System.out.println("streamAPITest01=" + count);
		*/
	}

	@Test
	public void imageCapacity01() {

		long temp = 2548576;
		double bb = Constants.GALLERY_MAXIMUM_CAPACITY / (double)temp;
		long width = 100;
		long height = 90;

		System.out.format("imageCapacity01 = %f\n", 11.22);
		System.out.println("imageCapacity01 = " + (long)(width * bb) + ", " + height * bb);

	}

	@Test
	public void 그림목록() {
		List<Gallery> galleries = galleryRepository.findByIdIn(
			Arrays.asList("54c4d42a3d9675d9e50e1bed", "54c4d42e3d9675d9e50e1bee"));
		System.out.println("galleries=" + galleries);
	}

}

