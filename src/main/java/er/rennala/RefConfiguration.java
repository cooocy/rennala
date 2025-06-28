package er.rennala;

import er.rennala.advice.domain.ref.RefProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@Import({RefProcessor.class})
@AutoConfiguration
public class RefConfiguration {

}
