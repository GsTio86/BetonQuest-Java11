/**
 * BetonQuest - advanced quests for Bukkit
 * Copyright (C) 2015  Jakub "Co0sh" Sapalski
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package pl.betoncraft.betonquest.conditions;

import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.api.Condition;
import pl.betoncraft.betonquest.core.Pointer;
import pl.betoncraft.betonquest.utils.Debug;

/**
 * Checks if the player has specified pointer in his journal
 * 
 * @author Coosh
 */
public class JournalCondition extends Condition {
    
    private String targetPointer = null;

    public JournalCondition(String playerID, String packName, String instructions) {
        super(playerID, packName, instructions);
        String[] parts = instructions.split(" ");
        if (parts.length < 2) {
            Debug.error("Journal entry not defined in journal condition: " + instructions);
            isOk = false;
            return;
        }
        targetPointer = parts[1];
    }
    
    @Override
    public boolean isMet() {
        if (!isOk) {
            Debug.error("There was an error, returning false.");
            return false;
        }
        for (Pointer pointer : BetonQuest.getInstance().getDBHandler(playerID).getJournal().getPointers()) {
            if (pointer.getPointer().equalsIgnoreCase(targetPointer)) {
                return true;
            }
        }
        return false;
    }
}