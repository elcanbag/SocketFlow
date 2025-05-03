package com.example.cubesat.logging;

import com.example.cubesat.model.RequestLog;
import com.example.cubesat.repository.RequestLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class LoggingInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);
    private final RequestLogRepository repo;

    public LoggingInterceptor(RequestLogRepository repo) {
        this.repo = repo;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        String ip = request.getRemoteAddr();
        String method = request.getMethod();
        String path = request.getRequestURI();
        String params = Optional.ofNullable(request.getQueryString()).orElse("");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName()))
                ? auth.getName() : null;
        LocalDateTime ts = LocalDateTime.now();

        // log to file
        logger.info("User={} | IP={} | {} {} | params={}", username, ip, method, path, params);

        // persist to DB
        RequestLog log = RequestLog.builder()
                .username(username)
                .ip(ip)
                .method(method)
                .path(path)
                .params(params)
                .timestamp(ts)
                .build();
        repo.save(log);

        return true;
    }
}
