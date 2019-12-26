package com.multitenant.arc.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


@Component
public class TokenHelper {

    @Value("${app.name}")
    private String APP_NAME;

    @Value("${jwt.secret}")
    private String SECRET;

    @Value("${jwt.expires_in}")
    private int EXPIRES_IN;

    @Value("${jwt.header}")
    private String AUTH_HEADER;

    private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;


    /**
     * This will generate token base on username and tenant value.
     * It will also use static value AppName, Signature algorithm and Secret.
     * */
    public String generateToken(String username, String tenant) {
        return Jwts.builder()
                .setIssuer( APP_NAME )
                .setSubject(username+"#"+tenant)
                .setIssuedAt(generateCurrentDate())
                .setExpiration(generateExpirationDate())
                .signWith( SIGNATURE_ALGORITHM, SECRET )
                .compact();
    }


    /**
     * It will parse token and will fetch username.
     * */
    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = this.getClaimsFromToken(token);
            String usernameAndTenant = claims.getSubject();
            username = usernameAndTenant.split("#")[0];
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * It will parse token and will fetch tenant value
     * */
    public String getTenantFromToken(String token) {
        String tenant;
        try {
            final Claims claims = this.getClaimsFromToken(token);
            String usernameAndTenant = claims.getSubject();
            tenant = usernameAndTenant.split("#")[1];
        } catch (Exception e) {
            tenant = null;
        }
        return tenant;
    }

    /**
     * This will return claims which is used to generate token.
     * */
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(this.SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    public boolean isExpired(String token){
        Claims claims = getClaimsFromToken(token);
        if(claims == null){
            return true;
        } else {
            return claims.getExpiration().getTime() < getCurrentTimeMillis();
        }
    }

    private long getCurrentTimeMillis() {
        return new Date().getTime();
    }

    private Date generateCurrentDate() {
        return new Date(getCurrentTimeMillis());
    }

    private Date generateExpirationDate() {

        return new Date(getCurrentTimeMillis() + this.EXPIRES_IN * 1000);
    }

    /**
     * This will look for Auth header from request and will return token.
     * In case of not found, it will return null.
     * */
    public String getToken( HttpServletRequest request ) {

        String authHeader = request.getHeader(AUTH_HEADER);
        if ( authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }

}
