package be.jeffcheasey88.template;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

import be.jeffcheasey88.template.json.Json;
import be.jeffcheasey88.template.json.JsonArray;
import be.jeffcheasey88.template.json.JsonMap;
import be.jeffcheasey88.template.json.JsonParser;
import be.jeffcheasey88.template.works.Bag;
import be.jeffcheasey88.template.works.ComponentRegistery;

public class Main{
	
	static JsonParser PARSER = new JsonParser();
	
	public static void main(String[] args) throws Exception{
		ArgumentReader reader = new ArgumentReader(args);
		
		ComponentRegistery registery = new ComponentRegistery();
		registery.load(new File("bin\\"));
		
		Bag data = readData(reader);
		registery.build(data, reader);
	}
	
	private static Bag readData(ArgumentReader reader) throws Exception{
		String value = "";
		if(reader.readInput()){
			Scanner scanner = new Scanner(System.in);
			while(scanner.hasNext()) value+=scanner.nextLine();
			scanner.close();
		}else if(reader.readFile() != null){
			BufferedReader fileReader = new BufferedReader(new FileReader(reader.readFile()));
			String line;
			while((line = fileReader.readLine()) != null) value+=line;
			fileReader.close();
		}else{
			value = reader.getData();
		}
		Json json = PARSER.parse(value);
		if(json == null) throw new NullPointerException();
		Bag result = new Bag();
		buildBag(result, json);
		return result;
	}

	private static void buildBag(Bag bag, Json json){
		if(json instanceof JsonMap){
			JsonMap jMap = (JsonMap)json;
			for(String key : jMap.keys()){
				Object jValue = jMap.get(key);
				if(jValue instanceof Json){
					Bag sub = new Bag();
					buildBag(sub, (Json)jValue);
					bag.set(key, sub);
				}else{
					bag.set(key, jValue);
				}
			}
		}else{
			bag.set(((JsonArray)json).toList());
		}
	}
}
