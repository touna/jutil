package com.touna.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 1. 将Class、Field的名字转为小写
 * 2. 根据驼峰规则,增加下划线进行分割 
 * @author wuqq
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface AutoMapKey {

}
