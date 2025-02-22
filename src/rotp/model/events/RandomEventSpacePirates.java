/*
 * Copyright 2015-2020 Ray Fowler
 * 
 * Licensed under the GNU General Public License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     https://www.gnu.org/licenses/gpl-3.0.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rotp.model.events;

import java.io.Serializable;

import rotp.model.colony.Colony;
import rotp.model.empires.Empire;
import rotp.model.galaxy.SpacePirates;
import rotp.model.galaxy.StarSystem;
import rotp.model.game.IGameOptions;
import rotp.ui.notifications.GNNNotification;
import rotp.util.Base;

// modnar: add Space Pirates random event
public class RandomEventSpacePirates implements Base, Serializable, RandomEvent {
    private static final long serialVersionUID = 1L;
    private static final String NEXT_ALLOWED_TURN = "PIRATES_NEXT_ALLOWED_TURN";
    public static SpacePirates monster;
    private int empId;
    private int sysId;
    private int turnCount = 0;
    private transient Integer nextAllowedTurn;
    
    static {
        initMonster();
    }
    @Override
    public String statusMessage()       { return text("SYSTEMS_STATUS_SPACE_PIRATES"); }
    @Override
    public String systemKey()           { return "MAIN_PLANET_EVENT_PIRATES"; }
    @Override
    public boolean goodEvent()    		{ return false; }
    @Override // modnar: Space Pirates are repeatable // BR: Player Choice
    public boolean repeatable()    		{ return options().selectedPiratesReturnTurn() != 0; } 
    @Override
    public boolean monsterEvent()       { return true; } // modnar: make into monsterEvent
    @Override
    public int minimumTurn() {
      	int turn = 0;
      	 		// modnar: Space Pirates can show up earlier than other space monsters, later than regular random events
		// but the space pirate ship stack stats will be based on galaxy empire development
      	int piratesDelayTurn = options().selectedPiratesDelayTurn();
		switch (options().selectedGameDifficulty()) { // BR: added adjustable delay
            case IGameOptions.DIFFICULTY_EASIEST:
            	turn = RandomEvents.START_TURN + 4 * piratesDelayTurn;
            	break;
            case IGameOptions.DIFFICULTY_EASIER:
            	turn = RandomEvents.START_TURN + 3 * piratesDelayTurn;
            	break;
            case IGameOptions.DIFFICULTY_EASY:
            	turn = RandomEvents.START_TURN + 2 * piratesDelayTurn;
            	break;
            default:
            	turn = RandomEvents.START_TURN + piratesDelayTurn;
        }
//      System.out.println("Space Pirates next Turn = " + max(turn, nextAllowedTurn()) +
//		" (current turn = " + galaxy().currentTurn() + ")");
return max(turn, nextAllowedTurn()); // BR: To allow repeatable event
	}
    @Override
    public String notificationText()    {
        String s1 = text("EVENT_SPACE_PIRATES");
        s1 = s1.replace("[system]", galaxy().empire(empId).sv.name(sysId));
        return s1;
    }
    @Override
    public void trigger(Empire emp) {
        log("Starting Pirates event against: "+emp.raceName());
//      System.out.println("Starting Pirates event against: "+emp.raceName());
        StarSystem targetSystem = random(emp.allColonizedSystems());
        empId = emp.id;
        sysId = targetSystem.id;
        turnCount = 3;
        galaxy().events().addActiveEvent(this);
    }
    @Override
    public void nextTurn() {
        if (!monsterAllowed()) {
            galaxy().events().removeActiveEvent(this);
            return;
        }
        if (turnCount == 3) 
            approachSystem();     
        else if (turnCount == 0) 
            enterSystem();
        turnCount--;
    }
    private boolean monsterAllowed() {
    	return options().selectedRandomEventOption().equals(IGameOptions.RANDOM_EVENTS_ON);
    }
    private int nextAllowedTurn() {
    	if (nextAllowedTurn == null)
    		nextAllowedTurn = (Integer) galaxy().dynamicOptions().getInteger(NEXT_ALLOWED_TURN, -1);
    	return nextAllowedTurn;
    }
    private void nextAllowedTurn(Integer turn) {
    	nextAllowedTurn = turn;
    	galaxy().dynamicOptions().setInteger(NEXT_ALLOWED_TURN, nextAllowedTurn);
    }
    private boolean nextSystemAllowed() { // BR: To allow disappearance
        if (!options().selectedRandomEventOption().equals(IGameOptions.RANDOM_EVENTS_ON))
            return false;
    	int maxSystem = options().selectedPiratesMaxSystems();
        return maxSystem == 0 || maxSystem > monster.vistedSystemsCount();
    }
     private static void initMonster() {
        monster = new SpacePirates();
    }
   private void enterSystem() {
        monster.visitSystem(sysId);
        monster.initCombat();
        StarSystem targetSystem = galaxy().system(sysId);
        targetSystem.clearEvent();
        Colony col = targetSystem.colony();
        if (!targetSystem.orbitingFleets().isEmpty())
            startCombat();
        else if ((col != null) && col.defense().isArmed())
            startCombat();
        
        if (monster.alive()) {
            if (col != null)
				// modnar: pirates pillage colonies rather than destroy
                pillageColony(col);
            if (nextSystemAllowed())
            	moveToNextSystem();
            else
            	monsterVanished();
        }
        else 
            piratesDestroyed();         
    }
    private void startCombat() {
        StarSystem targetSystem = galaxy().system(sysId);
        galaxy().shipCombat().battle(targetSystem, monster);
    }
    private void approachSystem() {
        StarSystem targetSystem = galaxy().system(sysId);
        targetSystem.eventKey(systemKey());
        Empire pl = player();
        if (targetSystem.isColonized()) { 
            if (pl.knowsOf(targetSystem.empire()) || !pl.sv.name(sysId).isEmpty())
                GNNNotification.notifyRandomEvent(notificationText("EVENT_SPACE_PIRATES", targetSystem.empire()), "GNN_Event_Pirates");
        }
        else if (pl.sv.isScouted(sysId))
            GNNNotification.notifyRandomEvent(notificationText("EVENT_SPACE_PIRATES_1", null), "GNN_Event_Pirates");   
    }
    private void pillageColony(Colony col) {
        StarSystem targetSystem = galaxy().system(sysId);  
        // colony may have already been destroyed in combat
        if (targetSystem.isColonized()) 
            monster.pillageColony(targetSystem);
        
        Empire pl = player();
        if (pl.knowsOf(col.empire()) || !pl.sv.name(sysId).isEmpty())
            GNNNotification.notifyRandomEvent(notificationText("EVENT_SPACE_PIRATES_2", col.empire()), "GNN_Event_Pirates");
    }
    private void piratesDestroyed() {
        galaxy().events().removeActiveEvent(this);
        
        monster.plunder();
        
        // destroying the space pirates gives reserve BC, scaling with turn number
        Empire heroEmp = monster.lastAttacker();
        int turnNum = galaxy().currentTurn();
        int spoilsBC = turnNum * 25;
        heroEmp.addToTreasury(spoilsBC);

        if (player().knowsOf(galaxy().empire(empId)) || !player().sv.name(sysId).isEmpty())
            GNNNotification.notifyRandomEvent(notificationText("EVENT_SPACE_PIRATES_3", monster.lastAttacker()), "GNN_Event_Pirates");
    }
    private void monsterVanished() { // BR: To allow disappearance
    	terminateEvent();
        if (player().knowsOf(galaxy().empire(empId)) || !player().sv.name(sysId).isEmpty())
            GNNNotification.notifyRandomEvent(notificationText("EVENT_SPACE_PIRATES_4", monster.lastAttacker()), "GNN_Event_Pirates");
    }
    private void terminateEvent() { // BR: To allow repeatable event
   		galaxy().events().removeActiveEvent(this);
    	if(repeatable())
    		nextAllowedTurn(galaxy().currentTurn() + options().selectedPiratesReturnTurn());
    }
    private void moveToNextSystem() {
        if (!options().selectedRandomEventOption().equals(IGameOptions.RANDOM_EVENTS_ON)) {
                log("ERR: Pirate Event No more allowed.");
                galaxy().events().removeActiveEvent(this);
                return;
            }

        StarSystem targetSystem = galaxy().system(sysId);
        // next system is one of the 10 nearest systems
        // more likely to go to new system (25%) than visited system (5%)
		// more likely to go to colony with a lot of factories (factories/2000 = additional chance)
		// increase chance with more loops (essentially try to force a choice before loops>10)
        int[] near = targetSystem.nearbySystems();
        boolean stopLooking = false;
        
        int nextSysId = -1;
        int loops = 0;
        if (near.length > 0) {
            while (!stopLooking) {
                loops++;
                for (int i=0;i<near.length;i++) {
					if (galaxy().system(near[i]).isColonized()) { // check for colony
						float chance = monster.vistedSystems().contains(near[i]) ? 0.05f : 0.25f;
						// more likely to go to colony with a lot of factories
						chance += galaxy().system(near[i]).colony().industry().factories()/2000.0f;
						// increase chance with more loops
						chance += (float)(loops/20);
						if (random() < chance) {
							nextSysId = near[i];
							stopLooking = true;
							break;
						}
                    }
                }
                if (loops > 10)
                    stopLooking = true;
            }
        }
        
        if (nextSysId < 0) {
            log("ERR: Could not find next system. Space Pirates removed.");
            galaxy().events().removeActiveEvent(this);
            return;
        }
    
        log("Space Pirates moving to system: "+nextSysId);
        StarSystem nextSys = galaxy().system(nextSysId);
        float slowdownEffect = max(1, 100.0f / galaxy().maxNumStarSystems());
        turnCount = (int) Math.ceil(1.5*slowdownEffect*nextSys.distanceTo(targetSystem));
        sysId = nextSys.id;
        if (turnCount <= 3)
            approachSystem();
    }
    private String notificationText(String key, Empire emp)    {
        String s1 = text(key);
        if (emp != null) {
            s1 = s1.replace("[system]", emp.sv.name(sysId));
            s1 = s1.replace("[race]", emp.raceName());
        }
        else 
            s1 = s1.replace("[system]", player().sv.name(sysId));
        return s1;
    }
}
