package er.rennala.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeFormatterBuilder;

/**
 * <p> Jackson ObjectMapper 客制化
 * <p> SpringBoot 在构建 ObjectMapper 后会回调所有该接口.
 */
public class JacksonCustomizer implements Jackson2ObjectMapperBuilderCustomizer {

    @Override
    public void customize(Jackson2ObjectMapperBuilder builder) {
        // 对 Instant 序列化时, 固定精确到秒;
        // 从 MySQL 反序列化的 Instant 只有秒级精度, Java Instant.now() 是纳秒级精度
        // 精度不同, 反序列化的字符串长度不同; 固定精确到秒, 防止预期不统一
        JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addSerializer(Instant.class, new InstantSerializerWithMilliSecondPrecision());

        // Long/long 类型转成 String, 避免前端无法识别精度
        SimpleModule sm = new SimpleModule();
        sm.addSerializer(Long.class, ToStringSerializer.instance);
        sm.addSerializer(long.class, ToStringSerializer.instance);
        sm.addSerializer(BigDecimal.class, ToStringSerializer.instance);

        // 增加 jdk 模块, 解析 Optional
        Jdk8Module jdk8Module = new Jdk8Module();

        // 返回所有模块
        builder.modules(timeModule, sm, jdk8Module);
    }

    public class InstantSerializerWithMilliSecondPrecision extends InstantSerializer {

        public InstantSerializerWithMilliSecondPrecision() {
            super(InstantSerializer.INSTANCE, false, new DateTimeFormatterBuilder().appendInstant(0).toFormatter());
        }

    }

}
