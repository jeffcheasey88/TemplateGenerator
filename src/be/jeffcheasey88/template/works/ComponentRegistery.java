package be.jeffcheasey88.template.works;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class ComponentRegistery{
	
	private Map<String, Map<String, Component>> map;
	
	public ComponentRegistery(){
		this.map = new HashMap<>();
	}
	
	public void load(File dir) throws Exception{
		for(File file : dir.listFiles()){
			if(!file.isDirectory()) continue;
			Map<String, Component> language = new HashMap<>();
			for(File child : file.listFiles()) load(language, child, child);
			map.put(file.getName(), language);
		}
	}
		
	private void load(Map<String, Component> map, File dir, File file) throws Exception{
		if(file.isDirectory()){
			for(File child : file.listFiles()) load(map, dir, child);
			return;
		}
		
		if(file.getName().endsWith(".class")){
			Class<?> clazz = new URLClassLoader(new URL[]{dir.toURI().toURL()}).loadClass(file.getAbsolutePath().substring(dir.getAbsolutePath().length()).replace("/", ".").replace("\\", ".").replace(".class", ""));
			Component component = (Component) clazz.newInstance();
			component.load(file.getParentFile());
			map.put(component.getName(), component);
		}
	}

}
