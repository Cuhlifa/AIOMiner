package scripts.Nodes;

import org.tribot.api2007.Inventory;

import scripts.Miner;
import scripts.MinerPlusPlus.MiningMethod;
import scripts.MinerPlusPlus.Node;

public class Drop extends Node {

	@Override
	public void execute() {

		Inventory.dropAllExcept(Miner.PICKAXES);
		
	}

	@Override
	public boolean validate() {

		if(Inventory.isFull() && Miner.method == MiningMethod.POWERMINE){
			
			return true;
			
		}else{return false;}
	
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Drop";
	}

}
