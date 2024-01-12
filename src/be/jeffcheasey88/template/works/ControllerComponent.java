package be.jeffcheasey88.template.works;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import be.jeffcheasey88.template.ArgumentReader;
import dev.peerat.parser.Parser;

public class ControllerComponent extends Component{
	
	static Parser<Controller> PARSER = new ControllerParser();

	private String name;
	private Controller controller;
	
	private Map<String, String> views;
	
	public ControllerComponent(String name, String controller) throws Exception{
		this.name = name;
		
		this.controller = new Controller();
		PARSER.parse(controller, this.controller);
		
		this.views = new HashMap<>();
	}
	
	@Override
	public String getName(){
		return name;
	}

	@Override
	public void load(File dir) throws Exception{
		for(Entry<String, String> view : controller.getViews()) views.put(view.getKey(), loadView(new File(dir, view.getValue())));
	}

	@Override
	public void build(Bag data, ArgumentReader args){
		if(!controller.checkData(data)) return;
		Bag result = new Bag();
		for(Entry<String, String> output : controller.getOutput()) result.set(output.getKey(), bind(this.views.get(output.getValue()), data));
		build(args, result);
	}

}
