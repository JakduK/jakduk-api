package com.jakduk.restcontroller;

import com.jakduk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(value = "/exist/email/update", method = RequestMethod.GET)
    public Boolean existUpdateEmail(@RequestParam(required = true) String email,
                                       HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);

        Boolean exist = userService.existEmailOnUpdate(locale, email.trim());

        return exist;
    }


    @RequestMapping(value = "/exist/username/update", method = RequestMethod.GET)
    public Boolean existUpdateUsername(@RequestParam(required = true) String username,
                                       HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);

        Boolean exist = userService.existUsernameOnUpdate(locale, username.trim());

        return exist;
    }

    @RequestMapping(value = "/exist/email", method = RequestMethod.GET)
    public Boolean existEmail(@RequestParam(required = true) String email,
                           HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);

        Boolean existEmail = userService.existEmail(locale, email.trim());

        return existEmail;
    }

    @RequestMapping(value = "/exist/username", method = RequestMethod.GET)
    public Boolean existUsername(@RequestParam(required = true) String username,
                              HttpServletRequest request) {

        Locale locale = localeResolver.resolveLocale(request);

        Boolean existUsername = userService.existUsernameOnWrite(locale, username.trim());

        return existUsername;
    }
}
