package com.study.modoos.auth.jwt;

import com.study.modoos.auth.dto.TokenDto;
import com.study.modoos.auth.service.PrincipalDetails;
import com.study.modoos.auth.service.PrincipalDetailsService;
import com.study.modoos.auth.service.RedisService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
@Transactional(readOnly = false)
public class JwtProvider implements InitializingBean {
    private static final String AUTHORITIES_KEY = "role";
    private static final String ID_KEY = "id";
    private static final String url = "http://localhost:8080";
    private static Key signingKey;
    private final PrincipalDetailsService principalDetailsService;
    private final RedisService redisService;
    private final String secretKey;
    private final Long accessTokenValidityInMilliseconds;
    private final Long refreshTokenValidityInMilliseconds;

    public JwtProvider(
            PrincipalDetailsService principalDetailsService,
            RedisService redisService,
            @Value("${jwt.token.secret}") String secretKey,
            @Value("${jwt.token.access-token-validity-in-seconds}") Long accessTokenValidityInMilliseconds,
            @Value("${jwt.token.refresh-token-validity-in-seconds}") Long refreshTokenValidityInMilliseconds) {
        this.principalDetailsService = principalDetailsService;
        this.redisService = redisService;
        this.secretKey = secretKey;
        // seconds -> milliseconds
        this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds * 1000;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds * 1000;
    }

    public static Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) { // Access Token
            return e.getClaims();
        }
    }

    public static long getTokenExpirationTime(String token) {
        return getClaims(token).getExpiration().getTime();
    }

    // 시크릿 키 설정
    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] secretKeyBytes = Base64.decodeBase64(secretKey);
        signingKey = Keys.hmacShaKeyFor(secretKeyBytes);
    }

    @Transactional
    public TokenDto createToken(String id, String authorities) {
        Long now = System.currentTimeMillis();

        String accessToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS512")
                .setExpiration(new Date(now + accessTokenValidityInMilliseconds))
                .setSubject("access-token")
                .claim(url, true)
                .claim(ID_KEY, id)
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(SignatureAlgorithm.HS512, signingKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS512")
                .setExpiration(new Date(now + refreshTokenValidityInMilliseconds))
                .setSubject("refresh-token")
                .signWith(SignatureAlgorithm.HS512, signingKey)
                .compact();

        return new TokenDto(accessToken, refreshToken);
    }

    public Authentication getAuthentication(String token) {
        String id = getClaims(token).get(ID_KEY).toString();
        PrincipalDetails principalDetails = principalDetailsService.loadUserByUsername(id);
        return new UsernamePasswordAuthenticationToken(principalDetails, "", principalDetails.getAuthorities());
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            if (redisService.getValues(refreshToken).equals("delete")) { // 회원 탈퇴했을 경우
                return false;
            }
            Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(refreshToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.");
        } catch (NullPointerException e) {
            log.error("JWT Token is empty.");
        }
        return false;
    }

    // Filter에서 사용
    public boolean validateAccessToken(String accessToken) {
        try {
            if (redisService.getValues(accessToken) != null // NullPointException 방지
                    && redisService.getValues(accessToken).equals("logout")) { // 로그아웃 했을 경우
                return false;
            }
            Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(accessToken);
            return true;
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 재발급 검증 API에서 사용
    public boolean validateAccessTokenOnlyExpired(String accessToken) {
        try {
            return getClaims(accessToken)
                    .getExpiration()
                    .before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}