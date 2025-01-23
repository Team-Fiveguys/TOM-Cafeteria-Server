package fiveguys.Tom.Cafeteria.Server.filter;

/**
 header에서 액세스 토큰 가져와 검증
 유효하지 않으면 예외 발생
 */

import fiveguys.Tom.Cafeteria.Server.auth.UserContext;
import fiveguys.Tom.Cafeteria.Server.auth.jwt.service.JwtTokenProvider;
import fiveguys.Tom.Cafeteria.Server.auth.jwt.service.JwtUtil;
import fiveguys.Tom.Cafeteria.Server.user.entity.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUtil jwtUtil;
    private final static String[] ignorePrefix = {"/sign-up", "/swagger-ui", "/v3/api-docs", "/auth", "/oauth2", "/health"
            , "/token/validate", "/token/access-token" , "/message", "/enums", "/users/nickname", "/cafeterias", "/diets", "/version"};
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("url ={} method = {}", request.getRequestURI(), request.getMethod());
        // 지정된 Path는 건너뛰기
        String currentPath = request.getServletPath();
        for(String ignorePath: ignorePrefix){
            if ( currentPath.startsWith(ignorePath) ) {
                doFilter(request, response, filterChain);
                return;
            }
        }

        // 토큰 파싱해서 id 얻어오기
        String accessToken = null;
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            accessToken = authHeader.substring(7);
        }
        // 토큰을 검증
        jwtTokenProvider.verifyToken(accessToken);

        try{
            // 얻은 아이디로 유저 조회하기
            Long userId = Long.valueOf(jwtUtil.getUserId(accessToken));
            if( currentPath.startsWith("/admin")) {
                // admin role이 필요한 요청
                String requesterRole = jwtUtil.getRole(accessToken);
                log.info("requesterRole = {} ", requesterRole);
                if(!requesterRole.equals(Role.ADMIN.toString())){
                    // 요청자가 admin Role이 아니면 인가하지 않음
                    response.sendError(401, "Unauthorized");
                }
            }
            // 조회한 유저id ThreadLocal에 저장하기
            UserContext.setUserId(userId);
            filterChain.doFilter(request, response);
        }
        finally {
            UserContext.clearUserId();
        }
    }
}
