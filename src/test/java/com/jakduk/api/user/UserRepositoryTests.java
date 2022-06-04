package com.jakduk.api.user;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.jakduk.api.common.Constants;
import com.jakduk.api.configuration.security.JakdukAuthority;
import com.jakduk.api.model.db.FootballClub;
import com.jakduk.api.model.db.FootballClubOrigin;
import com.jakduk.api.model.db.User;
import com.jakduk.api.model.db.UserPicture;
import com.jakduk.api.model.embedded.LocalName;
import com.jakduk.api.model.simple.UserProfile;
import com.jakduk.api.repository.user.UserProfileRepository;
import com.jakduk.api.repository.user.UserRepository;

@DataMongoTest
public class UserRepositoryTests {

	@Autowired
	private UserRepository repository;

	@Autowired
	private UserProfileRepository userProfileRepository;

	private User jakdukUser;
	private User facebookUser;
	private User naverUser;

	@BeforeEach
	public void setUp() {

		jakdukUser = User.builder()
			.id("571ccf50ccbfc325b20711c5")
			.email("test07@test.com")
			.username("test07")
			.password("33c7e555a3cbb6ffe3827d5be8e0e6ded856adea15066b822c15cc41e6c487f5b4d94c6c1b0f2dc1")
			.providerId(Constants.ACCOUNT_TYPE.JAKDUK)
			.about("Hi! test07~")
			.supportFC(FootballClub.builder()
				.id("54e1d2c68bf86df3fe819874")
				.origin(FootballClubOrigin.builder()
					.id("54e1d2a58bf86df3fe819871")
					.clubType(Constants.CLUB_TYPE.FOOTBALL_CLUB)
					.age(Constants.CLUB_AGE.SENIOR)
					.sex(Constants.CLUB_SEX.MEN)
					.build())
				.active("active")
				.names(new ArrayList<LocalName>() {{
					add(new LocalName("ko", "성남FC", "성남"));
					add(new LocalName("en", "SEONGNAM FC", "SEONGNAM"));
				}})
				.build())
			.userPicture(UserPicture.builder()
				.id("597a0d53807d710f57420aa5")
				.status(Constants.GALLERY_STATUS_TYPE.ENABLE)
				.contentType("image/jpeg")
				.build())
			.roles(Collections.singletonList(JakdukAuthority.ROLE_USER_01.getCode()))
			.build();

		facebookUser = User.builder()
			.email("test01@naver.com")
			.username("facebookUser")
			.providerId(Constants.ACCOUNT_TYPE.FACEBOOK)
			.providerUserId("1013999015380212")
			.about("꿀꿀")
			.supportFC(FootballClub.builder()
				.id("54e1d2c68bf86df3fe819874")
				.origin(FootballClubOrigin.builder()
					.id("54e1d2a58bf86df3fe819871")
					.clubType(Constants.CLUB_TYPE.FOOTBALL_CLUB)
					.age(Constants.CLUB_AGE.SENIOR)
					.sex(Constants.CLUB_SEX.MEN)
					.build())
				.active("active")
				.names(new ArrayList<LocalName>() {{
					add(new LocalName("ko", "성남FC", "성남"));
					add(new LocalName("en", "SEONGNAM FC", "SEONGNAM"));
				}})
				.build())
			.roles(Collections.singletonList(JakdukAuthority.ROLE_USER_01.getCode()))
			.lastLogged(LocalDateTime.now())
			.build();

		naverUser = User.builder()
			.email("test02@naver.com")
			.username("naverUser")
			.providerId(Constants.ACCOUNT_TYPE.NAVER)
			.providerUserId("1111111")
			.userPicture(UserPicture.builder()
				.id("5c6983e532e37f7fd8837b8f")
				.status(Constants.GALLERY_STATUS_TYPE.ENABLE)
				.contentType("image/jpeg")
				.build())
			.roles(Collections.singletonList(JakdukAuthority.ROLE_USER_02.getCode()))
			.lastLogged(LocalDateTime.now())
			.build();

		repository.save(jakdukUser);
		repository.save(naverUser);
		repository.save(facebookUser);
	}

	@Test
	public void findOneByEmail() {

		Optional<UserProfile> mustFind = userProfileRepository.findOneByEmail(naverUser.getEmail());
		assertTrue(mustFind.isPresent());

		Optional<UserProfile> mustNotFind = userProfileRepository.findByNEIdAndEmail(naverUser.getId(), naverUser.getEmail());
		assertFalse(mustNotFind.isPresent());
	}

	@Test
	public void findOneByUsername() {
		Optional<UserProfile> mustFind = userProfileRepository.findOneByUsername(jakdukUser.getUsername());
		assertTrue(mustFind.isPresent());

		Optional<UserProfile> mustNotFind = userProfileRepository.findByNEIdAndUsername(jakdukUser.getId(), jakdukUser.getUsername());
		assertFalse(mustNotFind.isPresent());
	}

	@AfterEach
	public void after() {
		repository.deleteById(jakdukUser.getId());
		assertFalse(repository.findById(jakdukUser.getId()).isPresent());
		repository.deleteById(facebookUser.getId());
		repository.deleteById(naverUser.getId());
		assertEquals(0, repository.findAll().size());
	}

}
