package it.unibz.krdb.obda.quest;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to store the result set information. Please refer to the
 * <code>ResultSetInfoSchema</code> to look at the supported attributes.
 */
public class ResultSetInfo {

	private Map<String, Object> info = new HashMap<String, Object>();
	
	public ResultSetInfo() {
		// NO-OP
	}
	
	public ResultSetInfo(Map<String, Object> otherInfo) {
		info.putAll(otherInfo);
	}
	
	public void put(String key, Object value) {
		info.put(key, value);
	}
	
	public Object get(String key) {
		return info.get(key);
	}
}