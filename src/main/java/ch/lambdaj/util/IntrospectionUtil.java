package ch.lambdaj.util;

public class IntrospectionUtil {

	IntrospectionUtil() {}

	public static Object getPropertyValue(Object bean, String propertyName) {
		if (bean == null) return null;
		int dotPos = propertyName.indexOf('.');
		if (dotPos > 0) return getPropertyValue(getPropertyValue(bean, propertyName.substring(0, dotPos)), propertyName.substring(dotPos+1));
		
		String accessorName = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
		Object value = null;
		try {
			value = bean.getClass().getMethod("get" + accessorName).invoke(bean, (Object[]) null);
		} catch (Exception e) {
			try {
				value = bean.getClass().getMethod("is" + accessorName).invoke(bean, (Object[]) null);
			} catch (Exception e1) {
				try {
					value = bean.getClass().getMethod(propertyName).invoke(bean, (Object[]) null);
				} catch (Exception e2) {
					throw new RuntimeException(e);
				}
			}
		}
		return value;
	}
}
