package kr.toyauction.global.token;

import kr.toyauction.domain.member.entity.Member;
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
import java.text.SimpleDateFormat;
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
    @Value("${jwt.token_expiration}")
    private long tokenExpiration;

    private Key key;

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(Member member) {
        Claims claims = Jwts.claims();
        claims.put(JwtEnum.USER_ID.getDescription(), member.getId());
        claims.put(JwtEnum.AUTHORITY.getDescription(),member.getRole());
        claims.put(JwtEnum.USER_NAME.getDescription(), member.getUsername());

        // expiration time
        long now = (new Date()).getTime();
        long expiration = now + tokenExpiration;
        Date expirationDate = new Date(expiration);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String expirationDateString = simpleDateFormat.format(expirationDate);
        claims.put(JwtEnum.EXPIRATION.getDescription(), expirationDateString);

        // build token
        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(JwtEnum.ISSUER.getDescription())
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
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

        User principal = new User("USER", "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(HttpServletRequest request, String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
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