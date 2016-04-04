package com.jakduk.restcontroller;

import com.jakduk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * Created by pyohwan on 16. 4. 5.
 */

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserRestController {

    @Resource
    LocaleResolver localeResolver;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/exist/update/username/{username}", method = RequestMethod.GET)
    public Boolean checkUpdateUsername(@PathVariable String username,
                                       HttpServletRequest request) {

        log.debug("username=" + username);

        Locale locale = localeResolver.resolveLocale(request);

        Boolean existUsername = userService.existUsernameOnUpdate(locale, username);

        return existUsername;
    }
}
