package scripts;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.net.URL;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.DynamicMouse;
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
import org.tribot.api2007.Objects;
import org.tribot.api2007.PathFinding;
import org.tribot.api2007.Login.STATE;
import org.tribot.api2007.Player;
import org.tribot.api2007.Projection;
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

import scripts.MinerPlusPlus.*;

@ScriptManifest(authors = {"Peticca10"}, name="AIOMiner++", category = "Mining")
public class Miner extends Script implements Painting{

	//Variables
	public ABCUtil AntiBan = new ABCUtil();
	public boolean GUI_ISCOMPLETE = false;
	public RSObject CurrentlyMining;
	public RSObject MineNext;
	public int[] SelectedOres;
	public Set<Integer> SelectedOresSet = new HashSet<>();
	public int StartingLevel;
	public int StartingXP;
	public long StartTime;
	public int MinedOres = 0;
	public RSTile StartTile;
	public GUI gui;
	public boolean AdvancedHUD = false;
	public boolean HighlightPath = false;
	public boolean HighlightModel = false;
	public boolean DrawHUD = false;
	public MiningMethod method = MiningMethod.BANKING;
	public ScriptState state = ScriptState.IDLE;
	public final int[] PICKAXES = {1265,1267,1269,1271,1273,1275};
	public final int[] PICKAXE_HEADS = {480,482,484,486,488,490};
	public final int PICKAXE_HANDLE = 466;
	private static final int[] ANIMATIONS = {624,625,628};
	public Image HUD;
	public int InventoryCount;
	public int[] Ores;
	public boolean SelectMode = false;
	public long PathPaintTimeout = 0;
	public RSTile[] WalkPath;
	public String[] OreNames = {"Tin ore","Copper ore","Silver ore","Runite ore","Iron ore","Coal","Gold ore", "ore", "Adamantite ore"};
	
	@Override
	public void run() {
		
		if(!(Login.getLoginState() == STATE.INGAME)){
			
			sleep(2000);
			run();
			
		}
		
		ThreadSettings.get().setObjectCModelMethod(ThreadSettings.MODEL_CLICKING_METHOD.CENTRE);
		General.useAntiBanCompliance(true);
		gui = new GUI(this);
		gui.frmMiner.setVisible(true);
		StartTile = Player.getPosition();
		StartingLevel = SKILLS.MINING.getActualLevel();
		StartingXP = Skills.getXP(SKILLS.MINING);
		StartTime = System.currentTimeMillis();
		try {HUD = ImageIO.read(new URL("http://i.imgur.com/EWhEBTU.png"));} catch (Exception e) {}
		InventoryCount = Inventory.getCount(OreNames);
		Start();
		
	}

