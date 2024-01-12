package be.jeffcheasey88.template.works;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import be.jeffcheasey88.template.ArgumentReader;

public class ComponentRegistery{
	
	private Map<String, Map<String, Component>> map;
	
	public ComponentRegistery(){
		this.map = new HashMap<>();
	}
	
	public void load(File dir) throws Exception{
		for(File file : dir.listFiles()){
			if(!file.isDirectory()) continue;
			Map<String, Component> language = new HashMap<>();
			for(File child : file.listFiles()) load(language, dir, child);
			map.put(file.getName(), language);
		}
	}
	
	public void build(Bag data, ArgumentReader reader){
		String language = data.get("language");
		String template = data.get("template");
		map.get(language).get(template).build(data, reader);
	}
		
	private void load(Map<String, Component> map, File dir, File file) throws Exception{
		if(file.isDirectory()){
			for(File child : file.listFiles()) load(map, dir, child);
			return;
		}
		
		if(file.getName().endsWith(".class")){
			Class<?> clazz = new URLClassLoader(new URL[]{dir.toURI().toURL()}).loadClass(file.getAbsolutePath().substring(dir.getAbsolutePath().length()+1).replace("/", ".").replace("\\", ".").replace(".class", ""));
			if(!clazz.getSuperclass().getName().equals("be.jeffcheasey88.template.works.Component")) return;
			if(clazz.getName().equals("be.jeffcheasey88.template.works.ControllerComponent")) return;
			Component component = (Component) clazz.newInstance();
			component.load(file.getParentFile());
			map.put(component.getName(), component);
		}else if(file.getName().endsWith(".controller")){
			String content = "";
			String line;
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while((line = reader.readLine()) != null) content+=line;
			reader.close();
			Component component = new ControllerComponent(file.getName().split("\\.")[0], content);
			component.load(file.getParentFile());
			map.put(component.getName(), component);
		}
	}

}
