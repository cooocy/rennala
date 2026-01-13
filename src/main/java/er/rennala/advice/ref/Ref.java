package er.rennala.advice.ref;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Ref {

    Class<? extends Dereferencer> dereferencer();

    String refField();

    String dereferenceTo();

}
