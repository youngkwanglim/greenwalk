package codehanzoom.greenwalk.login.handler;

import codehanzoom.greenwalk.login.jwt.JwtService;
import codehanzoom.greenwalk.user.domain.User;
import codehanzoom.greenwalk.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String email = extractUsername(authentication);
        String accessToken = jwtService.createAccessToken(email);
        String refreshToken = jwtService.createRefreshToken();

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                            user.updateRefreshToken(refreshToken);
                            userRepository.saveAndFlush(user);
                        }
                );

        log.info("로그인에 성공하였습니다. 이메일 : {}", email);
        log.info("로그인에 성공하였습니다. AccessToken : {}", accessToken);

    }

    private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

}
