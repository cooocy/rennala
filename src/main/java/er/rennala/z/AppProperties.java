package er.rennala.z;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@ToString
@Setter
@Getter
@Component
public class AppProperties {

    @Value("${spring.application.name:}")
    private String appName;

}
