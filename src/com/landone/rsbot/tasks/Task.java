package com.landone.rsbot.tasks;
import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;

public abstract class Task extends ClientAccessor {

	public Task(ClientContext ctx) {
		
		super(ctx);
		
	}
	
	public abstract boolean activate();
	
	public abstract void reset();
	
	public abstract void execute();
	
	public abstract void print();

}
