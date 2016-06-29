package com.jakduk.controller.user;

import com.jakduk.common.CommonConst;
import com.jakduk.common.CommonRole;
import com.jakduk.model.db.FootballClub;
import com.jakduk.model.db.User;
import com.jakduk.model.web.user.UserWrite;
import com.jakduk.service.CommonService;
import com.jakduk.service.FootballService;
import com.jakduk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 27.
 * @desc     :
 */

@Controller
@Slf4j
@RequestMapping("/user")
@SessionAttributes({"userWrite", "footballClubs"})
public class UserWriteController {
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private FootballService footballService;
	
	@Resource
	LocaleResolver localeResolver;

	@Autowired
	private StandardPasswordEncoder encoder;

	// jakduk 회원 가입 페이지.
	@RequestMapping(value = "/write", method = RequestMethod.GET)
	public String write(@RequestParam(required = false) String lang,
						HttpServletRequest request,
						Model model) {

		Locale locale = localeResolver.resolveLocale(request);
		String language = commonService.getLanguageCode(locale, lang);

		List<FootballClub> footballClubs = footballService.getFootballClubs(language, CommonConst.CLUB_TYPE.FOOTBALL_CLUB, CommonConst.NAME_TYPE.fullName);

		model.addAttribute("userWrite", new UserWrite());
		model.addAttribute("footballClubs", footballClubs);

		return "user/write";
	}

	// jakduk 회원 가입 처리.
	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public String write(@Valid UserWrite userWrite, BindingResult result, SessionStatus sessionStatus,
			HttpServletRequest request, HttpServletResponse response) {

		Locale locale = localeResolver.resolveLocale(request);

		// 첫 번째 검증.
		if (result.hasErrors()) {
			log.debug("result=" + result);
			return "user/write";
		}

		String pwd = userWrite.getPassword();
		String pwdCfm = userWrite.getPasswordConfirm();

		if (!pwd.equals(pwdCfm)) {
			result.rejectValue("passwordConfirm", "user.msg.password.mismatch");
		}

		userService.existEmail(locale, userWrite.getEmail());
		userService.existUsernameOnWrite(locale, userWrite.getUsername());

		// 위 검사를 통과 못한 경우, 메시지 출력
		if (result.hasErrors()) {
			log.debug("result=" + result);
			return "user/write";
		}

		User user = User.builder()
				.email(userWrite.getEmail().trim())
				.username(userWrite.getUsername().trim())
				.password(encoder.encode(userWrite.getPassword().trim()))
				.providerId(CommonConst.ACCOUNT_TYPE.JAKDUK)
				.build();

		String footballClub = userWrite.getFootballClub();
		String about = userWrite.getAbout();

		if (Objects.nonNull(footballClub) && footballClub.isEmpty() == false) {
			FootballClub supportFC = footballService.findById(footballClub);

			user.setSupportFC(supportFC);
		}

		if (Objects.nonNull(about) && about.isEmpty() == false) {
			user.setAbout(about.trim());
		}

		ArrayList<Integer> roles = new ArrayList<Integer>();
		roles.add(CommonRole.ROLE_NUMBER_USER_01);

		user.setRoles(roles);
		
		userService.save(user);

		log.debug("jakduk user created. user=" + user);

		userService.signInJakdukUser(user);

		sessionStatus.setComplete();
		
		String path = String.format("%s/", request.getContextPath());
		
		commonService.setCookie(response, CommonConst.COOKIE_EMAIL, userWrite.getEmail(), path);
		
		commonService.setCookie(response, CommonConst.COOKIE_REMEMBER, "1", path);
		
		return "redirect:/home";
	}

}
