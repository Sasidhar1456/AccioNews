        package com.example.AccioNews.config;

        import io.jsonwebtoken.ExpiredJwtException;
        import jakarta.servlet.FilterChain;
        import jakarta.servlet.ServletException;
        import jakarta.servlet.http.HttpServletRequest;
        import jakarta.servlet.http.HttpServletResponse;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
        import org.springframework.security.core.authority.SimpleGrantedAuthority;
        import org.springframework.security.core.context.SecurityContextHolder;
        import org.springframework.stereotype.Component;
        import org.springframework.web.filter.OncePerRequestFilter;

        import java.io.IOException;
        import java.util.List;

        @Component
        public class JwtAuthFilter extends OncePerRequestFilter {

            @Autowired
            private JwtUtil jwtUtil;

            @Override
            protected void doFilterInternal(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain filterChain)
                    throws ServletException, IOException {

                final String authHeader = request.getHeader("Authorization");

                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    String jwt = authHeader.substring(7);
                    try {
                            Boolean isValid = jwtUtil.validateToken(jwt);

                        if (isValid) {
                            Long userId = jwtUtil.extractUserId(jwt);
                            String userName = jwtUtil.extractUserName(jwt);

                            request.setAttribute("userId", userId);
                            request.setAttribute("userName", userName);

                            // Debug logs
                            System.out.println("userId: " + userId);
                            System.out.println("userName: " + userName);

                            UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                userName,
                                null,
                            List.of(new SimpleGrantedAuthority("ROLE_USER"))
                            );

                            System.out.println("Correct");

                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    } catch (ExpiredJwtException e) {
                        response.setContentType("application/json");
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("{\"message\": \"Access token expired\"}");
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        response.setContentType("application/json");
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("{\"message\": \"Invalid or missing token\"}");
                        return;
                    }


                }

                filterChain.doFilter(request, response);
            }

            @Override
            protected boolean shouldNotFilter(HttpServletRequest request) {
                String path = request.getServletPath();
                return path.equals("/user/v1/signup") ||
                    path.equals("/user/v1/login") ||
                    path.equals("/user/v1/refresh-token") ||
                    path.equals("/user/v1/logout"); 
            }
        }
