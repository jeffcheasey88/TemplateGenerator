package be.jeffcheasey88.template.works;

import java.io.File;

public abstract class Component{
	
	public Component(){}

	public abstract String getName();
	
	public abstract void load(File dir);
}
