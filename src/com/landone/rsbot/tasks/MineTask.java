package com.landone.rsbot.tasks;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Item;

import com.landone.rsbot.constants.ANIMATIONS;
import com.landone.rsbot.constants.ITEMS;
import com.landone.rsbot.recipes.Recipe;

public class MineTask extends Task {
	
	//Empty inv spaces
	private int empty;
	//Total inv spaces allocated for mining
	private int total;
	//Tile of last rock
	private GameObject lastRock = null;
	//Recipe of what we want to mine
	private Recipe recipe;
	//Index of ore being mined
	private int oreIndex;

	public MineTask(ClientContext ctx, Recipe recipe) {
		
		super(ctx);
		this.recipe = recipe;
		oreIndex = 0;
		
	}
	
	private void updateTotal() {
		
		empty = 28 - ctx.inventory.select().count();
		total = empty;
		for(int i=0;i<recipe.getIngredSize();i++) {
			total += getIngredInvCount(i);
		}
		total -= total % recipe.getSize();
		
	}
	
	private int getIngredInvCount(int index) {
		
		int id = recipe.getIngredItemID(index);
		return ctx.inventory.select().id(id).count();
		
	}

	@Override
	public boolean activate() {
		
		for(Item i : ctx.inventory.select().id(ITEMS.UNCUT_GEMS)) {
			i.interact("Drop");
		}
		
		updateTotal();
		
		return empty > 0 && !(empty < recipe.getSize() && (total - empty) % recipe.getSize() == 0);
		
	}

	@Override
	public void execute() {
		
		if(ctx.players.local().animation() == ANIMATIONS.MINING) {
			//Only continue mining if rock is still there
			if(lastRock != null && ctx.objects.select().contains(lastRock)) {
				return;
			}
		}
		
		int[] target = getIngredInvCount(oreIndex) < (total / recipe.getSize())*recipe.getIngredAmount(oreIndex) ? 
				recipe.getIngredIDs(oreIndex) : recipe.getIngredIDs(++oreIndex);
		
		GameObject rock = ctx.objects.select().id(target).nearest().poll();
		
		if(rock == null) {
			System.out.println("NULL rock GameObject");
			return;
		}
		
		lastRock = rock;
		
		if(!rock.inViewport()) {
			ctx.movement.step(rock);
			ctx.camera.turnTo(rock);
		}else {
			rock.interact("Mine");
		}

	}

	@Override
	public void reset() {
		
		lastRock = null;
		oreIndex = 0;
		
	}

	@Override
	public void print() {
		
		System.out.println("Mining ores");
		
	}

}
