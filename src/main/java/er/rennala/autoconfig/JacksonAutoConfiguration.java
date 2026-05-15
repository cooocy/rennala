package er.rennala.autoconfig;

import er.rennala.jackson.JacksonCustomizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class JacksonAutoConfiguration {

    @Bean
    public JacksonCustomizer jacksonCustomizer() {
        return new JacksonCustomizer();
    }

}
