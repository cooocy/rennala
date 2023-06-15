package er.rennala;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@Import({HealthController.class, GlobalExceptionHandler.class})
@AutoConfiguration
public class Config {

}
