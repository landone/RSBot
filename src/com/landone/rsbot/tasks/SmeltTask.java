package com.landone.rsbot.tasks;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import com.landone.rsbot.constants.ANIMATIONS;
import com.landone.rsbot.constants.ITEMS;
import com.landone.rsbot.constants.OBJECTS;

public class SmeltTask extends Task{
	
	private long lastSmelt = 0;

	public SmeltTask(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		
		int copper = ctx.inventory.select().id(ITEMS.COPPER_ORE).count();
		int tin = ctx.inventory.select().id(ITEMS.TIN_ORE).count();
		return copper >= 1 && tin >= 1;
	}

	@Override
	public void execute() {
		
		if(ctx.players.local().animation() == ANIMATIONS.SMELTING) {
			lastSmelt = System.currentTimeMillis();
			return;//Don't interrupt smelting
		}
		
		if(System.currentTimeMillis() - lastSmelt < 3000) {
			return;//Allow 3 seconds before concluding a stand-still
		}
		
		if(ctx.widgets.widget(270).valid()) {
			ctx.widgets.component(270, 12).click();//All button
			ctx.widgets.component(270, 14).click();//Bronze button
			return;
		}
		
		GameObject furn = ctx.objects.select().id(OBJECTS.FURNACE).nearest().poll();
		
		if(furn == null) {
			System.out.println("NULL furnace");
			return;
		}
		
		if(!furn.inViewport()) {
			ctx.movement.step(furn);
			ctx.camera.turnTo(furn);
		}else {
			furn.interact("Smelt");
		}
		
	}

	@Override
	public void reset() {
		
		lastSmelt = 0;
		
	}

	@Override
	public void print() {
		
		System.out.println("Smelting ores");
		
	}

}
