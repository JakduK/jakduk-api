package com.jakduk.configuration.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * Created by pyohwan on 16. 3. 27.
 */
public class IPTokenBasedRememberMeServices extends TokenBasedRememberMeServices {

    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<HttpServletRequest>();

    public IPTokenBasedRememberMeServices(String key, UserDetailsService userDetailsService) {
        super(key, userDetailsService);
    }

    public void setContext(HttpServletRequest context) {
        requestHolder.set(context);
    }

    public HttpServletRequest getContext() {
        return requestHolder.get();
    }

    protected String getUserIPAddress(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

    @Override
    public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
        try
        {
            setContext(request);
            super.onLoginSuccess(request, response, successfulAuthentication);
        }
        finally
        {
            setContext(null);
        }
    }

    @Override
    protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request, HttpServletResponse response) {
        try
        {
            setContext(request);
            // 마지막 토큰을 가져온다.
            String ipAddressToken = cookieTokens[cookieTokens.length-1];
            if(!getUserIPAddress(request).equals(ipAddressToken))
            {
                throw new InvalidCookieException("Cookie IP Address did not contain a matching IP (contained '" + ipAddressToken + "')");
            }

            return super.processAutoLoginCookie(Arrays.copyOf(cookieTokens, cookieTokens.length-1), request, response);
        }
        finally
        {
            setContext(null);
        }
    }

    @Override
    protected String makeTokenSignature(long tokenExpiryTime, String username, String password) {
        return DigestUtils.md5DigestAsHex((username + ":" + tokenExpiryTime + ":" + password + ":" + getKey() + ":" + getUserIPAddress(getContext())).getBytes());
    }

    @Override
    protected void setCookie(String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response) {
        // 쿠키에 IP 주소를 추가한다.
        String[] tokensWithIPAddress = Arrays.copyOf(tokens, tokens.length+1);
        tokensWithIPAddress[tokensWithIPAddress.length-1] = getUserIPAddress(request);
        super.setCookie(tokensWithIPAddress, maxAge, request, response);
    }
}
