package com.spotpulse.backend.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class FirebaseAuthFilter implements Filter {

    private static final String WEB_API_KEY = "AIzaSyDtzrXJkNgCpsm91jsse6tl-bFsr4i0MEQ";

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String idToken = authHeader.substring(7);
            try {
                String url = "https://identitytoolkit.googleapis.com/v1/accounts:lookup?key=" + WEB_API_KEY;
                Map<String, String> body = Map.of("idToken", idToken);
                Map response2 = restTemplate.postForObject(url, body, Map.class);

                List users = (List) response2.get("users");
                if (users == null || users.isEmpty()) {
                    throw new RuntimeException("유효하지 않은 토큰");
                }
                Map user = (Map) users.get(0);
                String uid = (String) user.get("localId");

                request.setAttribute("uid", uid);
                chain.doFilter(request, response);
            } catch (Exception e) {
                System.out.println("토큰 검증 실패: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":401,\"message\":\"유효하지 않은 토큰입니다.\"}");
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}