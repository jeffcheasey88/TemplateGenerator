package be.jeffcheasey88.template.json;

public class Json{
	
	void buildValue(StringBuilder builder, Object value){
		if(value == null) builder.append("null");
		else if(value instanceof  String){
			builder.append('"');
			for(char c : ((String)value).toCharArray()){
				if(c == '"'){
					builder.append("\\\"");
					continue;
				}
				if(c == '\\'){
					builder.append("\\\\");
					continue;
				}
				if(c == '\n'){
					builder.append("\\n");
					continue;
				}
				if(c == '\b'){
					builder.append("\\b");
					continue;
				}
				if(c == '\f'){
					builder.append("\\f");
					continue;
				}
				if(c == '\r'){
					builder.append("\\r");
					continue;
				}
				if(c == '\t'){
					builder.append("\\t");
					continue;
				}
				builder.append(c);
			}
			builder.append('"');
		}else if(value instanceof Character){
			builder.append('\'');
			builder.append(value);
			builder.append('\'');
		}else{
			builder.append(value);
		}
	}
	
}
