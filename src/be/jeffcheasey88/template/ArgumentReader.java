package be.jeffcheasey88.template;

import java.io.File;

public class ArgumentReader{
	
	/*
	 * 
	 *  -in			| read input
	 *  -f <file> 	| read file
	 *  -out <file> | write file
	 *  -out		| write output
	 * 
	 */
	private boolean readInput;
	private File readFile;
	private File writeFile;
	private boolean writeOutput;
	private String data = "";
	
	public ArgumentReader(String[] args){
		for(int i = 0; i < args.length; i++){
			String s = args[i];
			if(s.equals("-in")){
				readInput = true;
			}else if(s.equals("-f")){
				if(i == args.length-1) throw new IllegalArgumentException("no");
				readFile = new File(args[++i]);
			}else if(s.equals("-out")){
				if(i == args.length-1){
					writeOutput = true;
					continue;
				}
				writeFile = new File(args[++i]);
			}else data+=s;
		}
	}
	
	public boolean readInput(){
		return this.readInput;
	}
	
	public File readFile(){
		return this.readFile;
	}
	
	public File writeFile(){
		return this.writeFile;
	}
	
	public boolean writeOutput(){
		return this.writeOutput;
	}
	
	public String getData(){
		return this.data;
	}
}
