package net.x3pro.xbase.annotation;

import java.lang.annotation.*;

@Target(value=ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface XBaseField {
	String title() default "";
	int length() default 200;
	boolean notEmpty() default false;
	boolean hide() default false;
	boolean readOnly() default false;
	String owner() default "";
}
