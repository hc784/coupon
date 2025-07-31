package foodOrder.auth.security;

import foodOrder.auth.jwt.JwtAuthenticationFilter;
import foodOrder.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<?, ?> redisTemplate;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 기본 인증·CSRF 비활성화
            .httpBasic(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)

            // 세션을 사용하지 않는 JWT 방식
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // URL 권한 설정
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                		"/", "/login", "/sign-up",           // ← 추가
                    "/api/v1/users/sign-up", "/api/v1/users/login",
                    "/api/v1/users/authority", "/api/v1/users/reissue",
                    "/api/v1/users/logout"
                ).permitAll()
                .requestMatchers("/api/v1/users/userTest").hasRole("USER")
                .requestMatchers("/api/v1/users/adminTest").hasRole("ADMIN")
                .anyRequest().authenticated())

            // JWT 필터 등록
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, redisTemplate),
                             UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 비밀번호 암호화기
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
