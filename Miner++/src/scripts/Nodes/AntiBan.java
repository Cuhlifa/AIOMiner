package scripts.Nodes;

import org.tribot.api2007.Player;
import org.tribot.api2007.Skills.SKILLS;

import scripts.Miner;



public class AntiBan extends scripts.MinerPlusPlus.Node {

	@Override
	public void execute() {

		Miner.AntiBan.performCombatCheck();

		Miner.AntiBan.performEquipmentCheck();

		Miner.AntiBan.performExamineObject();

		Miner.AntiBan.performFriendsCheck();

		Miner.AntiBan.performLeaveGame();

		Miner.AntiBan.performMusicCheck();

		Miner.AntiBan.performPickupMouse();

		Miner.AntiBan.performQuestsCheck();

		Miner.AntiBan.performRandomMouseMovement();

		Miner.AntiBan.performRandomRightClick();

		Miner.AntiBan.performRotateCamera();

		Miner.AntiBan.performTimedActions(SKILLS.MINING);

		Miner.AntiBan.performXPCheck(SKILLS.MINING);

	}

	@Override
	public boolean validate() {

		return Player.getAnimation() != -1;

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "AntiBan";
	}

}
