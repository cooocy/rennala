package er.rennala.request.filters.log;

import cn.hutool.core.net.URLDecoder;

import cn.hutool.core.util.StrUtil;
import er.rennala.request.filters.FilterOrder;
import er.rennala.request.filters.RequestHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p> 请求日志过滤器.
 */
@Slf4j
@Order(FilterOrder.RequestLogFilter)
public class RequestLogFilter extends OncePerRequestFilter {

    private final RequestLogProperties p;

    public RequestLogFilter(RequestLogProperties requestLogProperties) {
        this.p = requestLogProperties;
        log.info("[RNA-RequestLogF] Properties: enable={}, verbose={}", p.isEnable(), p.isVerbose());
        logger.info("[RNA-RequestLogF] Loaded.");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 如果未开启日志记录, 则直接放行
        if (!p.isEnable()) {
            filterChain.doFilter(request, response);
            return;
        }

        // 开启日志记录
        // 开启详细记录
        log.info("[RNA-RequestLogF] B ----------------------------------------------------------------------------------");
        if (p.isVerbose()) {
            ContentCachingRequestWrapper req = new ContentCachingRequestWrapper(request);
            ContentCachingResponseWrapper res = new ContentCachingResponseWrapper(response);
            filterChain.doFilter(req, res);

            String queryString = URLDecoder.decode(req.getQueryString(), Charset.defaultCharset());
            if (StrUtil.isEmpty(queryString)) {
                queryString = "";
            }
            log.info("[RNA-RequestLogF] Query String: {}", queryString);
            log.info("[RNA-RequestLogF] Request Body: {}", new String(req.getContentAsByteArray()));
            logResponseBody(res);

            // fixme: 当 otherFilter.doFilterInternal() 里未执行 filterChain.doFilter(), 这里的 req.getContentAsByteArray() 会获取到空.
            res.copyBodyToResponse();
        } else {
            filterChain.doFilter(request, response);
        }

        Instant occurredAt = RequestHelper.getOccurredAt(request);
        long occurredAtMs = occurredAt.toEpochMilli();
        long end = Instant.now().toEpochMilli();

        Map<String, Object> requestLog = new LinkedHashMap<>();
        requestLog.put("requestId", RequestHelper.getRequestId(request));
        requestLog.put("occurredAt", occurredAt);
        requestLog.put("duration", end - occurredAtMs + "ms");
        requestLog.put("uri", request.getRequestURI());
        requestLog.put("clientIp", RequestHelper.getIp(request));
        requestLog.put("userAgent", request.getHeader("user-agent"));
        log.info("[RNA-RequestLogF] Request Log: {}", requestLog);
        log.info("[RNA-RequestLogF] E ----------------------------------------------------------------------------------");
    }

    /**
     * <p> 记录响应体内容
     * <p> 仅当响应头包含 application/json 时打印完整响应体, 否则只打印响应体大小
     */
    private void logResponseBody(ContentCachingResponseWrapper response) {
        // 响应头还未添加, 不能根据响应头判断
        int contentSize = response.getContentSize();
        if (contentSize < 65536) {
            // 小于 64KB 的响应体, 直接打印
            log.info("[RNA-RequestLogF] Response Body: {}", new String(response.getContentAsByteArray()));
        } else {
            // 否则打印 size
            log.info("[RNA-RequestLogF] Response Body Size: {} bytes", contentSize);
        }
    }

}