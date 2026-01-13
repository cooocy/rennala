package er.rennala.kits;

import com.fasterxml.jackson.databind.ObjectMapper;
import er.rennala.response.BizException;
import er.rennala.response.Codes;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;

/**
 * <p> Jackson 对象映射工具类
 */
@Slf4j
public final class JacksonObjectMapper {

    private static final ObjectMapper objectMapper = SpringKits.getBean(ObjectMapper.class);

    /**
     * <p> 将对象转换为 JSON 字符串
     *
     * @param obj 待转换的对象
     * @return JSON 字符串
     * @throws BizException {@link Codes#DataError}
     */
    @Nonnull
    public static String toJsonString(@Nonnull Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("JacksonObjectMapper toJsonString error: ", e);
            throw new BizException(Codes.DataError, "Object to JSON error.");
        }
    }

    /**
     * <p> 将 JSON 字符串转换为对象
     *
     * @param json  待转换的 JSON 字符串
     * @param clazz 目标对象类型
     * @param <T>   目标对象类型泛型
     * @return 目标对象
     * @throws BizException {@link Codes#DataError}
     */
    @Nonnull
    public static <T> T readJsonString(@Nonnull String json,
                                       @Nonnull Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            log.error("JacksonObjectMapper readJsonString error: ", e);
            throw new BizException(Codes.DataError, "JSON to Object error.");
        }
    }

    /**
     * <p> 将一个对象转换为另一个类型的对象
     *
     * @param fromValue   待转换的对象
     * @param toValueType 目标对象类型
     * @param <T>         目标对象类型泛型
     * @return 目标对象
     * @throws BizException {@link Codes#DataError}
     */
    @Nonnull
    public static <T> T convert(@Nonnull Object fromValue,
                                @Nonnull Class<T> toValueType) {
        try {
            return objectMapper.convertValue(fromValue, toValueType);
        } catch (Exception e) {
            log.error("JacksonObjectMapper convertValue error: ", e);
            throw new BizException(Codes.DataError, "Object convert error.");
        }
    }

}
