package com.touna.annotations;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.touna.common.BeanAccessor;
import com.touna.common.CommonUtils;
import com.touna.common.ReflectUtils;


/**
 * 输出格式化工具类
 * 
 * @author wuqq
 *
 */
public class OutputFormatter {

	private static ConcurrentMap<Class<?>, List<FieldFormater>> formatContainer = new ConcurrentHashMap<Class<?>, List<FieldFormater>>() ;
	
	
	
	/**
	 * 按照AutoMapKey, MapKey的规则, 将javabean转化为Map, 
	 * @param bean 不允许非javabean
	 * @return
	 */
	public static Map<String, Object> bean2Map(Object bean) {
		Class<?> klass = bean.getClass() ;
		List<FieldFormater> list = formatContainer.get(klass) ;
		if (list == null) {
			list = foundFormaters(klass) ;
			List<FieldFormater> absent = formatContainer.putIfAbsent(klass, list) ;
			list = absent == null ? list : absent ;
		}
		
		Map<String, Object> result = CommonUtils.stableMap(list.size()) ;
		for(FieldFormater formater : list)
		{
			Object value = BeanAccessor.getValue(bean, formater.field) ;
			if(value!=null) result.put(formater.keyName, value) ;
		}
		return result ;
	}
	
	
	
	
	
	private static class FieldFormater {
		Field field ;
		String keyName ;
	}
	
	
	
	
	private static List<FieldFormater> foundFormaters(Class<?> klass) {
		List<Field> fields = foundKeyFields(klass) ;
		List<FieldFormater> list = new ArrayList<FieldFormater>(fields.size()) ;
		boolean isClassAutoMapKey = klass.isAnnotationPresent(AutoMapKey.class) ;
		for(Field e : fields)
		{
			if(e.isAnnotationPresent(UnMapKey.class)) continue ;
			
			FieldFormater formatter = new FieldFormater() ;
			
			if(isClassAutoMapKey || e.isAnnotationPresent(AutoMapKey.class)) 
			{
				formatter.keyName = formatHump(e.getName()) ;
			}
			else if(e.isAnnotationPresent(MapKey.class)) {
				MapKey a = e.getAnnotation(MapKey.class) ;
				formatter.keyName = a.key() ;
			}
			
			if(CommonUtils.isEmpty(formatter.keyName))
			{
				formatter.keyName = e.getName() ;
			}
			formatter.field = e ;
			list.add(formatter) ;
		}
		return list ;
	}
	
	
	
	private static List<Field> foundKeyFields(Class<?> klass) {
		Map<String, Field> fields = ReflectUtils.getFields(klass) ;
		List<Field> list = new ArrayList<Field>(fields.size()) ;
		if(klass.isAnnotationPresent(AutoMapKey.class))
		{
			list.addAll(fields.values()) ;
		}
		else
		{
			for(Field field : fields.values()) {
				if(field.isAnnotationPresent(AutoMapKey.class) || field.isAnnotationPresent(MapKey.class)) {
					list.add(field) ;
				}
			}
		}
		return list ;
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	private static String formatHump(String input) {
		String[] array = splitUpcase(input) ;
		StringBuilder builder = new StringBuilder() ;
		for(int index=0; index<array.length; index++)
		{
			if(index==0) builder.append(array[index].toLowerCase()) ;
			else { builder.append("_").append(array[index].toLowerCase()) ; }
		}
		return builder.toString() ;
	}
	
	private static String[] splitUpcase(String input) {
		char[] array = input.toCharArray() ;
		StringBuilder builder = new StringBuilder();
        int start = 0, end = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] < 'a' && i != 0)
            {
                start = end; end = i;
                builder.append(input.substring(start,end) + " ");
            }
        }
	    builder.append(input.substring(end, array.length));
	    return builder.toString().split(" ") ;
	}
	
	
	public static void main(String[] args) {
		System.out.println(Arrays.toString(splitUpcase("HelloWorldILoveYou")));
	}
}
