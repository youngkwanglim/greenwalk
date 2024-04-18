package codehanzoom.greenwalk.logout.handler;

import codehanzoom.greenwalk.login.jwt.JwtService;
import codehanzoom.greenwalk.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;


@Slf4j
@RequiredArgsConstructor
public class LogoutHandler implements org.springframework.security.web.authentication.logout.LogoutHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        this.checkAccessTokenAndUpdateRefreshToken(request);
    }

    public void checkAccessTokenAndUpdateRefreshToken(HttpServletRequest request) {
        jwtService.extractAccessToken(request)
                .filter(AccessToken -> jwtService.isTokenValid(AccessToken))
                .ifPresent(AccessToken -> jwtService.extractEmail(AccessToken)
                        .ifPresent(email -> userRepository.findByEmail(email)
                                .ifPresent(user -> {
                                    user.updateRefreshToken("");
                                    userRepository.save(user);
                                })));
    }
}

