package sg.edu.ntu.in6206.orchestrator.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SagaIdFilter extends OncePerRequestFilter {

    public static final String HEADER = "X-Saga-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String sagaId = request.getHeader(HEADER);
        MDC.put("sagaId", sagaId != null ? sagaId : "-");
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}