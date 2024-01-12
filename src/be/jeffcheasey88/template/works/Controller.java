package be.jeffcheasey88.template.works;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import be.jeffcheasey88.template.ArgumentReader;

public class Controller{
	
	private Map<String, String> views;
	private List<Function<Bag, Boolean>> checkers;
	private Map<String, String> builders;
	
	public Controller(){
		this.views = new HashMap<>();
		this.checkers = new ArrayList<>();
		this.builders = new HashMap<>();
	}
	
	Controller load(String file, String name){
		if(name == null) name = "#"+System.currentTimeMillis();
		this.views.put(name, file);
		return this;
	}
	
	Controller addChecker(String type, boolean negate, String name, String value, String op){
		if(type.equals("if")){
			if(negate){
				this.checkers.add((data) -> {
					return data.has(name);
				});
			}else{
				this.checkers.add((data) -> {
					return !data.has(name);
				});
			}
		}else if(type.equals("=")){
			this.checkers.add((data) -> {
				Object result = data.get(value);
				if(op != null){
					try {
						Method method = result.getClass().getDeclaredMethod(op);
						result = method.invoke(result);
					} catch (Exception e){
						e.printStackTrace();
						return false;
					}
				}
				data.set(name, result);
				return true;
			});
		}
		return this;
	}
	
	Controller build(String view, String output){
		this.builders.put(output, view);
		return this;
	}
	
	public Set<Entry<String, String>> getViews(){
		return this.views.entrySet();
	}

	public boolean checkData(Bag data){
		for(Function<Bag, Boolean> check : this.checkers) if(!check.apply(data)) return false;
		return true;
	}
	
	public Set<Entry<String, String>> getOutput(){
		return this.builders.entrySet();
	}
}
