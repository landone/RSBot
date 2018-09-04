package com.landone.rsbot.tasks;

import org.powerbot.script.rt4.BasicQuery;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Combat.Style;
import org.powerbot.script.rt4.Npc;

import com.landone.rsbot.constants.NPCS;

public class CowAttackTask extends Task{

	public CowAttackTask(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		
		return true;
		
	}

	@Override
	public void reset() {
		
		
		
	}

	@Override
	public void execute() {
		
		if(ctx.players.local().interacting().valid()) {
			return;//Don't interrupt fighting
		}
		
		setStyle();
		
		BasicQuery<Npc> query = ctx.npcs.select().id(NPCS.COWS).nearest();
		Npc cow;
		while((cow = query.poll()) != null) {
			
			if(cow.interacting().valid()) {
				continue;
			}
			
			cow.interact("Attack");
			break;
			
		}
		
	}
	
	private void setStyle() {
		
		int lowest = -1;
		Style s = Style.ACCURATE;
		for(int i=0;i<3;i++) {
			int temp = ctx.skills.level(i);
			if(lowest == -1 || temp < lowest) {
				lowest = temp;
				s = (i == 0 ? Style.ACCURATE : (i == 1 ? Style.AGGRESSIVE : Style.DEFENSIVE));
			}
		}
		
		ctx.combat.style(s);
		
	}

	@Override
	public void print() {
		
		System.out.println("Attacking NPCs");
		
	}
	
}
