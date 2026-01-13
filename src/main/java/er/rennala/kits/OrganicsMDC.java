package er.rennala.kits;

import cn.hutool.core.util.StrUtil;
import er.rennala.request.filters.essential.EssentialFilter;
import jakarta.annotation.Nonnull;
import org.slf4j.MDC;

/**
 * <p> Organics MDC
 */
public class OrganicsMDC {

    private OrganicsMDC() {
    }

    /**
     * <p> 从 MDC 中获取请求 ID, 若无则返回空字符串
     * <p> {@link EssentialFilter} 负责将请求 ID 写入 MDC
     * <p> 在异步上下文中, requestID 为空
     *
     * @return 请求 ID, 若无则返回空字符串
     */
    @Nonnull
    public static String getRequestId() {
        String requestId = MDC.get("requestId");
        if (StrUtil.isEmpty(requestId)) {
            return "";
        } else {
            return requestId;
        }
    }

}
