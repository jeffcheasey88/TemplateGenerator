package be.jeffcheasey88.template.json;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class JsonMap extends Json{
	
	private Map<String, Object> map;
	
	public JsonMap(){
		this.map = new HashMap<>();
	}
	
	public Set<Entry<String, Object>> entries(){
		return this.map.entrySet();
	}
	
	public Set<String> keys(){
		return this.map.keySet();
	}
	
	public void set(String key, Object value){
		this.map.put(key, value);
	}
	
	public boolean has(String key){
		return map.containsKey(key);
	}
	
	public <E> E get(String key){
		return (E) map.get(key);
	}
	
	@Override
	public String toString(){
		Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
		if(!iterator.hasNext()) return "{}";
		StringBuilder builder= new StringBuilder();
		builder.append('{');
		while(iterator.hasNext()){
			Entry<String, Object> entry = iterator.next();
			builder.append('"');
			builder.append(entry.getKey());
			builder.append("\":");
			
			Object value = entry.getValue();
			buildValue(builder, value);
			
			if(iterator.hasNext()) builder.append(',');
		}
		builder.append('}');
		return builder.toString();
	}
}
