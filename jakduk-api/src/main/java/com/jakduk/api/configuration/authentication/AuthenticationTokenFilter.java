package com.jakduk.api.configuration.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakduk.api.common.util.JwtTokenUtil;
import com.jakduk.api.restcontroller.exceptionHandler.RestError;
import com.jakduk.core.authentication.common.SocialUserDetail;
import com.jakduk.core.common.CommonConst;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

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
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            String authToken = httpRequest.getHeader(tokenHeader);

            if (! StringUtils.isEmpty(authToken)) {
                String username = jwtTokenUtil.getUsernameFromToken(authToken);
                String providerId = jwtTokenUtil.getProviderIdFromToken(authToken);

                if (! StringUtils.isEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails;
                    String email;

                    if (CommonConst.ACCOUNT_TYPE.JAKDUK.toString().equals(providerId)) {
                        userDetails = jakdukDetailsService.loadUserByUsername(username);
                        email = userDetails.getUsername();
                    } else {
                        userDetails = socialDetailService.loadUserByUsername(username);
                        email = ((SocialUserDetail) userDetails).getEmail();
                    }

                    if (jwtTokenUtil.isValidateToken(authToken, email)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, userDetails.getPassword(), userDetails.getAuthorities());

                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }

            chain.doFilter(request, response);
        } catch (ServiceException e) {
            httpResponse.setContentType("application/json");
            httpResponse.setStatus(e.getServiceError().getHttpStatus().value());
            httpResponse.setCharacterEncoding("utf-8");

            RestError error = new RestError(e.getServiceError().getCode(), e.getMessage());

            String errorJson = new ObjectMapper().writeValueAsString(error);

            PrintWriter out = httpResponse.getWriter();
            out.print(errorJson);
            out.flush();
            out.close();
        }
    }

}
