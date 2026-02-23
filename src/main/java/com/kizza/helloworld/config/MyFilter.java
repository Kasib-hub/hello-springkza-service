package com.kizza.helloworld.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.util.Objects;


    /*
    This filter allows me to decorate or edit all requests coming in, something useful would be capturing a bearer token
    And decoding for some additional processing

    I originally got stuck because I thought a controller advice would catch an error thrown from a filter but this is not
    possible as the Filter executes before the controller. Instead, write the error into the response that comes through
    the filter
     */

@Component
@RequiredArgsConstructor
@Slf4j
public class MyFilter implements Filter {

    private static final String X_TEST_HEADER = "x-test-header";
    private final ObjectMapper objectMapper;
    private final AppProperties appProperties;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException {
        try {
            final ContentCachingRequestWrapper contentCachingRequestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) servletRequest);
            final HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
            if (appProperties.isSkipFilter()) {
                filterChain.doFilter(contentCachingRequestWrapper, servletResponse);
            } else {
                // the message here is like the error
                final String testHeader = Objects.requireNonNull(
                        httpRequest.getHeader(X_TEST_HEADER),
                        String.format("%s is missing from the incoming request", X_TEST_HEADER)
                );

                log.info("myFilter executed and sees x-test-header: {}", testHeader);

                // proceed with response
                filterChain.doFilter(contentCachingRequestWrapper, servletResponse);
            }

        } catch (Exception error) {
            /*
            We can throw the 500 error with the customized message
            new ResponseStatusException formats the error in a nice way and then we write it to the servlet response
             */
            log.error("Error reading request: {}", error.getMessage());
            servletResponse.setContentType("application/json");
            ((HttpServletResponse) servletResponse)
                    .setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            this.objectMapper.writeValue(servletResponse.getWriter(), new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, error.getMessage()).getBody());
            servletResponse.getWriter().flush();
            servletResponse.getWriter().close();
        }
    }
}
