package com.touna.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解在Bean上, 则会用来作为bean的key, 注解在Filed上则会作为field的key
 * 
 * 暂时性方案：用于描述javabean转换为Map时的key
 * 长远规划：可能会通过xml来替代
 * 
 * @author wuqq
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface MapKey {
	
	/**
	 * 
	 * @return
	 */
	String key() ;
	
}
