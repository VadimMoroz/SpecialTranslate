package net.x3pro.xbase.annotation;

import java.lang.annotation.*;

@Target(value=ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface XBaseObject {
	String title() default "";
	String buttonOkTitle() default "";
	String buttonCancelTitle() default "";
	String buttonOkValue() default "";
	String buttonCancelValue() default "";
}
