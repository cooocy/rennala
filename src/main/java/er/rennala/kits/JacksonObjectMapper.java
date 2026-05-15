package er.rennala.kits;

import com.fasterxml.jackson.databind.JsonNode;
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
     * <p> 将 JSON 字符串转换为 JsonNode 树
     *
     * @param json 待转换的 JSON 字符串
     * @return JsonNode 树
     */
    @Nonnull
    public static JsonNode readJsonTree(@Nonnull String json) {
        try {
            return objectMapper.readTree(json);
        } catch (Exception e) {
            log.error("JacksonObjectMapper readJsonTree error: ", e);
            throw new BizException(Codes.DataError, "JSON to JsonNode error.");
        }
    }


    /**
     * <p> 将一个对象转换为另一个类型的对象.
     *
     * <p> 原理:
     * <p> ObjectMapper#convertValue 内部用 TokenBuffer (内存中的 Jackson token 流), 整个过程是:
     * <p> fromValue → 序列化器 → TokenBuffer (内存 token) → 反序列化器 → toValueType
     * <p> 中间没有生成 String 形态的 JSON 文本, 也不走 I/O, 所以省掉了字符编码/解析开销.
     * <p> 关注点:
     * <p> @JsonProperty, @JsonCreator, @JsonIgnore, 可见性设置都生效.
     * <p> Long → String（ToStringSerializer）也会生效. 于是反序列化的目标字段如果是 Long, 需要它能从字符串解析回来（Long 默认可以）.
     * <p> 因为本质就是「序列化再反序列化」, 所以精度会按序列化器规则截断. 例如 Instant 经过你的秒级序列化器后, 转出来的对象就只剩秒精度了.
     * <P> 性能上比直接字段拷贝 (MapStruct or 手写 set) 慢一两个数量级, 做高频热路径就别用 convertValue.
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
