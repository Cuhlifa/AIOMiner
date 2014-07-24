package scripts;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api.util.ABCUtil;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Camera.ROTATION_METHOD;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Game;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Login;
import org.tribot.api2007.Login.STATE;
import org.tribot.api2007.Objects;
import org.tribot.api2007.PathFinding;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.api2007.util.ThreadSettings;
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
	public static ABCUtil AntiBan = new ABCUtil();
	public static boolean GUI_ISCOMPLETE = false;
	public static RSObject CurrentlyMining;
	public static RSObject MineNext;
	public static int[] SelectedOres;
	public static Set<Integer> SelectedOresSet = new HashSet<>();
	public static int StartingLevel;
	public static int StartingXP;
	public static long StartTime;
	public static int MinedOres = 0;
	public static RSTile StartTile;
	public static GUI gui;
	public static boolean AdvancedHUD = false;
	public static boolean HighlightPath = false;
	public static boolean HighlightModel = false;
	public static boolean DrawHUD = false;
	public static MiningMethod method = MiningMethod.BANKING;
	public static ScriptState state = ScriptState.IDLE;
	public static final String[] PICKAXES = {"Bronze pickaxe","Iron pickaxe","Steel pickaxe","Mithril pickaxe","Adamant pickaxe","Rune pickaxe","Dragon pickaxe"};
	public static final String[] PICKAXE_HEADS = {"Bronze pick head","Iron pick head","Steel pick head","Mithril pick head","Adamant pick head","Rune pick head","Dragon pick head"};
	public static final int PICKAXE_HANDLE = 466;
	public static final int[] ANIMATIONS = { 624, 625, 628 };
	public static Image HUD;
	public static int InventoryCount;
	public static boolean SelectMode = false;
	public static long PathPaintTimeout = 0;
	public static RSTile[] WalkPath;
	public static String[] OreNames = { "Tin ore", "Copper ore", "Silver ore",
			"Runite ore", "Iron ore", "Coal", "Gold ore", "Mithril ore",
			"Adamantite ore" };
	public static ArrayList<Node> Nodes = new ArrayList<Node>();
	public static Miner miner;
	
	public boolean containsID(int Id) {

		if (SelectedOres != null && SelectedOres.length > 0) {

			for (int i : SelectedOres) {

				if (i == Id) {
					return true;
				}

			}

			return false;

		} else {
			return false;
		}

	}

