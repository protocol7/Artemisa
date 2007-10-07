package com.protocol7.artemisa;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface XpathProperty {
    public class BeanClassDefault{};
    
    String xpath() default "";
    Class<?> beanClass() default BeanClassDefault.class;
}
