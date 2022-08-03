package kr.toyauction.global.token;

import kr.toyauction.global.dto.Token;
import kr.toyauction.global.exception.NoAuthorityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.servlet.http.HttpServletRequest;

@Component
public class JwtProvider implements InitializingBean {
    private final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access_token_expiration}")
    private long accessTokenExpiration;
    @Value("${jwt.refresh_token_expiration}")
    private long refreshTokenExpiration;

    private Key key;

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public Token createToken(Long memberId, Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // expiration time
        long now = (new Date()).getTime();
        long accessValidityinSecond = now + accessTokenExpiration;
        long refreshValidityinSecond = now + refreshTokenExpiration;
        Date accessValidity = new Date(accessValidityinSecond);
        Date refreshValidity = new Date(refreshValidityinSecond);

        // build token
        return new Token(
                Jwts.builder()
                        .setSubject(String.valueOf(memberId))
                        .claim(JwtEnum.AUTHORITY.getDescription(), authorities)
                        .signWith(key, SignatureAlgorithm.HS512)
                        .setExpiration(accessValidity)
                        .compact(),
                Jwts.builder()
                        .setSubject(String.valueOf(memberId))
                        .claim(JwtEnum.AUTHORITY.getDescription(), authorities)
                        .signWith(key, SignatureAlgorithm.HS512)
                        .setExpiration(refreshValidity)
                        .compact()
        );
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        if (claims.get(JwtEnum.AUTHORITY.getDescription()) == null) {
            throw new NoAuthorityException();
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(JwtEnum.AUTHORITY.getDescription()).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(HttpServletRequest request, String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            request.setAttribute(JwtEnum.EXCEPTION_PRODUCE.getDescription(), JwtEnum.ERROR_EXPIRED_TOKEN.getDescription());	// 만료된 토큰을 구별하기 위해서
            logger.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}