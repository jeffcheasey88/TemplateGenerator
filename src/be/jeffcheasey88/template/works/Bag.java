package be.jeffcheasey88.template.works;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Bag{
	
	private Map<String, Object> map;
	
	private Object value;
	
	private String path = "";
	
	public Bag(){
		this.map = new HashMap<>();
		path = Thread.currentThread().getStackTrace()[2].toString();
	}
	
	public Set<String> keys(){
		return map.keySet();
	}
	
	public <E> E get(){
		return (E) this.value;
	}
	
	public void addPath(String s){
		path+=s;
	}
	
	public String path(){
		return this.path;
	}
	
	public void set(Object value){
		this.value = value;
	}
	
	public <E> E get(String key){
		return (E) this.map.get(key);
	}
	
	public boolean has(String key){
		return this.map.containsKey(key);
	}
	
	public void remove(String key){
		this.map.remove(key);
	}
	
	public void set(String key, Object value){
		this.map.put(key, value);
	}
	
	@Override
	public String toString(){
		String map = "";
		for(Entry<String, Object> entry : this.map.entrySet()) map+=","+(entry.getKey())+" -> "+entry.getValue();
		if(map.length() > 0) map = map.substring(1);
		return "([bag] | value="+value+" | map["+Arrays.toString(this.map.keySet().toArray())+"]="+map+" )";
	}
}