//	public void findPickaxeHead() {
//
//		RSGroundItem[] Objs = GroundItems.find(PICKAXE_HEADS);
//
//		if (Objs.length > 0 && Inventory.getCount(PICKAXE_HANDLE) > 0
//				|| Equipment.getCount(PICKAXE_HANDLE) > 0) {
//
//			if (Objs[0].isOnScreen()) {
//
//				if (Objs[0].click("Take")) {
//
//					sleep(AntiBan.DELAY_TRACKER.ITEM_INTERACTION.next());
//					AntiBan.DELAY_TRACKER.ITEM_INTERACTION.reset();
//					RSItem[] head = Inventory.find(PICKAXE_HEADS);
//					RSItem[] handle = Inventory.find(PICKAXE_HANDLE);
//
//					if (head[0].click("Use")) {
//
//						sleep(AntiBan.DELAY_TRACKER.ITEM_INTERACTION.next());
//						AntiBan.DELAY_TRACKER.ITEM_INTERACTION.reset();
//						handle[0].click("Use");
//
//					}
//
//				}
//
//			}
//
//		}
//
//	}

	@Override
	public void onPaint(Graphics g) {

		Mouse.setSpeed(General.random(110, 140));

		int CurrentXP = Skills.getXP(SKILLS.MINING);
		int GainedXP = CurrentXP - StartingXP;
		int CurrentLevel = Skills.getActualLevel(SKILLS.MINING);
		int GainedLevel = CurrentLevel - StartingLevel;
		long RunTime = (System.currentTimeMillis() - StartTime) / 1000;
		long hours = TimeUnit.SECONDS.toHours(RunTime);
		long minutes = TimeUnit.SECONDS.toMinutes(RunTime
				- TimeUnit.HOURS.toSeconds(hours));
		long seconds = RunTime
				- (TimeUnit.HOURS.toSeconds(hours) + TimeUnit.MINUTES
						.toSeconds(minutes));
		int MinedOresHour = 0;
		int GainedXPHour = 0;
		if (MinedOres > 0) {
			MinedOresHour = (int) ((MinedOres * 3600) / RunTime);
		}
		if (GainedXP > 0) {
			GainedXPHour = (int) ((GainedXP * 3600) / RunTime);
		}

		if (Login.getLoginState() == STATE.INGAME) {

			Graphics2D g2d = (Graphics2D) g;

			if (!GUI_ISCOMPLETE) {

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

			if (SelectedOres != null && SelectedOres.length > 0) {

				RSObject[] Objs = Objects.find(30, SelectedOres);

				for (RSObject Obj : Objs) {

					for (Polygon p : Obj.getModel().getTriangles()) {

						g2d.setColor(Color.YELLOW);
						g2d.draw(p);

					}

				}

			}

			if (CurrentlyMining != null && HighlightModel) {

				for (Polygon p : CurrentlyMining.getModel().getTriangles()) {

					g2d.setColor(Color.GREEN);
					g2d.draw(p);

				}

			}

			if (MineNext != null && HighlightModel
					&& state == ScriptState.MINING) {

				for (Polygon p : MineNext.getModel().getTriangles()) {

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
			g.drawString("" + MinedOres + " (" + MinedOresHour + ")", 240, 402);
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

		}else{
			
			miner = this;
			General.useAntiBanCompliance(true);
			gui = new GUI();
			gui.frmMiner.setVisible(true);
			StartTile = Player.getPosition();
			StartingLevel = SKILLS.MINING.getActualLevel();
			StartingXP = Skills.getXP(SKILLS.MINING);
			StartTime = System.currentTimeMillis();
			try {
				HUD = ImageIO.read(new URL("http://i.imgur.com/EWhEBTU.png"));
			} catch (Exception e) {
			}
			InventoryCount = Inventory.getCount(OreNames);
			Nodes.add(new Bank());
			Nodes.add(new scripts.Nodes.AntiBan());
			Nodes.add(new Drop());
			Nodes.add(new Mine());
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

//	private void stairFailsafe() {
//		final RSObject[] stairs = Objects.findNearest(10, 2162);
//		if (stairs.length > 0) {
//			if (stairs[0].isOnScreen()) {
//				stairs[0].hover(
//						new Point(General.random(-13, 10), General
//								.random(-9, 5)), new Point(null));
//				Timing.waitCondition(new Condition() {
//
//					@Override
//					public boolean active() {
//						return Game.getUptext().contains("Stairs");
//					}
//
//				}, 691);
//				if (Game.getUptext().contains("Stairs")) {
//					Mouse.click(1);
//				}
//				Timing.waitCondition(new Condition() {
//
//					@Override
//					public boolean active() {
//						return !stairs[0].isOnScreen();
//					}
//
//				}, 2861);
//			}
//		}
//
//	}

	public void Start() {

		while (true) {

			if (GUI_ISCOMPLETE && Login.getLoginState() == STATE.INGAME) {

				System.out.println("Checking Node");
				
				for(Node n : Nodes){
					System.out.println("Checking " + n.getName());
					if(n.validate()){n.execute(); System.out.println("Excuting " + n.getName());}
					
				}
				
				int InvCount = Inventory.getCount(OreNames);
				if (InvCount > InventoryCount) {

					MinedOres += InvCount - InventoryCount;
					InventoryCount = InvCount;

				}

			}

			sleep(300,750);
		}

	}

}
