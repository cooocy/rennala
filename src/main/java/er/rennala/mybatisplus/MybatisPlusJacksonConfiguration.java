package er.rennala.mybatisplus;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusJacksonConfiguration implements CommandLineRunner {

    private final ObjectMapper objectMapper;

    @Autowired
    public MybatisPlusJacksonConfiguration(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) {
        // mybatis-plus 默认自己 new 一个新的 ObjectMapper
        // 在这里配置让它使用 spring-boot 中配置的 ObjectMapper Bean
        JacksonTypeHandler.setObjectMapper(objectMapper);
    }

}
