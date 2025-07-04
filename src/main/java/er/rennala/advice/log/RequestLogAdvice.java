package er.rennala.advice.log;

import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.util.IdUtil;

import cn.hutool.core.util.StrUtil;
import er.rennala.advice.AdviceOrder;
import er.rennala.advice.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 记录 Request Access Log.
 * 封装 {@link Constants#sOccurredAt} 和 {@link Constants#sRequestId} 属性到 Request 中.
 * 记录 {@link Constants#sRequestId} 到 MDC.
 */
@Slf4j
@Order(AdviceOrder.log)
public class RequestLogAdvice extends OncePerRequestFilter {

    private final RequestLogProperties p;

    public RequestLogAdvice(RequestLogProperties requestLogProperties) {
        this.p = requestLogProperties;
        log.info("[RennalaAdvice] RequestLogProperties: enable={}, verbose={}", p.isEnable(), p.isVerbose());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 生成发生时间和请求 ID
        Instant occurredAt = Instant.now();
        long occurredAtMs = occurredAt.toEpochMilli();
        String requestId = occurredAtMs + "-" + IdUtil.fastSimpleUUID().toUpperCase();

        // 放入上下文
        request.setAttribute(Constants.sOccurredAt, occurredAt);
        request.setAttribute(Constants.sRequestId, requestId);
        MDC.put(Constants.sRequestId, requestId);

        // 添加请求 ID 到响应头
        response.addHeader("X-Request-Id", requestId);

        // 如果未开启日志记录, 则直接放行
        if (!p.isEnable()) {
            filterChain.doFilter(request, response);
            return;
        }

        // 开启日志记录
        // 开启详细记录
        log.info("[RL] B ----------------------------------------------------------------------------------");
        if (p.isVerbose()) {
            ContentCachingRequestWrapper req = new ContentCachingRequestWrapper(request);
            ContentCachingResponseWrapper res = new ContentCachingResponseWrapper(response);
            filterChain.doFilter(req, res);

            String queryString = URLDecoder.decode(req.getQueryString(), Charset.defaultCharset());
            if (StrUtil.isEmpty(queryString)) {
                queryString = "";
            }
            log.info("[RL] Request String: {}", queryString);
            log.info("[RL] Request Body: {}", new String(req.getContentAsByteArray()));
            log.info("[RL] Response Body: {}", new String(res.getContentAsByteArray()));

            // fixme: 当 otherFilter.doFilterInternal() 里未执行 filterChain.doFilter(), 这里的 req.getContentAsByteArray() 会获取到空.
            res.copyBodyToResponse();
        } else {
            filterChain.doFilter(request, response);
        }

        long end = System.currentTimeMillis();
        Map<String, Object> requestLog = new LinkedHashMap<>();
        requestLog.put(Constants.sOccurredAt, occurredAt);
        requestLog.put(Constants.sRequestId, requestId);
        requestLog.put("uri", request.getRequestURI());
        requestLog.put("duration", end - occurredAtMs + "ms");
        requestLog.put("clientIp", getIp(request));
        requestLog.put("userAgent", request.getHeader("user-agent"));
        log.info("[RL] Request Log: {}", requestLog);
        log.info("[RL] E ----------------------------------------------------------------------------------");
    }

    private static String getIp(HttpServletRequest request) {
        Enumeration<String> xff = request.getHeaders("X-Forwarded-For");
        if (!xff.hasMoreElements()) {
            return "";
        }

        String xffHeader = xff.nextElement();
        return xffHeader.contains(",") ? xffHeader.substring(0, xffHeader.indexOf(",")) : xffHeader;
    }

}