package be.jeffcheasey88.template.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class JsonArray extends Json{
	
	private List<Object> list;
	
	public JsonArray(){
		this.list = new ArrayList<>();
	}
	
	public <E> Collection<E> toList(){
		return (Collection<E>) this.list;
	}
	
	public void add(Object value){
		this.list.add(value);
	}
	
	@Override
	public String toString(){
		Iterator<Object> iterator = list.iterator();
		if(!iterator.hasNext()) return "[]";
		StringBuilder builder= new StringBuilder();
		builder.append('[');
		while(iterator.hasNext()){
			buildValue(builder, iterator.next());
			if(iterator.hasNext()) builder.append(',');
		}
		builder.append(']');
		return builder.toString();
	}

	
}
