package com.tracker.tasks.config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static javax.crypto.Cipher.SECRET_KEY;


@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims :: getSubject); // tek claim extract yaparken bu yapı.
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) { // extract one claim (that you want)
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }
    // en kolay yol bu . extra claim( role, id gibi seyler) koymaz.
    // username + iat + exp  = payload





    // implement  the method the helps us the generate token
    // extraClaim : JWT payloada eklemek istediğim ekstra bilgiler
    // userDetails : jwt tokenının hangi kullanıcı icin olusturulacağını belirtir.
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails)
    {
        return Jwts
                .builder() // starts builder pattern
                .setClaims(extraClaims) // set extraClaims(bilgiler) into payload
                .setSubject(userDetails.getUsername()) // kullanıcıyı tanımlamak icin ex : sub: "meg123"
                .setIssuedAt(new Date(System.currentTimeMillis())) // token generated date
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration )) // token son kullanma tarihi
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // token signature kısmını üretir
                .compact(); // JWT'yi string haline getirecek.
    }


    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token); // token icindeki sub  claimden username cıkardık.
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
        // db deki username ile token icindeki username karsılastırıldı ve token süresi dolmus mu kontrol etti
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); // tokendan exp claimini alır ve karsılastırır
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration); // extractClaim metodu ile token’dan sadece exp değerini çıkarır.


    }


    private Claims extractAllClaims(String token) {    // extract all claims(all payloads)
        return Jwts
                .parserBuilder() // parser insa edildi
                .setSigningKey(getSignInKey()) // parsera hangi secret key ile imza doğrulaması yapacağnı söylüyoruz.
                .build() // parser object üret
                .parseClaimsJws(token) // Token'ı çözümler, Signature kısmı secret key ile doğrulanır,
                //süre dolması , imza uyusmaması veya format bozukluğuna göre hata fırlatır.
                .getBody(); // JWT payload kısmındaki JSON verileri döner. claims objesi olarak alırız
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // key byte array formatına dönüstürülür.
        return Keys.hmacShaKeyFor(keyBytes);
    }


    // jjwt raw byte array ister.
    // Base64 çözülüyor → byte[]
    // Byte array → Key objesi
}