package er.rennala.advice.domain.ref;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Ref {

    Class<? extends RefAnalyzer> analyzer();

    String refField();

    String analysisTo();

}
