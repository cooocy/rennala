package er.rennala.health;

import cn.hutool.core.util.StrUtil;
import er.carian.response.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping
public class HealthController {

    @Value("${spring.application.name}")
    private String appName;

    private final HealthProperties healthProperties;

    @Autowired
    public HealthController(HealthProperties healthProperties) {
        this.healthProperties = healthProperties;
    }

    @GetMapping("/")
    public R<Map<String, Object>> index() {
        HashMap<String, Object> m = new HashMap<>();
        m.put("app", appName);
        m.put("ts", Instant.now());
        Map<String, String> property2Value = new HashMap<>();
        m.put("properties", property2Value);
        if (Objects.nonNull(healthProperties.getProperties())) {
            healthProperties.getProperties().forEach(p -> {
                String v = System.getProperty(p);
                if (StrUtil.isEmpty(v)) {
                    v = "";
                }
                property2Value.put(p, v);
            });
        }
        return R.ok(m);
    }

}
