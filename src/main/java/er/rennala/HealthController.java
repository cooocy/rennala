package er.rennala;

import er.carian.response.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping
public class HealthController {

    @Value("${spring.application.name}")
    private String appName;

    @GetMapping("/")
    public R<Map<String, Object>> index() {
        Map<String, Object> m = Map.of("server", appName, "ts", Instant.now());
        return R.ok(m);
    }

}
