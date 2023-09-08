package er.rennala.advice.ct;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.Set;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "auth.uri")
public class CheckedUris {

    @Nullable
    private Set<String> black;

    @Nullable
    private Set<String> white;

    /**
     * black/white
     */
    private String mode;

}
