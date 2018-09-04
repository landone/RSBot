package com.landone.rsbot.tasks;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Item;

import com.landone.rsbot.constants.ANIMATIONS;
import com.landone.rsbot.constants.ITEMS;
import com.landone.rsbot.constants.OBJECTS;

public class BronzeMineTask extends Task {

	public BronzeMineTask(ClientContext ctx) {
		super(ctx);
	}
	
	//Empty inv spaces
	private int empty;
	//Copper inv spaces
	private int copper;
	//Tin inv spaces
	private int tin;
	//Total inv spaces allocated for mining
	private int total;
	//Tile of last rock
	private GameObject lastRock = null;

	@Override
	public boolean activate() {
		
		for(Item i : ctx.inventory.select().id(ITEMS.UNCUT_GEMS)) {
			i.interact("Drop");
		}
		
		empty = 28 - ctx.inventory.select().count();
		copper = ctx.inventory.select().id(ITEMS.COPPER_ORE).count();
		tin = ctx.inventory.select().id(ITEMS.TIN_ORE).count();
		total = empty + copper + tin;
		total -= total % 2;//Assert 
		
		return empty > 0 && !(empty < 2 && copper == tin);
		
	}

	@Override
	public void execute() {
		
		if(ctx.players.local().animation() == ANIMATIONS.MINING) {
			//Only continue mining if rock is still there
			if(lastRock != null && ctx.objects.select().contains(lastRock)) {
				return;
			}
		}
		
		int[] target = copper < total / 2 ? OBJECTS.COPPER_ORE : OBJECTS.TIN_ORE;
		
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
		
	}

	@Override
	public void print() {
		
		System.out.println("Mining ores");
		
	}

}
