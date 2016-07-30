package com.jakduk.configuration.authentication;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.aop.AopInvocationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.jakduk.authentication.common.CommonPrincipal;
import com.jakduk.authentication.common.JakdukPrincipal;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;

    private static final String CLAIM_KEY_USER_ID = "uid";
    private static final String CLAIM_KEY_NAME = "name";
    private static final String CLAIM_KEY_PROVIDER_ID = "pid";

    private static final String AUDIENCE_UNKNOWN = "unknown";
    private static final String AUDIENCE_WEB = "web";
    private static final String AUDIENCE_MOBILE = "mobile";
    private static final String AUDIENCE_TABLET = "tablet";

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expiration}")
    private Long expiration;

    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = claims.getIssuer();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    public String getAudienceFromToken(String token) {
        String audience;
        try {
            final Claims claims = getClaimsFromToken(token);
            audience = (String) claims.get(Claims.AUDIENCE);
        } catch (Exception e) {
            audience = null;
        }
        return audience;
    }

    public Map<String, Object> getAttemptedFromToken(String token) {
        Map<String, Object> attempted = new HashMap<>();
        try {
            final Claims claims = getClaimsFromToken(token);

            if (claims.containsKey("email"))
                attempted.put("email", claims.get("email", String.class));

            if (claims.containsKey("username"))
                attempted.put("username", claims.get("username", String.class));

            if (claims.containsKey("id"))
                attempted.put("id", claims.get("id", String.class));

            if (claims.containsKey("about"))
                attempted.put("about", claims.get("about", String.class));

            if (claims.containsKey("footballClub"))
                attempted.put("footballClub", claims.get("footballClub", String.class));

        } catch (Exception ignored) {

        }

        return attempted;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private String generateAudience(Device device) {
        String audience = this.AUDIENCE_UNKNOWN;

        try {
            if (device.isNormal()) {
                audience = this.AUDIENCE_WEB;
            } else if (device.isTablet()) {
                audience = AUDIENCE_TABLET;
            } else if (device.isMobile()) {
                audience = AUDIENCE_MOBILE;
            }
        } catch (AopInvocationException e) {
            return audience;
        }

        return audience;
    }

    private Boolean ignoreTokenExpiration(String token) {
        String audience = getAudienceFromToken(token);
        return (AUDIENCE_TABLET.equals(audience) || AUDIENCE_MOBILE.equals(audience));
    }

    public String generateToken(CommonPrincipal userDetails, Device device) {

        Map<String, Object> claims = new HashMap<>();
        claims.put(Claims.ISSUER, userDetails.getEmail());
        claims.put(Claims.AUDIENCE, generateAudience(device));
        claims.put(CLAIM_KEY_USER_ID, userDetails.getId());
        claims.put(CLAIM_KEY_NAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_PROVIDER_ID, userDetails.getProviderId());

        return generateToken(claims, generateExpirationDate());
    }

    public String generateAttemptedToken(Map<String, Object> claims) {

        return generateToken(claims, new Date(System.currentTimeMillis() + 600 * 1000));
    }

    private String generateToken(Map<String, Object> claims, Date expirationDate) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Boolean canTokenBeRefreshed(String token) {
        return (! isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            refreshedToken = generateToken(claims, generateExpirationDate());
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public Boolean isValidateTokenJakdukUser(String token, UserDetails userDetails) {
        JakdukPrincipal user = (JakdukPrincipal) userDetails;
        final String username = this.getUsernameFromToken(token);

        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }
}
