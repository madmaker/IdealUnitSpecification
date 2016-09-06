package ru.idealplm.utils.specification;

import java.util.ArrayList;

public class BLKits {
	
	private ArrayList<String> ids;
	private ArrayList<String> names;
	private ArrayList<Integer> qtys;

	public BLKits(){
		ids = new ArrayList<String>();
		names = new ArrayList<String>();
		qtys = new ArrayList<Integer>();
	}
	
	public void addKit(String id, String name, int qty){
		int pos = ids.indexOf(id);
		if(pos!=-1){
			System.out.println("UPDATING KIT");
			qtys.set(pos, qtys.get(pos)+qty); 
		} else {
			System.out.println("NEW KIT");
			ids.add(id);
			names.add(name);
			qtys.add(qty);
		}
	}
	
	public void addKits(BLKits kits){
		if(kits!=null){
			for(int i = 0; i < kits.ids.size(); i++){
				addKit(kits.ids.get(i), kits.names.get(i), kits.qtys.get(i));
			}
		}
	}
	
	public ArrayList<String> getKits(){
		ArrayList<String> result = new ArrayList<String>();
		if(ids.size()==1 && qtys.get(0)==1){
			result.add("из компл. " + names.get(0));
			return result;
		}
		for(int i = 0; i < ids.size(); i++){
			result.add(qtys.get(i)+" шт. из компл. " + names.get(i));
			System.out.println(qtys.get(i)+" шт. из компл. " + names.get(i));
		}
		return result;
	}
}
