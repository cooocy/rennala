package er.rennala.advice.ctx;

import er.rennala.advice.AdviceOrder;
import er.rennala.advice.Constants;
import er.rennala.advice.log.RequestLogAdvice;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

/**
 * 封装 Context.
 * 使用时, 需要实现 {@link ProfileAssembler}
 */
@Slf4j
@Order(AdviceOrder.ctx)
@Import(RequestLogAdvice.class)
public class ContextAdvice extends OncePerRequestFilter {

    private final ProfileAssembler profileAssembler;

    public ContextAdvice(ProfileAssembler profileAssembler) {
        this.profileAssembler = profileAssembler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestId = (String) request.getAttribute(Constants.sRequestId);
        Instant occurredAt = (Instant) request.getAttribute(Constants.sOccurredAt);
        Context context = new Context(requestId, occurredAt);
        Optional<String> ot = profileAssembler.getToken(request);
        Optional<AbstractProfile> op = profileAssembler.assemble(request);
        ot.ifPresent(context::setToken);
        op.ifPresent(context::setProfile);
        request.setAttribute(ContextKey.sCtx, context);
        log.info("[] Context: {}", context);
        filterChain.doFilter(request, response);
    }

}