	@Override
	public void onPaint(Graphics g) {
		
		Mouse.setSpeed(General.random(95,110));
		
		int CurrentXP = Skills.getXP(SKILLS.MINING);
		int GainedXP = CurrentXP - StartingXP;
		int CurrentLevel = Skills.getActualLevel(SKILLS.MINING);
		int GainedLevel = CurrentLevel - StartingLevel;
		long RunTime = (System.currentTimeMillis() - StartTime) / 1000;
		long hours = TimeUnit.SECONDS.toHours(RunTime);
		long minutes = TimeUnit.SECONDS.toMinutes(RunTime - TimeUnit.HOURS.toSeconds(hours));
		long seconds = RunTime - (TimeUnit.HOURS.toSeconds(hours) + TimeUnit.MINUTES.toSeconds(minutes));
		int MinedOresHour = 0;
		int GainedXPHour = 0;
		if(MinedOres > 0){MinedOresHour = (int) ((MinedOres * 3600) / RunTime);}
		if(GainedXP > 0){GainedXPHour = (int) ((GainedXP * 3600) / RunTime);}

		if(Login.getLoginState() == STATE.INGAME){
			
			Graphics2D g2d = (Graphics2D) g;
			
			if(!GUI_ISCOMPLETE){
				
				RSObject[] Objs = Objects.getAll(30);
				
				for(RSObject obj : Objs){
					
					if(obj.getDefinition().getName().equalsIgnoreCase("Rocks") && !containsID(obj.getDefinition().getID())){
						
						for(Polygon p : obj.getModel().getTriangles()){
							
							g2d.setColor(Color.RED);
							g2d.draw(p);
							
						}
						
					}
					
				}
				
				if(gui.getList_Ores().getSelectedItem() != null && gui.getList_Ores().getSelectedItem().length() > 0){

					RSObject[] Objs1 = Objects.find(30, Integer.valueOf(gui.getList_Ores().getSelectedItem()));
					
					for(RSObject Obj : Objs1){
						
						for(Polygon p : Obj.getModel().getTriangles()){
							
							g2d.setColor(Color.MAGENTA);
							g2d.draw(p);
							
						}
				
					}
					
				}
				
				
			}
			
			if(SelectedOres != null && SelectedOres.length > 0){

				RSObject[] Objs = Objects.find(30, SelectedOres);
				
				for(RSObject Obj : Objs){
					
					for(Polygon p : Obj.getModel().getTriangles()){
						
						g2d.setColor(Color.YELLOW);
						g2d.draw(p);
						
					}
			
				}
				
			}

			if(CurrentlyMining != null && HighlightModel){
				
				for(Polygon p : CurrentlyMining.getModel().getTriangles()){
					
					g2d.setColor(Color.GREEN);
					g2d.draw(p);
					
				}	
				
			}
			
			if(MineNext != null && HighlightModel && state == ScriptState.MINING){
				
				for(Polygon p : MineNext.getModel().getTriangles()){
					
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
			g.drawString("" + hours + "H " +  minutes + "M " + seconds + "S", 370, 402);
			g.drawString("" + GainedLevel + " (" + CurrentLevel + ")", 240, 439);
			g.drawString("" + state.toString(), 370, 439);
			
		}
		
	}
	
	public void Start() {

		while (true) {
			
			if(GUI_ISCOMPLETE && Login.getLoginState() == STATE.INGAME){
				
				stairFailsafe();
				
				if(isMining() || Player.getAnimation() == -1){
					
					AntiBan.performCombatCheck();
					AntiBan.performEquipmentCheck();
					AntiBan.performExamineObject();
					AntiBan.performFriendsCheck();
					AntiBan.performLeaveGame();
					AntiBan.performMusicCheck();
					AntiBan.performPickupMouse();
					AntiBan.performQuestsCheck();
					AntiBan.performRandomMouseMovement();
					AntiBan.performRandomRightClick();
					AntiBan.performRotateCamera();
					AntiBan.performTimedActions(SKILLS.MINING);
					AntiBan.performXPCheck(SKILLS.MINING);
					
				}
				
				if(Inventory.isFull()){
					
					if(method == MiningMethod.BANKING){
						
						state = ScriptState.BANKING;
						Bank();
						
					}
					
					if(method == MiningMethod.POWERMINE){
						
						state = ScriptState.DROPING;
						Drop();
						
					}
					
				}
				
				if(!Inventory.isFull() && PathFinding.distanceTo(StartTile, false) > 30 && !isMining()){
					
					state = ScriptState.WALKING;
					WalkToMine();
					
				}
				
				if(!Inventory.isFull() && PathFinding.distanceTo(StartTile, false) < 30 && !isMining()){
					
					Mine();
					
				}
				
				if(method == MiningMethod.M1D1 && Inventory.getCount(Ores) > 0){
					
					state = ScriptState.DROPING;
					Drop();
					
				}
				
				if(Inventory.getCount(PICKAXE_HANDLE) > 0){
					
					findPickaxeHead();
					
				}
				
				int InvCount = Inventory.getCount(OreNames);

				if(InvCount > InventoryCount){

					MinedOres += InvCount - InventoryCount;
					InventoryCount = InvCount;
					
				}
				
			}
			
			sleep(2500, 4000);
		}
		
	}

	public void findPickaxeHead() {

		RSGroundItem[] Objs = GroundItems.find(PICKAXE_HEADS);
		
		if(Objs.length > 0 && Inventory.getCount(PICKAXE_HANDLE) > 0 || Equipment.getCount(PICKAXE_HANDLE) > 0){
			
			if(Objs[0].isOnScreen()){
				
				if(Objs[0].click("Take")){
					
					sleep(AntiBan.DELAY_TRACKER.ITEM_INTERACTION.next());
					AntiBan.DELAY_TRACKER.ITEM_INTERACTION.reset();
					RSItem[] head = Inventory.find(PICKAXE_HEADS);
					RSItem[] handle = Inventory.find(PICKAXE_HANDLE);
					
					if(head[0].click("Use")){
							
						sleep(AntiBan.DELAY_TRACKER.ITEM_INTERACTION.next());
						AntiBan.DELAY_TRACKER.ITEM_INTERACTION.reset();
						handle[0].click("Use");
							
					}
					
				}
				
			}
			
		}
		
	}

	public void Mine() {
		
		Random Camera = new Random();

		RSObject[] Rocks = Objects.findNearest(20, SelectedOres);
		
		if(Rocks.length > 0){
			
			if(AntiBan.BOOL_TRACKER.USE_CLOSEST.next() || Rocks.length < 2){
				
				switch (Camera.nextInt(1)) {
				
				case 0:
					
					org.tribot.api2007.Camera.setRotationMethod(ROTATION_METHOD.ONLY_KEYS);
					
					break;
					
				case 1:
					
					org.tribot.api2007.Camera.setRotationMethod(ROTATION_METHOD.ONLY_MOUSE);
					
					break;

				default:
					break;
				}
				
				int Angle = org.tribot.api2007.Camera.getTileAngle(Rocks[0].getPosition()) - General.random(-30, 30);
				org.tribot.api2007.Camera.setCameraRotation(Angle);
				
				if(Rocks[0].isOnScreen() && Rocks[0].getModel().getVertexCount() < 150){
					
					if(DynamicClicking.clickRSModel(Rocks[0].getModel(), "Mine")){
						
						CurrentlyMining = Rocks[0];
						if(Rocks.length > 1){MineNext = Rocks[1];}
						state = ScriptState.MINING;
						
						if(AntiBan.BOOL_TRACKER.HOVER_NEXT.next()){
							
							sleep(AntiBan.DELAY_TRACKER.SWITCH_OBJECT.next());
							AntiBan.DELAY_TRACKER.SWITCH_OBJECT.reset();
							Rocks[1].hover(new Point(General.random(-13, 10),General.random(-9, 5)), new Point(null));
							AntiBan.BOOL_TRACKER.HOVER_NEXT.reset();
							
						}else{
							
							sleep(AntiBan.DELAY_TRACKER.SWITCH_OBJECT.next());
							AntiBan.DELAY_TRACKER.SWITCH_OBJECT.reset();
							Rocks[0].hover(new Point(General.random(-13, 10),General.random(-9, 5)), new Point(null));
							
						}
						
					}
					
				}else{
					
					if(WebWalking.walkTo(Rocks[0].getPosition())){

						sleep(100, 300);
						
						if(Rocks[0].getModel().getVertexCount() < 150 && DynamicClicking.clickRSModel(Rocks[0].getModel(), "Mine")){
							
							CurrentlyMining = Rocks[0];
							MineNext = Rocks[1];
							state = ScriptState.MINING;
						

							
							if(AntiBan.BOOL_TRACKER.HOVER_NEXT.next()){
								
								sleep(AntiBan.DELAY_TRACKER.SWITCH_OBJECT.next());
								AntiBan.DELAY_TRACKER.SWITCH_OBJECT.reset();
								Rocks[1].hover();
								AntiBan.BOOL_TRACKER.HOVER_NEXT.reset();
								
							}else{
								
								sleep(AntiBan.DELAY_TRACKER.SWITCH_OBJECT.next());
								AntiBan.DELAY_TRACKER.SWITCH_OBJECT.reset();
								Rocks[0].hover();
								
							}
							
						}
						
					}
					
				}
				AntiBan.BOOL_TRACKER.USE_CLOSEST.reset();
			}else{
				
				switch (Camera.nextInt(1)) {
				
				case 0:
					
					org.tribot.api2007.Camera.setRotationMethod(ROTATION_METHOD.ONLY_KEYS);
					
					break;
					
				case 1:
					
					org.tribot.api2007.Camera.setRotationMethod(ROTATION_METHOD.ONLY_MOUSE);
					
					break;

				default:
					break;
				}
				
				int Angle = org.tribot.api2007.Camera.getTileAngle(Rocks[0].getPosition()) - General.random(-30, 30);
				org.tribot.api2007.Camera.setCameraRotation(Angle);
				
				if(Rocks[1].isOnScreen() && Rocks[1].getModel().getVertexCount() < 150){
					
					if(DynamicClicking.clickRSModel(Rocks[1].getModel(), "Mine")){
						
						CurrentlyMining = Rocks[1];
						MineNext = Rocks[0];
						state = ScriptState.MINING;
						
						if(AntiBan.BOOL_TRACKER.HOVER_NEXT.next()){
							
							sleep(AntiBan.DELAY_TRACKER.SWITCH_OBJECT.next() + 200);
							AntiBan.DELAY_TRACKER.SWITCH_OBJECT.reset();
							Rocks[0].hover(new Point(General.random(-13, 10),General.random(-9, 5)), new Point(0,0));
							AntiBan.BOOL_TRACKER.HOVER_NEXT.reset();
							
						}else{
							
							sleep(AntiBan.DELAY_TRACKER.SWITCH_OBJECT.next() + 200);
							AntiBan.DELAY_TRACKER.SWITCH_OBJECT.reset();
							Rocks[1].hover(new Point(General.random(-13, 10),General.random(-9, 5)), new Point(0,0));
							
						}
						
					}
					
				}else{
					
					if(WebWalking.walkTo(Rocks[1].getPosition())){
						
						sleep(100, 300);
						
						if(Rocks[1].getModel().getVertexCount() < 150 && DynamicClicking.clickRSModel(Rocks[1].getModel(), "Mine")){
							
							CurrentlyMining = Rocks[1];
							MineNext = Rocks[0];
							state = ScriptState.MINING;
							
							if(AntiBan.BOOL_TRACKER.HOVER_NEXT.next()){
								
								sleep(AntiBan.DELAY_TRACKER.SWITCH_OBJECT.next() + 200);
								AntiBan.DELAY_TRACKER.SWITCH_OBJECT.reset();
								Rocks[0].hover(new Point(General.random(-13, 10),General.random(-9, 5)), new Point(0,0));
								AntiBan.BOOL_TRACKER.HOVER_NEXT.reset();
								
							}else{
								
								sleep(AntiBan.DELAY_TRACKER.SWITCH_OBJECT.next() + 200);
								AntiBan.DELAY_TRACKER.SWITCH_OBJECT.reset();
								Rocks[1].hover(new Point(General.random(-13, 10),General.random(-9, 5)), new Point(0,0));
								
							}
							
						}
						
					}
					
				}
				
			}
			
		}
		
	}

	private void WalkToMine() {

		if(WebWalking.walkTo(StartTile)){
			
			state = ScriptState.IDLE;
			
		}
		
	}

	private void Drop() {

		Inventory.dropAllExcept(PICKAXES);
		InventoryCount = 0;
		
	}

	private void Bank() {

		if(WebWalking.walkToBank()){
			
			if(Banking.openBank()){
				
				if(Banking.isBankScreenOpen()){
					
					Banking.depositAllExcept(PICKAXES);
					sleep(100,400);
					Banking.close();
					sleep(AntiBan.DELAY_TRACKER.NEW_OBJECT.next());
					AntiBan.DELAY_TRACKER.NEW_OBJECT.reset();
					InventoryCount = Inventory.getCount(OreNames);
					WalkToMine();
					
					
				}else{
					
					sleep(AntiBan.DELAY_TRACKER.NEW_OBJECT.next());
					AntiBan.DELAY_TRACKER.NEW_OBJECT.reset();
					WalkToMine();
					
				}
				
			}
			
		}
		
	}
	
	public boolean containsID(int Id){
		
		if(SelectedOres != null && SelectedOres.length > 0){
			
			for(int i : SelectedOres){
				
				if(i == Id){return true;}
				
			}
			
			return false;
			
		}else{return false;}
		
	}
	
	public int[] SetToArray(Set<Integer> SetInt){
		
		int Count = 0;
		int[] ArrayInt = new int[SetInt.size()];
		
		for(Integer i : SetInt){
			
			ArrayInt[Count] = i;
			Count++;
			
		}
		
		return ArrayInt;
		
	}
	
	private boolean isMining(){
		for(int i : ANIMATIONS){
			if(Player.getAnimation() == i){
				return true;
			}
		}
		return false;
	}
	
	private void stairFailsafe() {
        final RSObject[] stairs = Objects.findNearest(10, 2162);
        if(stairs.length>0){
                if(stairs[0].isOnScreen()){
                        stairs[0].hover(new Point(General.random(-13, 10),General.random(-9, 5)), new Point(null));
                        Timing.waitCondition(new Condition(){

                                @Override
                                public boolean active() {
                                        return Game.getUptext().contains("Stairs");
                                }
                               
                        }, 691);
                        if(Game.getUptext().contains("Stairs")){
                                Mouse.click(1);
                        }
                        Timing.waitCondition(new Condition(){

                                @Override
                                public boolean active() {
                                        return !stairs[0].isOnScreen();
                                }
                               
                        }, 2861);
                }
        }
       
}
	
	public void updateOres(){
		
		if(!GUI_ISCOMPLETE && Login.getLoginState() == STATE.INGAME){
			
			RSObject[] Objs = Objects.getAll(30);
			Set<Integer> IDs = new HashSet<>();
			
			for(RSObject obj : Objs){
				
				if(obj.getDefinition().getName().equalsIgnoreCase("Rocks")){
					
					IDs.add(obj.getID());
					
				}
				
			}
			if(IDs.size() > 0){
				
				gui.getList_Ores().removeAll();
				
				for(Integer i : IDs){
					
					gui.getList_Ores().add("" + i);
				
				}
				
			}
		}
		
	}

		

}
