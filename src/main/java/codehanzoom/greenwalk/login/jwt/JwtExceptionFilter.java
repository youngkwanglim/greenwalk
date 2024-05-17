package codehanzoom.greenwalk.login.jwt;

import codehanzoom.greenwalk.global.dto.ResponseDto;
import codehanzoom.greenwalk.user.domain.User;
import codehanzoom.greenwalk.user.repository.UserRepository;
import codehanzoom.greenwalk.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // "/auth/", "/swagger-ui" "/v3/api-docs"로 시작하는 URI는 허용
        if (request.getRequestURI().startsWith("/auth/") || request.getRequestURI().startsWith("/swagger-ui") || request.getRequestURI().startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        // accessToken 추출한 뒤 String 변수에 저장
        String accessToken = jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);

        // 만약 accessToken이 null이라면 오류 반환
        if (accessToken == null) {
            jwtExceptionHandler(response);
            return;
        }

        //다음 filter 진행
        filterChain.doFilter(request, response);

    }

    public void jwtExceptionHandler(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String result = objectMapper.writeValueAsString(new ResponseDto<String>(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 엑세스토큰입니다"));
        response.getWriter().write(result);

    }
}
