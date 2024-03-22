package codehanzoom.greenwalk.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

//@configuration : 설정 파일
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http    .formLogin(form -> form.disable())// FormLogin 사용 X
                .httpBasic(httpbasic -> httpbasic.disable()) // httpBasic 사용 X
                .csrf(r -> r.disable())// csrf 보안 사용 X
                .sessionManagement(r -> r.sessionCreationPolicy(SessionCreationPolicy.STATELESS));// 세션 사용하지 않으므로 STATELESS로 설정

        http
                .authorizeHttpRequests
                        (authorizeRequests -> authorizeRequests
                                .requestMatchers("/auth/**").permitAll()// 로그인,회원가입 경로
                                //.requestMatchers("/api/v1/admin/**").hasRole("ADMIN") // ADMIN 접속 URL => ADMIN 사용 시 주석 해제
                                .anyRequest().authenticated()); // 위의 경로 이외에는 모두 인증된 사용자만 접근 가능
        return http.build();
    }
}
