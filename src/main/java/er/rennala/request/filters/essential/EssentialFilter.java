package er.rennala.request.filters.essential;

import cn.hutool.core.util.IdUtil;
import er.rennala.request.filters.FilterOrder;
import er.rennala.request.filters.RequestHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;

/**
 * <p> 必要过滤器.
 * <p> 封装发生时间和请求 ID 属性到 Request 中, 属性名在 {@link RequestHelper} 中定义.
 * <p> 记录 RequestId 到 MDC.
 * <p> 记录 RequestId 到响应头.
 */
@Slf4j
@Order(FilterOrder.EssentialFilter)
public class EssentialFilter extends OncePerRequestFilter {

    public EssentialFilter() {
        logger.info("[RNA-EssentialF] Loaded.");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 生成发生时间和请求 ID
        Instant occurredAt = Instant.now();
        String requestId = occurredAt.toEpochMilli() + "-" + IdUtil.fastSimpleUUID().toUpperCase();

        RequestHelper.putOccurredAt(request, occurredAt);
        RequestHelper.putRequestId(request, requestId);

        MDC.put("requestId", requestId);

        response.addHeader("X-Request-Id", requestId);

        filterChain.doFilter(request, response);
    }

}
