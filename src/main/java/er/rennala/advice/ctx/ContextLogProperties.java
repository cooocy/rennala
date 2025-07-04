package er.rennala.advice.ctx;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "rennala.context-log")
public class ContextLogProperties {

    /**
     * 是否打印日志, 默认 true
     */
    private boolean enable = true;

}
