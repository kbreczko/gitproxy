package com.github.kbreczko.gitproxy.configurations.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kbreczko.gitproxy.common.properties.GitproxyProperties;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

    private static final Set<String> URL_WHITELIST = Set.of(
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v2/api-docs"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestId = "ID_" + System.currentTimeMillis() + "_" + request.hashCode();
        MDC.put(GitproxyProperties.REQUEST_ID, requestId);

        if (isLogRequestEnabled(request)) {
            logRequest(requestId, request, response, filterChain);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private void logRequest(String requestId, HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        final ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            final String requestBody = new String(requestWrapper.getContentAsByteArray());
            final String responseBody = new String(responseWrapper.getContentAsByteArray());

            log.info("=Request= \n ID: {} \n URI: {} \n Parameters: {} \n Http-Method: {} \n Body: {}",
                    requestId,
                    requestWrapper.getRequestURI(),
                    new ObjectMapper().writeValueAsString(requestWrapper.getParameterMap()),
                    requestWrapper.getMethod(),
                    requestBody
            );

            log.info("=Response= \n ID: {} \n Response-Code: {} \n Body: {}",
                    requestId,
                    responseWrapper.getStatus(),
                    responseBody
            );

            responseWrapper.copyBodyToResponse();
        }
    }

    private boolean isLogRequestEnabled(HttpServletRequest request) {
        final AntPathMatcher pathMatcher = new AntPathMatcher();
        final String requestPath = new UrlPathHelper().getPathWithinApplication(request);
        return URL_WHITELIST.stream().noneMatch(pattern -> pathMatcher.match(pattern, requestPath));
    }
}
