package me.jh.springstudy.config;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Deprecated
public class JwtProvider {
//    @Value("${jwt.secret}")
//    private String secretKey;
//
//    @Value("${jwt.expiration}")
//    private long expirationTime;
//
//    public String createToken(LoginDto loginDto, List<String> roles) {
//        Claims claims = Jwts.claims().setSubject(loginDto.getLoginId()); // JWT payload 에 저장되는 정보단위, 보통 여기서 user를 식별하는 값을 넣는다.
//        claims.put("roles", roles); // 정보는 key / value 쌍으로 저장된다.
//        Date now = new Date();
//        return Jwts.builder()
//                .setClaims(claims) // 정보 저장
//                .setIssuedAt(now) // 토큰 발행 시간 정보
//                .setExpiration(new Date(now.getTime() + expirationTime)) // set Expire Time
//                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
//                // signature 에 들어갈 secret값 세팅
//                .compact();
//    }
//
//    public boolean validateToken(String token, Logger logger) {//유효성 검증
//        try {
//            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
//            return true;
//        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
//            logger.info("잘못된 JWT 서명입니다.");
//        } catch (ExpiredJwtException e) {
//            logger.info("만료된 JWT 토큰입니다.");
//        } catch (UnsupportedJwtException e) {
//            logger.info("지원되지 않는 JWT 토큰입니다.");
//        } catch (IllegalArgumentException e) {
//            logger.info("JWT 토큰이 잘못되었습니다.");
//        }
//        return false;
//    }

}

