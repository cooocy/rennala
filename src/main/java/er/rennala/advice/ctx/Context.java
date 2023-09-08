package er.rennala.advice.ctx;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
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
    private final String requestId;

    /**
     * 发生时间, 后端过滤器产生
     */
    private final Instant occurredAt;

    /**
     * 用户携带的 Token Key.
     */
    @Setter
    @Nullable
    private String tokenKey;

    /**
     * 根据 Token Key 解析的 Token.
     */
    @Setter
    @Nullable
    private Token token;

    /**
     * 根据解析到的 Token 获取的用户侧写
     */
    @Setter
    @Nullable
    private AbstractProfile profile;

    public Context(String requestId, Instant occurredAt) {
        this.requestId = requestId;
        this.occurredAt = occurredAt;
    }

}
