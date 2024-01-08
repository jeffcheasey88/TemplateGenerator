package be.jeffcheasey88.template.works;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import be.jeffcheasey88.template.ArgumentReader;

public abstract class Component{
	
	public Component(){}

	public abstract String getName();
	
	public abstract void load(File dir) throws Exception;
	
	public abstract void build(Bag data, ArgumentReader args);
	
	public void build(ArgumentReader args, Bag output){
		if(args.writeOutput()){
			System.out.println(output.<String>get());
			for(String key : output.keys()) System.out.println(output.<String>get(key));
			System.out.println();
		}else{
			File dir = args.writeFile();
			if(output.get() != null){
				try {
					BufferedWriter writer = new BufferedWriter(new FileWriter(dir));
					writer.write(output.<String>get());
					writer.flush();
					writer.close();
				} catch (IOException e){
					e.printStackTrace();
				}
			}else{
				for(String key : output.keys()) try{
					File file = new File(dir, key);
					File parent = file.getParentFile();
					if(!parent.exists()) parent.mkdirs();
					BufferedWriter writer = new BufferedWriter(new FileWriter(file));
					writer.write(output.<String>get(key));
					writer.flush();
					writer.close();
				} catch (IOException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public String bind(String view, Bag data){
		String result = view;
		for(String key : data.keys()) result = result.replace("%"+key+"%", data.get(key));
		return result;
	}
	
	public String loadView(File file) throws Exception{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;
		String result = "";
		while((line = reader.readLine()) != null) result+=line+"\n";
		reader.close();
		return result;
	}
}