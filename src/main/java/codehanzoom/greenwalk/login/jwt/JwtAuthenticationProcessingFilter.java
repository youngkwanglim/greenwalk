package codehanzoom.greenwalk.login.jwt;

import codehanzoom.greenwalk.user.domain.User;
import codehanzoom.greenwalk.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Jwt 인증 필터
 * "/auth/login" 이외의 URI 요청이 왔을 때 처리하는 필터
 *
 * 기본적으로 사용자는 요청 헤더에 AccessToken만 담아서 요청
 * AccessToken 만료 시에만 RefreshToken을 요청 헤더에 AccessToken과 함께 요청
 *
 * 1. RefreshToken이 없고, AccessToken이 유효한 경우 -> 인증 성공 처리, RefreshToken을 재발급하지는 않는다.
 * 2. RefreshToken이 없고, AccessToken이 없거나 유효하지 않은 경우 -> 인증 실패 처리, 403 ERROR
 * 3. RefreshToken이 있는 경우 -> DB의 RefreshToken과 비교하여 일치하면 AccessToken 재발급, RefreshToken 재발급
 *                              인증 성공 처리는 하지 않고 실패 처리
 *
 */

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getRequestURI().equals("/auth/login")){
           filterChain.doFilter(request,response); // "/login" 요청이 들어오면, 다음 필터 호출
           return; // return으로 이후 현재 필터 진행 막기 (안해주면 아래로 내려가서 계속 필터 진행시킴)
        }

        // 사용자 요청 헤더에서 RefreshToken 추출하고
        // RefreshToken이 없거나 유효하지 않으면 NUll, 있으면 AccessToken 새로 발급
        String refreshToken = jwtService.extractRefreshToken(request)
                .filter(token -> jwtService.isTokenValid(token))
                .orElse(null);

        // 리프레시 토큰이 요청 헤더에 존재했다면, 사용자가 AccessToken이 만료되어서
        // RefreshToken까지 보낸 것이므로 리프레시 토큰이 DB의 리프레시 토큰과 일치하는지 판단 후,
        // 일치한다면 AccessToken을 재발급해준다.
        if(refreshToken != null){
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
        }

        // RefreshToken이 없거나 유효하지 않다면, AccessToken을 검사하고 인증을 처리하는 로직 수행
        // AccessToken이 없거나 유효하지 않다면, 인증 객체가 담기지 않은 상태로 다음 필터로 넘어가기 때문에 403 에러 발생
        // AccessToken이 유효하다면, 인증 객체가 담긴 상태로 다음 필터로 넘어가기 때문에 인증 성공
        if(refreshToken == null){
            log.info("refreshToken = null");
            checkAccessTokenAndAuthentication(request,response,filterChain);
        }
    }

    /**
     * RefreshToken이 들어온 경우 실행되는 메서드
     * RefreshToken가 유효한지 체크한 뒤에 User 객체가 반환된다
     * 반환된 User 객체를 이용하여 새로운 RefreshToken과 AccessToken을 생성한 뒤에
     * 두 개의 토큰을 response 헤더에 추가한다.
     */

     public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) throws ServletException,IOException {
        userRepository.findByRefreshToken(refreshToken) // refreshToken 유효성 check
                        .ifPresent(user -> {
                            String reIssuedRefreshToken = reIssueRefreshToken(user); // refreshToken 재발급 후 저장
                            // accessToken과 refreshToken 전송
                            try {
                                jwtService.sendAccessAndRefreshToken(response, jwtService.createAccessToken(user.getEmail()), reIssuedRefreshToken);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });

    }

    /**
     *  RefreshToken을 재발급하는 메서드
     * RefreshToken을 생성, DB에 save,commit 후
     * RefreshToken을 반환
     */

    private String reIssueRefreshToken(User user) {
        String reIssuedRefreshToken = jwtService.createRefreshToken();
        user.updateRefreshToken(reIssuedRefreshToken);
        userRepository.saveAndFlush(user);
        return reIssuedRefreshToken;
    }

    /** [Aceess 토큰 체크 & 인증 처리]
     * 엑세스 토큰 추출한 후 유효한 토큰인지 확인
     * (유요한 토큰인 경우) 엑세스 토큰에서 이메일 추출
     * 추출한 이메일로 User 객체를 인증처리 saveAuthentication()
     * 인증 허가 처리된 객체를 SecurityContextHolder에 담기
     *  -> 인증 필터에서 getAuthentication 하여 인증된 User인지 확인
     * 그 다음 인증 필터로 진행
     */

    public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("checkAccessTokenAndAuthentication");

        jwtService.extractAccessToken(request)
                .filter(AccessToken -> jwtService.isTokenValid(AccessToken))
                .ifPresent(AccessToken -> jwtService.extractEmail(AccessToken)
                    .ifPresent(email -> userRepository.findByEmail(email)
                            .ifPresent(user -> {
                                log.info("user 정보"+ user.getEmail());
                                this.saveAuthentication(user);
                            })));

        filterChain.doFilter(request, response);
    }

    /** [인증 허가 메소드]
     * 파라미터 : checkAccessTokenAndAuthentication 메소드 마지막에 만든 user 객체(AccessToken에서 email 추출한 후 findbyEmail로 얻은 user 객체)
     * UsernamePasswordAuthentication의 인증 객체 Authentication 생성
     * UsernamePasswordAuthenticationd의 파라미터 1. 유저 객체 2. credential 3.Collection< ? extends GrantedAuthority>
     */
    public void saveAuthentication(User myUser) {
        UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
                .username(myUser.getEmail())
                .password(myUser.getPassword())
                .roles(myUser.getRole().name())
                .build();
        log.info("role" + userDetailsUser.getAuthorities());

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetailsUser, null, authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}

