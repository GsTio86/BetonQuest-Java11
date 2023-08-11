package org.betonquest.betonquest.quest.event.random;

import org.betonquest.betonquest.VariableNumber;
import org.betonquest.betonquest.id.EventID;


public class ResolvedRandomEvent {
    public final EventID eventID;
    public final double chance;
    /**
     * Represents an event with its chance.
     *
     * @param eventID the event to be executed
     * @param chance  the resolved chance of the event
     */
    public ResolvedRandomEvent(EventID eventID, double chance) {
        this.eventID = eventID;
        this.chance = chance;
    }
}
