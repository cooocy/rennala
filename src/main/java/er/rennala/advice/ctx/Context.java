package er.rennala.advice.ctx;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

/**
 * 表示一个请求的上下文.
 */
@Getter
@ToString
public class Context {

    /**
     * 请求 id
     */
    @Nonnull
    private final String requestId;

    /**
     * 发生时间, 后端过滤器产生
     */
    @Nonnull
    private final Instant occurredAt;

    /**
     * 请求 Token
     */
    @Setter
    @Nullable
    private String token;

    /**
     * 用户侧写, 只有当 token 有效时才能获取到 profile
     */
    @Setter
    @Nullable
    private AbstractProfile profile;

    public Context(@Nonnull String requestId, @Nonnull Instant occurredAt) {
        this.requestId = requestId;
        this.occurredAt = occurredAt;
    }

}
