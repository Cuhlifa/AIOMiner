package scripts.Nodes;

import org.tribot.api2007.Player;
import org.tribot.api2007.Skills.SKILLS;

import scripts.Miner;

public class AntiBan extends scripts.MinerPlusPlus.Node {

	@Override
	public void execute() {

		Miner.antiBan.performCombatCheck();

		Miner.antiBan.performEquipmentCheck();

		Miner.antiBan.performExamineObject();

		Miner.antiBan.performFriendsCheck();

		Miner.antiBan.performLeaveGame();

		Miner.antiBan.performMusicCheck();

		Miner.antiBan.performPickupMouse();

		Miner.antiBan.performQuestsCheck();

		Miner.antiBan.performRandomMouseMovement();

		Miner.antiBan.performRandomRightClick();

		Miner.antiBan.performRotateCamera();

		Miner.antiBan.performTimedActions(SKILLS.MINING);

		Miner.antiBan.performXPCheck(SKILLS.MINING);

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "AntiBan";
	}

	@Override
	public boolean validate() {

		return Player.getAnimation() != -1;

	}

}
