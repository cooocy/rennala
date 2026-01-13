package er.rennala.kits;

import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Nonnull;

import java.util.Map;
import java.util.Objects;

/**
 * <p> 安全的 Map Getter, 可以安全地从 Map 里获取值并转换类型.
 * <p> 注意 Map.getOrDefault() 方法并不能正确处理 value 为 null 的情况.
 */
public class SafeMapGetter {

    /**
     * <p> 从 Map 里安全地获取 String 类型的值, 存在以下任意一种情况, 则返回默认值:
     * <p> key 不存在
     * <p> key 对应的值为 null
     * <p> key 对应的值不是 String 类型
     * <p> key 对于的值是 String 但 empty/blank
     *
     * @param m            Map 容器
     * @param key          键
     * @param defaultValue 默认值
     * @param <K>          键类型
     * @return 值或默认值
     */
    public static <K> String getString(@Nonnull Map<K, Object> m,
                                       @Nonnull K key,
                                       @Nonnull String defaultValue) {
        Object value = m.get(key);
        if (Objects.isNull(value)) {
            return defaultValue;
        }
        if (value instanceof String stringV && StrUtil.isNotEmpty(stringV)) {
            return stringV;
        }
        return defaultValue;
    }

    /**
     * <p> 从 Map 里安全地获取 Integer 类型的值, 存在以下任意一种情况, 则返回默认值:
     * <p> key 不存在
     * <p> key 对应的值为 null
     * <p> key 对应的值不是 Integer 类型
     *
     * @param m            Map 容器
     * @param key          键
     * @param defaultValue 默认值
     * @param <K>          键类型
     * @return 值或默认值
     */
    public static <K> Integer getInteger(@Nonnull Map<K, Object> m,
                                         @Nonnull K key,
                                         @Nonnull Integer defaultValue) {
        Object value = m.get(key);
        if (Objects.isNull(value)) {
            return defaultValue;
        }
        if (value instanceof Integer intV) {
            return intV;
        }
        return defaultValue;
    }

}
