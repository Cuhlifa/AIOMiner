package scripts.MinerPlusPlus;

import org.tribot.api.interfaces.Positionable;
import org.tribot.api2007.PathFinding;
import org.tribot.api2007.types.RSTile;

public class PathGen extends Thread{
	
	public RSTile[] getPath(Positionable start, Positionable end, boolean object){
		
		return PathFinding.generatePath(start, end, object);
		
	}
	
}
