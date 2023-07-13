package scalibrary.Annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Docs {
    public String route() default "/";

    public String method() default "get";

    public String input() default "{}";

    public String output() default "{}";

    public String description() default "";
}
