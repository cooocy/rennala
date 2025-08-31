package er.rennala.request.filters.ctx;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "rennala.filters.context")
public class ContextProperties {

    /**
     * 是否启用, 默认 true
     */
    private boolean enable = true;

    /**
     * 是否打印日志, 默认 true
     */
    private boolean log = true;

}
