package scripts.Nodes;

import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.WebWalking;

import scripts.Miner;
import scripts.MinerPlusPlus.MiningMethod;
import scripts.MinerPlusPlus.Node;

public class Bank extends Node {

	@Override
	public void execute() {

		if (!Banking.isInBank()) {

			WebWalking.walkToBank();

		} else {

			if (Banking.openBank()) {

				if (Banking.isBankScreenOpen()) {

					Banking.depositAllExcept(Miner.PICKAXES);
					WebWalking.walkTo(Miner.startTile);

				}

			}

		}

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Bank";
	}

	@Override
	public boolean validate() {

		if (Inventory.isFull() && Miner.method == MiningMethod.BANKING) {

			return true;

		} else {
			return false;
		}

	}

}
