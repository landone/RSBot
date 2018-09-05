package com.landone.rsbot;
import java.util.ArrayList;

import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;
import org.powerbot.script.rt4.Prayer.Effect;

import com.landone.rsbot.constants.ITEMS;
import com.landone.rsbot.constants.OBJECTS;
import com.landone.rsbot.constants.PATHS;
import com.landone.rsbot.recipes.Recipe;
import com.landone.rsbot.tasks.*;

@Script.Manifest(name = "BotTest", description="Testing bot stuff")
public class BotMain extends PollingScript<ClientContext> {
	
	private ArrayList<Task> tasks = new ArrayList<Task>();
	private int currentTask = 0;
	private long nextRandom = 0;
	
	private Recipe bronzeMining = new Recipe();
	
	@Override
	public void start() {
		
		bronzeMining.addIngredient(OBJECTS.COPPER_ORE, 1, ITEMS.COPPER_ORE);
		bronzeMining.addIngredient(OBJECTS.TIN_ORE, 1, ITEMS.TIN_ORE);
		
		ctx.camera.pitch(true);
		tasks.add(new MineTask(ctx, bronzeMining));
		tasks.add(new WalkTask(ctx, PATHS.LumbCenter_LumbMine, false));
		tasks.add(new WalkTask(ctx, PATHS.LumbCenter_LumbFurnace, true));
		tasks.add(new SmeltTask(ctx));
		tasks.add(new WalkTask(ctx, PATHS.LumbCenter_LumbFurnace, false));
		tasks.add(new WalkTask(ctx, PATHS.LumbCenter_LumbBank, true));
		tasks.add(new BankTask(ctx));
		tasks.add(new WalkTask(ctx, PATHS.LumbCenter_LumbBank, false));
		tasks.add(new WalkTask(ctx, PATHS.LumbCenter_LumbMine, true));
		//tasks.add(new CowAttackTask(ctx));
		
	}

	@Override
	public void poll() {
		
		randomAction();
		
		Task t = tasks.get(currentTask);
		if(t.activate()) {
			t.execute();
		}else {
			currentTask++;
			if(currentTask >= tasks.size()) {
				currentTask = currentTask % tasks.size();
			}
			Task next = tasks.get(currentTask);
			next.reset();
			next.print();
		}
		
	}
	
	@Override
	public void stop() {
		
		tasks.clear();
		
	}
	
	/**
	 * Hopefully keeps us from being banned!
	 */
	private void randomAction() {
		
		if(System.currentTimeMillis() >= nextRandom) {//Time to do something random!
			
			switch((int)(Math.random()*5.0)) {
			case 0://Rotate camera
				ctx.camera.angle((int)(Math.random()*180));
				break;
			case 1://Toggle running
				ctx.movement.running(!ctx.movement.running());
				break;
			case 2://Check skill menu
				ctx.skills.experiences();
				break;
			case 3://Examine an NPC if possible
				Npc npc = ctx.npcs.select().nearest().poll();
				if(npc != null) {
					ctx.camera.turnTo(npc);
					npc.interact("Examine");
				}
				break;
			case 4:
				ctx.prayer.prayer(Effect.THICK_SKIN, true);
				break;
			}
			
			nextRandom = System.currentTimeMillis() + 21000 + (int)(Math.random()*30.0)*1000;//21 to 50 seconds later
			
		}
		
	}
	
}
