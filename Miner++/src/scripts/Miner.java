package scripts;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api.util.ABCUtil;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Login;
import org.tribot.api2007.Login.STATE;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

import scripts.MinerPlusPlus.GUI;
import scripts.MinerPlusPlus.MiningMethod;
import scripts.MinerPlusPlus.Node;
import scripts.MinerPlusPlus.ScriptState;
import scripts.Nodes.Bank;
import scripts.Nodes.Drop;
import scripts.Nodes.Mine;

@ScriptManifest(authors = { "Peticca10" }, name = "AIOMiner++", category = "Mining")
public class Miner extends Script implements Painting {

	// Variables
	public static ABCUtil antiBan = new ABCUtil();
	public static boolean guiIsComplete = false;
	public static RSObject currentlyMining;
	public static RSObject mineNext;
	public static int[] selectedOres;
	public static Set<Integer> SelectedOresSet = new HashSet<>();
	public static int startingLevel;
	public static int startingXP;
	public static long startTime;
	public static int minedOres = 0;
	public static RSTile startTile;
	public static GUI gui;
	public static boolean advancedHUD = false;
	public static boolean highlightPath = false;
	public static boolean highlightModel = false;
	public static boolean drawHUD = false;
	public static MiningMethod method = MiningMethod.BANKING;
	public static ScriptState state = ScriptState.IDLE;
	public static final String[] PICKAXES = { "Bronze pickaxe", "Iron pickaxe",
		"Steel pickaxe", "Mithril pickaxe", "Adamant pickaxe",
		"Rune pickaxe", "Dragon pickaxe" };
	public static final String[] PICKAXE_HEADS = { "Bronze pick head",
		"Iron pick head", "Steel pick head", "Mithril pick head",
		"Adamant pick head", "Rune pick head", "Dragon pick head" };
	public static final String PICKAXE_HANDLE = "Pickaxe handle";
	public static final int[] ANIMATIONS = { 624, 625, 628 };
	public static Image HUD;
	public static int inventoryCount;
	public static boolean selectMode = false;
	public static long pathPaintTimeout = 0;
	public static RSTile[] walkPath;
	public static String[] oreNames = { "Tin ore", "Copper ore", "Silver ore",
		"Runite ore", "Iron ore", "Coal", "Gold ore", "Mithril ore",
	"Adamantite ore" };
	public static ArrayList<Node> nodes = new ArrayList<Node>();
	public static Miner miner;

	public boolean containsID(int Id) {

		if (selectedOres != null && selectedOres.length > 0) {

			for (int i : selectedOres) {

				if (i == Id) {
					return true;
				}

			}

			return false;

		} else {
			return false;
		}

	}

	// public void findPickaxeHead() {
	//
	// RSGroundItem[] Objs = GroundItems.find(PICKAXE_HEADS);
	//
	// if (Objs.length > 0 && Inventory.getCount(PICKAXE_HANDLE) > 0
	// || Equipment.getCount(PICKAXE_HANDLE) > 0) {
	//
	// if (Objs[0].isOnScreen()) {
	//
	// if (Objs[0].click("Take")) {
	//
	// sleep(AntiBan.DELAY_TRACKER.ITEM_INTERACTION.next());
	// AntiBan.DELAY_TRACKER.ITEM_INTERACTION.reset();
	// RSItem[] head = Inventory.find(PICKAXE_HEADS);
	// RSItem[] handle = Inventory.find(PICKAXE_HANDLE);
	//
	// if (head[0].click("Use")) {
	//
	// sleep(AntiBan.DELAY_TRACKER.ITEM_INTERACTION.next());
	// AntiBan.DELAY_TRACKER.ITEM_INTERACTION.reset();
	// handle[0].click("Use");
	//
	// }
	//
	// }
	//
	// }
	//
	// }
	//
	// }

