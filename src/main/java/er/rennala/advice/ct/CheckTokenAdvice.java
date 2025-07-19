package er.rennala.advice.ct;

import com.fasterxml.jackson.databind.ObjectMapper;
import er.carian.response.BizException;
import er.carian.response.Codes;
import er.carian.response.R;
import er.rennala.advice.AdviceOrder;
import er.rennala.advice.ctx.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

/**
 * 根据配置文件中的白名单或黑名单, 检查请求是否携带了有效的 Token, 无效时返回 10001 + Token Not Found or Invalid
 * 两种模式不能同时生效.
 */
@Slf4j
@Order(AdviceOrder.checkToken)
public class CheckTokenAdvice extends OncePerRequestFilter {

    private final TokenPolice tokenPolice;

    private final ObjectMapper objectMapper;

    private final CheckTokenProperties p;

    private final String black = "black";

    private final String white = "white";

    public CheckTokenAdvice(TokenPolice tokenPolice, ObjectMapper objectMapper, CheckTokenProperties properties) {
        this.tokenPolice = tokenPolice;
        this.objectMapper = objectMapper;
        this.p = properties;
        String mode = properties.getMode();
        if (!black.equals(mode) && !white.equals(mode)) {
            log.warn("[RennalaAdvice] mode {} error, must be black or white. CheckTokenAdvice will not work.", mode);
        }
        log.info("[RennalaAdvice] CheckTokenProperties: enable={}, mode={}", p.isEnable(), p.getMode());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (p.isEnable()) {
            String mode = p.getMode();
            Set<String> uris = p.getUris();
            String uri = request.getRequestURI();
            if (black.equals(mode)) {
                if (Objects.nonNull(uris) && uris.contains(uri)) {
                    checkToken(request, response, filterChain);
                } else {
                    filterChain.doFilter(request, response);
                }
                return;
            }

            if (white.equals(mode)) {
                if (Objects.nonNull(uris) && uris.contains(uri)) {
                    filterChain.doFilter(request, response);
                } else {
                    checkToken(request, response, filterChain);
                }
                return;
            }
        }

        // 如果没有配置模式, 则直接放行
        filterChain.doFilter(request, response);
    }

    private void checkToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Context ctx = ContextReader.getContext(request);
        Token token = ctx.getToken();
        if (token != null && tokenPolice.isValid(token)) {
            filterChain.doFilter(request, response);
        } else {
            response.setHeader("Content-Type", "application/json");
            R<Void> r = R.err(new BizException(Codes.TokenInvalid));
            response.getWriter().write(objectMapper.writeValueAsString(r));
        }
    }

}
