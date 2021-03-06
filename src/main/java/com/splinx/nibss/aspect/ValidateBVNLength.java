package com.splinx.nibss.aspect;


import java.lang.annotation.*;

@Target({ElementType.TYPE ,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ValidateBVNLength {

    Class<?> type();
}
