package er.rennala.request.filters.ctx;

import er.rennala.request.filters.FilterOrder;
import er.rennala.request.filters.RequestHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

/**
 * <p> 封装 Context.
 * <p> 使用时, 需要实现 {@link ProfileAssembler} 和 {@link TokenPolice}.
 */
@Slf4j
@Order(FilterOrder.ContextFilter)
public class ContextFilter extends OncePerRequestFilter {

    private final TokenPolice tokenPolice;

    private final ProfileAssembler profileAssembler;

    private final ContextProperties p;

    public ContextFilter(TokenPolice tokenPolice, ProfileAssembler profileAssembler, ContextProperties properties) {
        this.tokenPolice = tokenPolice;
        this.profileAssembler = profileAssembler;
        this.p = properties;
        log.info("[RNA-ContextF] Properties: enable={}, log={}", p.isEnable(), p.isLog());
        logger.info("[RNA-ContextF] Loaded.");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 如果未开启, 则直接放行
        if (!p.isEnable()) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestId = RequestHelper.getRequestId(request);
        Instant occurredAt = RequestHelper.getOccurredAt(request);
        Context context = new Context(requestId, occurredAt);

        Optional<String> otk = tokenPolice.getTokenKey(request);
        otk.ifPresent(tk -> {
            context.setTokenKey(tk);
            Optional<Token> ot = tokenPolice.decodeToken(tk);
            ot.ifPresent(t -> {
                context.setToken(t);
                if (tokenPolice.isValid(t)) {
                    tokenPolice.refresh(t);
                    Optional<AbstractProfile> op = profileAssembler.assemble(t);
                    op.ifPresent(context::setProfile);
                }
            });

        });
        request.setAttribute(ContextReader.sCtx, context);

        if (p.isLog()) {
            log.info("[RNA-ContextF] Context: {}", context);
        }

        filterChain.doFilter(request, response);
    }

}
