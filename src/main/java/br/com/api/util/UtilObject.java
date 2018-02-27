package br.com.api.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class UtilObject {
	
	/**
	 * Retorna uma nova instancia do objeto
	 * @param arg
	 * @param fields
	 * @return
	 */
	public static Object copiar(Object arg, String... fields) {
		Object newInstance = null;
		
		try {
			newInstance = arg.getClass().newInstance();

			BeanInfo beaninfo = Introspector.getBeanInfo(arg.getClass());

			PropertyDescriptor propertyDescriptors[] = beaninfo.getPropertyDescriptors();

			List<String> listFields = Arrays.asList(fields);
			
			for (PropertyDescriptor pd : propertyDescriptors) {
				
				if (!listFields.isEmpty()) {
					if (!listFields.contains(pd.getName())) continue;
				}
				
				Method setterMethod = pd.getWriteMethod();
				Method readMethod = pd.getReadMethod();
				
				if(readMethod == null || setterMethod == null) continue;

				Object value = readMethod.invoke(arg);
				if (value ==null) continue; 

				setterMethod.invoke(newInstance, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return newInstance;
	}
	
	/**
	 * Chama os seters de argSeter com o valor dos geters de argGeter
	 * @param arg
	 * @param fields
	 * @return
	 */
	public static void copiar(Object argSeter, Object argGeter, String... fields) {
		if (!argSeter.getClass().equals(argGeter.getClass())) {
			return;
		}
		
		try {
			BeanInfo beaninfo = Introspector.getBeanInfo(argSeter.getClass());
			
			PropertyDescriptor propertyDescriptors[] = beaninfo.getPropertyDescriptors();
			
			List<String> listFields = Arrays.asList(fields);
			
			for (PropertyDescriptor pd : propertyDescriptors) {
				
				if (!listFields.isEmpty()) {
					if (!listFields.contains(pd.getName())) continue;
				}
				
				Method setterMethod = pd.getWriteMethod();
				Method readMethod = pd.getReadMethod();
				
				if(readMethod == null || setterMethod == null) continue;
				
				Object value = readMethod.invoke(argGeter);
				if (value ==null) continue; 
				
				setterMethod.invoke(argSeter, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
