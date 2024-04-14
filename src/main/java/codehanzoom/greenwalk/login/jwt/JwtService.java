package codehanzoom.greenwalk.login.jwt;

import codehanzoom.greenwalk.global.dto.ResponseDto;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL_CLAIM = "email";
    private static final String BEARER = "Bearer ";

    private final ObjectMapper objectMapper;

    // AccessToken 생성 메서드
    public String createAccessToken(String email){
        Date now = new Date();
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod)) //60시간
                .withClaim(EMAIL_CLAIM, email)
                .sign(Algorithm.HMAC512(secretKey));
    }

    //request에서 RefreshToken을 추출하는 메서드
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    //Token이 유효한지 반환하는 메서드
    public boolean isTokenValid(String Token){
        try{
            log.info("Token : " + Token);
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(Token);
            return true;
        }catch(Exception e){
            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
            return false;
        }
    }

    //AccessToken과 RefreshToken을 response의 header에 첨부하는 메서드
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String reIssuedRefreshToken) throws IOException, IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        setAccessTokenHeader(response,accessToken);
        setRefreshTokenHeader(response,reIssuedRefreshToken);

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        String result = objectMapper.writeValueAsString(new ResponseDto<String>(HttpStatus.OK.value(), "로그인 완료"));
        response.getWriter().write(result);

        log.info("Access Token, RefreshToken 헤더 설정 완료");
    }

    private void setRefreshTokenHeader(HttpServletResponse response, String reIssuedRefreshToken) {
        response.setHeader(refreshHeader,reIssuedRefreshToken);
    }

    private void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, accessToken);
    }

    //AccessToken을 Header에서 추출하는 메서드
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        log.info("extractAccessToken");
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(AccessToken -> AccessToken.startsWith(BEARER))
                .map(AccessToken -> AccessToken.replace(BEARER, ""));

    }

    // AccessToken에서 이메일을 추출하는 메서드
    public Optional<String> extractEmail(String accessToken) {
       try {

           return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                   .build() //변환된 빌더로 JWT verifier 생성
                   .verify(accessToken) //accessToken을 검증하고 유효하지 않으면 예외 발생
                   .getClaim(EMAIL_CLAIM) // email 정보 추출하기
                   .asString());
       }catch (Exception e){
           log.error("엑세스 토큰이 유효하지 않습니다.");
           return Optional.empty();
       }
    }

    // RefreshToken 생성하는 메서드
    public String createRefreshToken() {
        Date now = new Date();
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + 12*24*60*60*1000))
                .sign(Algorithm.HMAC512(secretKey));
    }
}
