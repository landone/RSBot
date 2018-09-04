package com.landone.rsbot.tasks;

import org.powerbot.script.rt4.Bank;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;

import com.landone.rsbot.constants.ITEMS;
import com.landone.rsbot.constants.NPCS;

public class BankTask extends Task {

	public BankTask(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		
		return ctx.inventory.select().id(ITEMS.BRONZE_BAR).count() > 0;
		
	}

	@Override
	public void execute() {
		
		if(!ctx.bank.opened()) {
			
			Npc banker = ctx.npcs.select().id(NPCS.BANKER).poll();
			
			if(banker == null) {
				System.out.println("NULL banker NPC");
				return;
			}
			
			if(!banker.inViewport()) {
				ctx.movement.step(banker);
				ctx.camera.turnTo(banker);
			}else {
				banker.interact("Bank");
			}
			return;
			
		}
		
		ctx.bank.deposit(ITEMS.BRONZE_BAR, Bank.Amount.ALL);
		ctx.bank.close();
		
	}

	@Override
	public void reset() {
		
		//No variables to reset
		
	}

	@Override
	public void print() {
		
		System.out.println("Depositing items");
		
	}

}
