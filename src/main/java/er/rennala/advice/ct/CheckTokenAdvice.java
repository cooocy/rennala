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
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

/**
 * 根据配置文件中的白名单或黑名单, 检查请求是否携带了有效的 Token, 无效时返回 10001 + Token Not Found or Invalid
 * 两种模式不能同时生效.
 * auth:
 * * uri:
 * *** black:
 * ***** - x
 * ***** - y
 * *
 * *** white:
 * ***** - a
 * ***** - b
 * *
 * *** mode: black/white
 */
@Slf4j
@Order(AdviceOrder.checkToken)
@Import({ContextAdvice.class, CheckedUris.class})
public class CheckTokenAdvice extends OncePerRequestFilter {

    private final CheckedUris checkedUris;

    private final TokenPolice tokenPolice;

    private final ObjectMapper objectMapper;

    public CheckTokenAdvice(CheckedUris checkedUris, TokenPolice tokenPolice, ObjectMapper objectMapper) {
        this.checkedUris = checkedUris;
        this.tokenPolice = tokenPolice;
        this.objectMapper = objectMapper;
        String mode = checkedUris.getMode();
        if (mode != null && !"black".equals(mode) && !"white".equals(mode)) {
            log.warn("mode {} error, must be black or white.", mode);
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String mode = checkedUris.getMode();
        String uri = request.getRequestURI();
        if ("black".equals(mode)) {
            Set<String> black = checkedUris.getBlack();
            if (black != null && black.contains(uri)) {
                checkToken(request, response, filterChain);
                return;
            } else {
                filterChain.doFilter(request, response);
                return;
            }
        }
        if ("white".equals(mode)) {
            Set<String> white = checkedUris.getWhite();
            if (white != null && white.contains(uri)) {
                filterChain.doFilter(request, response);
                return;
            } else {
                checkToken(request, response, filterChain);
                return;
            }
        }
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
