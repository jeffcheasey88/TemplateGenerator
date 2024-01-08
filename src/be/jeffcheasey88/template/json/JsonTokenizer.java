package be.jeffcheasey88.template.json;

import dev.peerat.parser.Token;
import dev.peerat.parser.TokenType;
import dev.peerat.parser.Tokenizer;

public class JsonTokenizer extends Tokenizer{

	@Override
	public void parse(String line){
		for(int i = 0; i < line.length(); i++){
			char c = line.charAt(i);
			
			if(isValidName(c)){
				String buffer =  ""+c;
				int j = i+1;
				for(; j < line.length(); j++){
					c = line.charAt(j);
					if(isValidName(c)) buffer+=c;
					else break;
				}
				getTokens().add(new Token(1, i+1, buffer, TokenType.NAME));
				i=j-1;
			}else if(Character.isWhitespace(c)) continue;
			else{
				if(c == '"'){
					String buffer = "";
					int j = i+1;
					for(; j < line.length(); j++){
						c = line.charAt(j);
						if(c == '\\'){
							buffer+=c+line.charAt(++j);
						}
						if(c == '\"') break;
						buffer+=c;
					}
					getTokens().add(new Token(1, i+1, buffer, TokenType.STRING));
					i=j;
					continue;
				}
				if(c == '\''){
					String buffer = "";
					int j = i+1;
					for(; j < line.length(); j++){
						c = line.charAt(j);
						if(c == '\\'){
							buffer+=c+line.charAt(++j);
						}
						if(c == '\'') break;
						buffer+=c;
					}
					getTokens().add(new Token(1, i+1, buffer, TokenType.CHAR));
					i=j;
					continue;
				}
				getTokens().add(new Token(1, i+1, ""+c, TokenType.DELIMITER));
			}
		}
	}
	
	private boolean isValidName(char c){
		return c == '_' || c == '-' || c == '$' || Character.isAlphabetic(c) || Character.isDigit(c);
	}
	
}
