package be.jeffcheasey88.template;

import java.io.File;

import be.jeffcheasey88.template.works.ComponentRegistery;

public class Main{
	
	public static void main(String[] args) throws Exception{
		ArgumentReader reader = new ArgumentReader(args);
		
		ComponentRegistery registery = new ComponentRegistery();
		registery.load(new File("bin\\"));
	}

}
