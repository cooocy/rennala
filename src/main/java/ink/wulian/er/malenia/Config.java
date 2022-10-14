package ink.wulian.er.malenia;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({HealthController.class, GlobalExceptionHandler.class})
@Configuration
public class Config {

}
