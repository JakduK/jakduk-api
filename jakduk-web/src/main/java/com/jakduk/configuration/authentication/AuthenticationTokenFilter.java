package com.jakduk.configuration.authentication;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.GenericFilterBean;

import com.jakduk.common.CommonConst;
import com.jakduk.common.util.JwtTokenUtil;
import com.jakduk.exception.NotFoundJakdukAccountException;

@Slf4j
public class AuthenticationTokenFilter extends GenericFilterBean {

  @Value("${jwt.token.header}")
  private String tokenHeader;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  private JakdukDetailsService jakdukDetailsService;

  @Autowired
  private SocialDetailService socialDetailService;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    String authToken = httpRequest.getHeader(tokenHeader);
    String username = jwtTokenUtil.getUsernameFromToken(authToken);
    String providerId = jwtTokenUtil.getProviderIdFromToken(authToken);

    if (! ObjectUtils.isEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
    	try {
		    UserDetails userDetails;
		    if (CommonConst.ACCOUNT_TYPE.JAKDUK.toString().equals(providerId)) {
			    userDetails = jakdukDetailsService.loadUserByUsername(username);
		    } else {
			    userDetails = socialDetailService.loadUserByUsername(username);
		    }

		    if (jwtTokenUtil.isValidateToken(authToken, userDetails)) {
			    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				    userDetails, userDetails.getPassword(), userDetails.getAuthorities());

			    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
			    SecurityContextHolder.getContext().setAuthentication(authentication);
		    }
	    } catch (NotFoundJakdukAccountException e) {
				log.info(e.getMessage(), e);
	    }
    }

    chain.doFilter(request, response);
  }

}
