package er.rennala.autoconfig;

import er.rennala.advice.ref.RefProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@Import({RefProcessor.class})
@AutoConfiguration
public class RefAutoConfiguration {

}
