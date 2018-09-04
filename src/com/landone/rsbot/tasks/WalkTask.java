package com.landone.rsbot.tasks;

import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import com.landone.rsbot.constants.OBJECTS;

public class WalkTask extends Task {
	
	private Tile[] path;//Series of tiles
	private Tile destTile;
	private int dest;//Current destination index
	private boolean forwards;

	public WalkTask(ClientContext ctx, Tile[] path, boolean forwards) {
		
		super(ctx);
		this.path = path;
		this.forwards = forwards;
		this.dest = forwards ? 0 : path.length - 1;
		setDest();
		
	}
	
	public boolean isDone() {
		
		return (!forwards || dest >= path.length) && (forwards || dest < 0);
		
	}
	
	private boolean isLastDest() {
		return (forwards && dest == path.length - 1) || (!forwards && dest == 0);
	}
	
	private void setDest() {//Randomizes destination
		if(dest < 0 || dest >= path.length) {
			return;
		}
		this.destTile = new Tile(path[dest].x() - 2 + (int)Math.round(Math.random()*4),
				path[dest].y() - 2 + (int)Math.round(Math.random()*4), path[dest].floor());
	}
	
	private void nextDest() {//Goes to next destination index
		dest += (forwards ? 1 : -1);
	}
	
	@Override
	public boolean activate() {
		
		return !isDone();
		
	}

	@Override
	public void execute() {
		
		if(!ctx.movement.running() && ctx.movement.energyLevel() >= 50) {
			ctx.movement.running(true);
		}
		
		Tile current = ctx.players.local().tile();
		
		if(current.floor() != destTile.floor()) {
			
			GameObject stairs = ctx.objects.select().id(OBJECTS.STAIRCASE).nearest().poll();
			
			if(stairs == null) {
				System.out.println("NULL staircase GameObject");
			}
			
			if(!stairs.inViewport()) {
				ctx.camera.turnTo(stairs);
			}
			
			stairs.interact("Climb-" + (current.floor() < path[dest].floor() ? "up" : "down"));
			return;//Skip this poll to allow climb
			
		}
		
		if(current.distanceTo(destTile) < 10 && !isLastDest()) {
			nextDest();
			setDest();
		}else if(current.equals(path[dest])) {//Last destination should be equal
			nextDest();
		}else {
			Tile closest = ctx.movement.closestOnMap(path[dest]);
			ctx.movement.step(closest);
		}
		
	}

	@Override
	public void reset() {
		
		this.dest = forwards ? 0 : path.length - 1;
		setDest();
		
	}

	@Override
	public void print() {
		
		System.out.println("Walking to next destination");
		
	}

}
