package me.jh.springstudy.auth;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    private final String SECRET_KEY;
    private static final Logger log = LoggerFactory.getLogger(JwtProvider.class);


    @Autowired
    public JwtProvider(@Value("${jwt.secret}") String secretKey) {
        this.SECRET_KEY = secretKey;
    }



    public Authentication getAuthentication(String accessToken) {
        // Jwt 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get("role") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims
                        .get("role")
                        .toString()
                        .replace("[{authority=", "")
                        .replace("}]", "")
                        .split(","))//TODO: 여기에 권한정보..?
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());


        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

	public Authentication getAuthenticationFromRefreshToken(String refreshToken) {
		Claims claims = parseClaims(refreshToken);
		String userId = claims.getSubject();
		UserDetails principal = new User(userId, "", List.of(new SimpleGrantedAuthority("ROLE_USER")));
		return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
	}



    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new JwtException("유효하지 않은 토큰입니다.", e);
        } catch (ExpiredJwtException e) {
            throw new JwtException("만료된 토큰입니다.", e);
        } catch (UnsupportedJwtException e) {
            throw new JwtException("지원되지 않는 토큰입니다.", e);
        } catch (IllegalArgumentException e) {
            throw new JwtException("토큰이 비어있습니다.", e);
        }
    }


    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser()
		            .verifyWith(getSigningKey())
		            .build()
		            .parseSignedClaims(accessToken)
                    .getPayload();
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

