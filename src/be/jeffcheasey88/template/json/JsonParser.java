package be.jeffcheasey88.template.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import dev.peerat.parser.Parser;
import dev.peerat.parser.Token;
import dev.peerat.parser.TokenType;
import dev.peerat.parser.state.RedirectStateTree;
import dev.peerat.parser.state.StateTree;

public class JsonParser extends Parser<Json>{
	
	private static StateTree<Json> base;
	
	static{
		base = new StateTree<>();
		
		StateTree<Json> content = new StateTree<>();
		
		StateTree<Json> content_array = new StateTree<>();
		StateTree<Json> content_array_element = content_array.then(new RedirectStateTree<>(content, (global, local) ->{
			List<Object> list = global.get();
			if(list == null){
				list = new ArrayList<>();
				global.set(list);
			}
			list.add(local.get());
		}));
		content_array_element.end((parent, bag) -> {
			JsonArray json = (JsonArray)parent;
			
			List<Object> list =  bag.get();
			for(Object value : list){
				json.add(convert(value));
			}
			return null;
		});
		content_array_element.then((validator) -> validator.validate((token) -> token.getValue().equals(",")))
			.then(content_array_element);
		
		content.then(new RedirectStateTree<>(base, (global, local) -> global.set(local.get()))).end();
		content.then((validator) -> validator.validate((token) -> token.getType().equals(TokenType.STRING), (bag, token) -> bag.set(token))).end();
		content.then((validator) -> validator.validate((token) -> token.getType().equals(TokenType.CHAR), (bag, token) -> bag.set(token))).end();
		StateTree<Json> number = content.then((validator) -> validator.validate((token) -> token.getType().equals(TokenType.NAME), (bag, token) -> bag.set(token)));
		number.end();
		number.then((validator) -> validator.validate((token) -> token.getValue().equals("."), (bag, token) -> bag.set(bag.<Token>get().concat(token))))
			.then((validator) -> validator.validate((token) -> token.getType().equals(TokenType.NAME), (bag, token) -> bag.set(bag.<Token>get().concat(token))))
			.end();
		
		StateTree<Json> mapper = new StateTree<>();
		StateTree<Json> mapper_key = mapper.then((validator) -> validator.validate((token) -> token.getType().equals(TokenType.STRING), (bag, token) -> {
			Map<Token, Object> map = bag.get("map");
			if(map == null){
				map = new HashMap<>();
				bag.set("map", map);
			}
			map.put(token, null);
			bag.set("last", token);
		}));
		StateTree<Json> mapper_value = mapper_key.then((validator) -> validator.validate((token) -> token.getValue().equals(":")))
			.then(new RedirectStateTree<>(content, (global, local) -> {
				global.<Map<Token, Object>>get("map").put(global.<Token>get("last"), local.get());
			}));
		
		mapper_value.end((parent, bag) -> {
			Map<Token, Object> map = bag.get("map");
			JsonMap jMap = (JsonMap)parent;
			for(Entry<Token, Object> entry : map.entrySet()){
				jMap.set(entry.getKey().getValue(), convert(entry.getValue()));
			}
			return null;
		});
		mapper_value.then((validator) -> validator.validate((token) -> token.getValue().equals(",")))
			.then(mapper_key);
		
		
		base.then((validator) -> validator.validate((token) -> token.getValue().equals("{")))
			.<Json>end((parent, bag) -> {
				JsonMap result = new JsonMap();
				bag.set(result);
				if(parent instanceof JsonContainer) ((JsonContainer)parent).value = result;
				return result;
			})
				.unique(mapper)
				.unique((validator) -> validator.validate((token) -> token.getValue().equals("}")))
				.end();
		
		base.then((validator) -> validator.validate((token) -> token.getValue().equals("[")))
			.<Json>end((parent, bag) -> {
				JsonArray result = new JsonArray();
				bag.set(result);
				if(parent instanceof JsonContainer) ((JsonContainer)parent).value = result;
				return result;
			})
				.unique(content_array)
				.unique((validator) -> validator.validate((token) -> token.getValue().equals("]")))
				.end();
	}
	
	private static Object convert(Object value){
		if(value instanceof Token){
			Token token = (Token) value;
			String content = token.getValue();
			if(token.getType().equals(TokenType.STRING)){
				return content;
			}else if(token.getType().equals(TokenType.CHAR)){
				return content.charAt(0);
			}else{
				try{
					return Long.parseLong(content);
				}catch(Exception _){
					try {
						return  Double.parseDouble(content);
					}catch(Exception __){
						try{
							return Boolean.parseBoolean(content);
						}catch(Exception ___){}
					}
				}
			}
		}
		return value;
	}
	
	public JsonParser(){
		setTokenizer(new JsonTokenizer());
		setStateTree(base);
	}
	
	public <J extends Json> J parse(String content) throws Exception{
		JsonContainer container = new JsonContainer();
		parse(content,  container);
		return container.getValue();
	}
	
	private static class JsonContainer extends Json{
		
		private Json value;
		
		public JsonContainer(){}
		
		public <J extends Json> J getValue(){
			return (J) this.value;
		}
		
	}
}
