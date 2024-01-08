package javalanguage.clazz;

import java.io.File;

import be.jeffcheasey88.template.ArgumentReader;
import be.jeffcheasey88.template.works.Bag;
import be.jeffcheasey88.template.works.Component;

public class Clazz extends Component{
	
	private String view;

	@Override
	public String getName(){
		return "class";
	}

	@Override
	public void load(File dir) throws Exception{
		this.view = loadView(new File(dir, "clazz.component"));
	}

	@Override
	public void build(Bag data, ArgumentReader args){
		if(!checkData(data)) return;
		Bag result = new Bag();
		result.set(bind(view, data));
		build(args, result);
	}

	private boolean checkData(Bag data){
		if(!data.has("package")) return false;
		if(!data.has("name")) return false;
		if(!data.has("modifier")) data.set("modifier","");
		if(!data.has("import")) data.set("import","");
		if(!data.has("extends")) data.set("extends","");
		return true;
	}
}
