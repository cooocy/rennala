package er.rennala.request.filters.ctx;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

/**
 * 定义如何从 Request 中获取 Token Key, 如何解析 Token, 如何判断 Token 有效, 如何续期 Token.
 */
public interface TokenPolice<T extends Token> {

    /**
     * 从 Request 中获取 Token Key
     */
    Optional<String> getTokenKey(HttpServletRequest request);

    /**
     * 根据 Token Key 解析 Token
     *
     * @param tokenKey Token Key
     */
    Optional<T> decodeToken(String tokenKey);

    /**
     * 判断 Token 是否有效
     *
     * @param token Token
     * @return true 有效
     */
    boolean isValid(T token);

    /**
     * 刷新 Token
     *
     * @param token Token
     */
    void refresh(T token);

}
