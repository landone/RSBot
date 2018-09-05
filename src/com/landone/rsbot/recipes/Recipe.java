package com.landone.rsbot.recipes;

import java.util.ArrayList;

public class Recipe {
	
	private int totalSize = 0;
	
	private class Ingredient{
		public int[] ids;
		public int amt;
		public int item;
		Ingredient(int[] ids, int amt, int item){
			this.ids = ids;
			this.amt = amt;
			this.item = item;
		}
	}
	
	private ArrayList<Ingredient> ingreds;
	
	public Recipe(){
		
		ingreds = new ArrayList<Ingredient>();
		
	}
	
	public int addIngredient(int[] ids, int amt, int itemID) {
		
		Ingredient ingred = new Ingredient(ids, amt, itemID);
		ingreds.add(ingred);
		totalSize += ingred.amt;
		return ingreds.size() - 1;
		
	}
	
	public boolean removeIngredient(int index) {
		
		if(index < 0 || index >= ingreds.size()) {
			return false;
		}
		
		Ingredient ingred = ingreds.get(index);
		totalSize -= ingred.amt;
		ingreds.remove(index);
		return true;
		
	}
	
	public int getIngredItemID(int index) {
		
		if(index < 0 || index >= ingreds.size()) {
			return 0;
		}
		
		return ingreds.get(index).item;
		
	}
	
	public int getIngredAmount(int index) {
		
		if(index < 0 || index >= ingreds.size()) {
			return 0;
		}
		
		return ingreds.get(index).amt;
		
	}
	
	public int[] getIngredIDs(int index) {
		
		if(index < 0 || index >= ingreds.size()) {
			return new int[0];
		}
		
		return ingreds.get(index).ids;
		
	}
	
	public int getIngredSize() {
		
		return ingreds.size();
		
	}
	
	public int getSize() {
		
		return totalSize;
		
	}
	
}
