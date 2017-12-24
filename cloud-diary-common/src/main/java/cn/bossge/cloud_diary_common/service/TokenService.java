package cn.bossge.cloud_diary_common.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import cn.bossge.cloud_diary_common.enums.TokenMessage;
import cn.bossge.cloud_diary_common.exception.TokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenService {

	private static final String key = "test key";
	
	private static void verifyTokenExpiration(Claims body) {
		long time = body.getExpiration().getTime();
		if (LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()).plusDays(7L).isBefore(LocalDateTime.now())) {
			throw new TokenException(TokenMessage.EXPIRATION.info());
		}
	}
	
	public static Claims parseToken(String token) {
		Claims body = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
	    TokenService.verifyTokenExpiration(body);
		return body;
		
	}
	
    public static String createToken(String email, Long id) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneWeekLater = now.plusDays(7L);
        
		return Jwts.builder().setId(String.valueOf(id))
                .setSubject(email)
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setIssuer("www.minidiary.org")
                .setExpiration(Date.from(oneWeekLater.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }
	

}
