package scripts.Nodes;

import java.awt.Point;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api.util.ABCUtil;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.PathFinding;
import org.tribot.api2007.Player;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSObject;

import scripts.Miner;
import scripts.MinerPlusPlus.Node;

public class Mine extends Node {

	@Override
	public void execute() {

		if(PathFinding.distanceTo(Miner.StartTile, false) > 20){
			
			WebWalking.walkTo(Miner.StartTile);
			
		}else{
			
			int use;
			int hover;
			RSObject[] ores = Objects.findNearest(50, Miner.SelectedOres);
			
			if(ores != null && ores.length > 0){
				
				if(Miner.AntiBan.BOOL_TRACKER.USE_CLOSEST.next() || ores.length > 2){
					
					Miner.AntiBan.BOOL_TRACKER.USE_CLOSEST.reset();
					use = 0;
					hover = 1;
					
				}else{
					
					use = 1;
					hover = 0;
					
				}
				
				if(ores.length > use && ores[use] != null){
					
					General.sleep(Miner.AntiBan.DELAY_TRACKER.NEW_OBJECT.next());
					Miner.AntiBan.DELAY_TRACKER.NEW_OBJECT.reset();
					
					if(Miner.miner.containsID(ores[use].getDefinition().getID()) && DynamicClicking.clickRSModel(ores[use].getModel(), 1)){
						
						Miner.CurrentlyMining = ores[use];
						
						Timing.waitCondition(new Condition() {
							
							@Override
							public boolean active() {
								
								if(Player.getAnimation() != -1){
									
									return true;
									
								}else{return false;}
								
							}
						}, 2000);
						
						if(ores[hover] != null && Miner.AntiBan.BOOL_TRACKER.HOVER_NEXT.next()){
							
							Miner.AntiBan.BOOL_TRACKER.HOVER_NEXT.reset();
							
							Point p[] = ores[hover].getModel().getPoints();
							Miner.MineNext = ores[hover];
							
							int pUse = General.random(0, p.length - 1);
							
							if(p[pUse] != null){
								
								Mouse.move(p[pUse]);
								
							}
							
							
						}
						
					}
					
				}
				
			}
			
		}
		
	}

	@Override
	public boolean validate() {

		if(Player.getAnimation() == -1 && !Inventory.isFull()){
			
			return true;
			
		}else{return false;}
	
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Mine";
	}

}
