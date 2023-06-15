package er.rennala.advice.ctx;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

/**
 * 定义如何从 Request 中获取 Token Key, 如何解析 Token, 如何判断 Token 有效, 如何续期 Token.
 */
public interface TokenPolice<T extends Token> {

    /**
     * 从 Request 中获取 Token Key
     */
    @Nonnull
    Optional<String> getTokenKey(@Nonnull HttpServletRequest request);

    /**
     * 根据 Token Key 解析 Token
     *
     * @param tokenKey Token Key
     */
    @Nonnull
    Optional<T> decodeToken(@Nonnull String tokenKey);

    /**
     * 判断 Token 是否有效
     *
     * @param token Token
     * @return true 有效
     */
    boolean isValid(@Nonnull T token);

    /**
     * 刷新 Token
     *
     * @param token Token
     */
    void refresh(@Nonnull T token);

}
