package ch.lambdaj;

import static ch.lambdaj.Lambda.*;
import static ch.lambdaj.Matchers.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.*;

import ch.lambdaj.util.*;
import static java.util.Arrays.*;

public class Groups {
	public static final Object group(Iterable iterable, List<String> tokens) {
		Iterator<String> iterator = tokens.iterator();
		if (!iterator.hasNext()) return iterable;
		String criteria = iterator.next();
		String op = iterator.next();
		if (op.equals("as")) {
			Object children;
			if(tokens.size() > 2) children = group(iterable, tokens.subList(2, tokens.size()));
			else children = iterable;
			Map map = new HashMap();
			map.put(criteria, children);
			return map;
		}

		if (op.equals("by")) {
			List<Map> listOfSelected = new LinkedList<Map>();
			for (Object value : criteriavalues(criteria, iterable)) {
				Collection selected = select(from(iterable), where(criteria, is(equalTo(value))));
				Map selectedmap = new HashMap();
				selectedmap.put("value", value);
				selectedmap.put("selected", selected);
				listOfSelected.add(selectedmap);
			}

			List result = new LinkedList();
			for (Map selectedmap : listOfSelected) {
				Collection selected = (Collection) selectedmap.get("selected");
				Object value = selectedmap.get("value");
				Object children;
				if(tokens.size() > 2) children = group(selected, tokens.subList(2, tokens.size()));
				else children = iterable;
				if (children instanceof Map) {
					String childrenkey = (String) ((Map) children).keySet().iterator().next();
					Object childrenvalue = ((Map) children).get(childrenkey);
					Map map = new HashMap();
					map.put(criteria, value);
					map.put(childrenkey, childrenvalue);
					result.add(map);
				} else {
					Map map = new HashMap();
					map.put(criteria, value);
					map.put("children", children);
					result.add(map);
				}
			}
			return result;
		}

		throw new RuntimeException("unknown operation " + op);
	}

	public static final Collection criteriavalues(String criteria, Iterable iterable) {
		Collection criteriavalues = new LinkedList();
		for (Object object : iterable) {
			try {
				criteriavalues.add(IntrospectionUtil.getPropertyValue(object, criteria));
			} catch (Exception e) {

			}
		}
		return criteriavalues;
	}
}