	@Override
	public void onPaint(Graphics g) {

		Mouse.setSpeed(General.random(110, 140));

		int CurrentXP = Skills.getXP(SKILLS.MINING);
		int GainedXP = CurrentXP - startingXP;
		int CurrentLevel = Skills.getActualLevel(SKILLS.MINING);
		int GainedLevel = CurrentLevel - startingLevel;
		long RunTime = (System.currentTimeMillis() - startTime) / 1000;
		long hours = TimeUnit.SECONDS.toHours(RunTime);
		long minutes = TimeUnit.SECONDS.toMinutes(RunTime
				- TimeUnit.HOURS.toSeconds(hours));
		long seconds = RunTime
				- (TimeUnit.HOURS.toSeconds(hours) + TimeUnit.MINUTES
						.toSeconds(minutes));
		int MinedOresHour = 0;
		int GainedXPHour = 0;
		if (minedOres > 0) {
			MinedOresHour = (int) ((minedOres * 3600) / RunTime);
		}
		if (GainedXP > 0) {
			GainedXPHour = (int) ((GainedXP * 3600) / RunTime);
		}

		if (Login.getLoginState() == STATE.INGAME) {

			Graphics2D g2d = (Graphics2D) g;

			if (!guiIsComplete) {

				RSObject[] Objs = Objects.getAll(30);

				for (RSObject obj : Objs) {

					if (obj.getDefinition().getName().equalsIgnoreCase("Rocks")
							&& !containsID(obj.getDefinition().getID())) {

						for (Polygon p : obj.getModel().getTriangles()) {

							g2d.setColor(Color.RED);
							g2d.draw(p);

						}

					}

				}

				if (gui != null && gui.getList_Ores().getSelectedItem() != null
						&& gui.getList_Ores().getSelectedItem().length() > 0) {

					RSObject[] Objs1 = Objects.find(30, Integer.valueOf(gui
							.getList_Ores().getSelectedItem()));

					for (RSObject Obj : Objs1) {

						for (Polygon p : Obj.getModel().getTriangles()) {

							g2d.setColor(Color.MAGENTA);
							g2d.draw(p);

						}

					}

				}

			}

			if (selectedOres != null && selectedOres.length > 0) {

				RSObject[] Objs = Objects.find(30, selectedOres);

				for (RSObject Obj : Objs) {

					for (Polygon p : Obj.getModel().getTriangles()) {

						g2d.setColor(Color.YELLOW);
						g2d.draw(p);

					}

				}

			}

			if (currentlyMining != null && highlightModel) {

				for (Polygon p : currentlyMining.getModel().getTriangles()) {

					g2d.setColor(Color.GREEN);
					g2d.draw(p);

				}

			}

			if (mineNext != null && highlightModel
					&& state == ScriptState.MINING) {

				for (Polygon p : mineNext.getModel().getTriangles()) {

					g2d.setColor(Color.BLUE);
					g2d.draw(p);

				}

			}

			try {
				g2d.drawImage(HUD, 7, 345, null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			g.setColor(Color.WHITE);
			g.setFont(new Font("Verdana", Font.BOLD, 12));
			g.drawString("" + GainedXP + " (" + GainedXPHour + ")", 91, 402);
			g.drawString("" + "N/A", 91, 439);
			g.drawString("" + minedOres + " (" + MinedOresHour + ")", 240, 402);
			g.drawString("" + hours + "H " + minutes + "M " + seconds + "S",
					370, 402);
			g.drawString("" + GainedLevel + " (" + CurrentLevel + ")", 240, 439);
			g.drawString("" + state.toString(), 370, 439);

		}

	}

	@Override
	public void run() {

		if (!(Login.getLoginState() == STATE.INGAME)) {

			sleep(2000);
			run();

		} else {

			miner = this;
			General.useAntiBanCompliance(true);
			gui = new GUI();
			gui.frmMiner.setVisible(true);
			startTile = Player.getPosition();
			startingLevel = SKILLS.MINING.getActualLevel();
			startingXP = Skills.getXP(SKILLS.MINING);
			startTime = System.currentTimeMillis();
			try {
				HUD = ImageIO.read(new URL("http://i.imgur.com/EWhEBTU.png"));
			} catch (Exception e) {
			}
			inventoryCount = Inventory.getCount(oreNames);
			nodes.add(new Bank());
			nodes.add(new scripts.Nodes.AntiBan());
			nodes.add(new Drop());
			nodes.add(new Mine());
			Start();

		}

	}

	public int[] SetToArray(Set<Integer> SetInt) {

		int Count = 0;
		int[] ArrayInt = new int[SetInt.size()];

		for (Integer i : SetInt) {

			ArrayInt[Count] = i;
			Count++;

		}

		return ArrayInt;

	}

	// private void stairFailsafe() {
	// final RSObject[] stairs = Objects.findNearest(10, 2162);
	// if (stairs.length > 0) {
	// if (stairs[0].isOnScreen()) {
	// stairs[0].hover(
	// new Point(General.random(-13, 10), General
	// .random(-9, 5)), new Point(null));
	// Timing.waitCondition(new Condition() {
	//
	// @Override
	// public boolean active() {
	// return Game.getUptext().contains("Stairs");
	// }
	//
	// }, 691);
	// if (Game.getUptext().contains("Stairs")) {
	// Mouse.click(1);
	// }
	// Timing.waitCondition(new Condition() {
	//
	// @Override
	// public boolean active() {
	// return !stairs[0].isOnScreen();
	// }
	//
	// }, 2861);
	// }
	// }
	//
	// }

	public void Start() {

		while (true) {

			if (guiIsComplete && Login.getLoginState() == STATE.INGAME) {

				System.out.println("Checking Node");

				for (Node n : nodes) {
					System.out.println("Checking " + n.getName());
					if (n.validate()) {
						n.execute();
						System.out.println("Excuting " + n.getName());
					}

				}

				int InvCount = Inventory.getCount(oreNames);
				if (InvCount > inventoryCount) {

					minedOres += InvCount - inventoryCount;
					inventoryCount = InvCount;

				}

			}

			sleep(300, 750);
		}

	}

}
