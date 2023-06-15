package er.rennala.advice.ctx;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

/**
 * 定义如何根据 Request 组装 Profile.
 */
public interface ProfileAssembler {

    /**
     * 从 request 中获取用户 Token
     */
    @Nonnull
    Optional<String> getToken(@Nonnull HttpServletRequest request);

    /**
     * 从 request 中获取用户 Token, 组装对应的 Profile.
     * 未指定 Token 或 Token 无效时, 返回 Empty.
     * 返回 Present 时, getToken() 一定也返回 Present.
     */
    @Nonnull
    Optional<AbstractProfile> assemble(@Nonnull HttpServletRequest request);

}
