package org.betonquest.betonquest.quest.event.journal;

import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.logger.BetonQuestLoggerFactory;
import org.betonquest.betonquest.api.quest.event.Event;
import org.betonquest.betonquest.api.quest.event.EventFactory;
import org.betonquest.betonquest.api.quest.event.StaticEvent;
import org.betonquest.betonquest.api.quest.event.StaticEventFactory;
import org.betonquest.betonquest.database.Saver;
import org.betonquest.betonquest.database.UpdateType;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.quest.event.*;
import org.betonquest.betonquest.utils.PlayerConverter;
import org.betonquest.betonquest.utils.Utils;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Locale;

/**
 * Factory to create journal events from {@link Instruction}s.
 */
public class JournalEventFactory implements EventFactory, StaticEventFactory {
    /**
     * Logger factory to create a logger for events.
     */
    private final BetonQuestLoggerFactory loggerFactory;

    /**
     * BetonQuest instance to provide to events.
     */
    private final BetonQuest betonQuest;

    /**
     * The instant source to provide to events.
     */
    private final Instant instantSource;

    /**
     * The saver to inject into database-using events.
     */
    private final Saver saver;

    /**
     * Create the journal event factory.
     *
     * @param loggerFactory           logger to pass on
     * @param betonQuest    BetonQuest instance to pass on
     * @param instantSource instant source to pass on
     * @param saver         database saver to use
     */
    public JournalEventFactory(final BetonQuestLoggerFactory loggerFactory, final BetonQuest betonQuest, final Instant instantSource, final Saver saver) {
        this.loggerFactory = loggerFactory;
        this.betonQuest = betonQuest;
        this.instantSource = instantSource;
        this.saver = saver;
    }

    @Override
    public Event parseEvent(final Instruction instruction) throws InstructionParseException {
        final String action = instruction.next();
        switch (action.toLowerCase(Locale.ROOT)) {
            case "update": return createJournalUpdateEvent();
            case "add": return createJournalAddEvent(instruction);
            case "delete": return createJournalDeleteEvent(instruction);
            default: throw new InstructionParseException("Unknown journal action: " + action);
        }
    }

    @Override
    public StaticEvent parseStaticEvent(final Instruction instruction) throws InstructionParseException {
        final String action = instruction.next();
        switch (action.toLowerCase(Locale.ROOT)) {
            case "update":
            case "add": return new DoNothingStaticEvent();
            case "delete": return createStaticJournalDeleteEvent(instruction);
            default: throw new InstructionParseException("Unknown journal action: " + action);
        }
    }

    @NotNull
    private JournalEvent createJournalDeleteEvent(final Instruction instruction) throws InstructionParseException {
        final String entryName = Utils.addPackage(instruction.getPackage(), instruction.getPart(2));
        final JournalChanger journalChanger = new RemoveEntryJournalChanger(entryName);
        final NotificationSender notificationSender = new NoNotificationSender();
        return new JournalEvent(betonQuest, journalChanger, notificationSender);
    }

    @NotNull
    private JournalEvent createJournalAddEvent(final Instruction instruction) throws InstructionParseException {
        final String entryName = Utils.addPackage(instruction.getPackage(), instruction.getPart(2));
        final JournalChanger journalChanger = new AddEntryJournalChanger(instantSource, entryName);
        final NotificationSender notificationSender = new IngameNotificationSender(loggerFactory.create(JournalEvent.class), instruction.getPackage(), instruction.getID().getFullID(), NotificationLevel.INFO, "new_journal_entry");
        return new JournalEvent(betonQuest, journalChanger, notificationSender);
    }

    @NotNull
    private JournalEvent createJournalUpdateEvent() {
        final JournalChanger journalChanger = new NoActionJournalChanger();
        final NotificationSender notificationSender = new NoNotificationSender();
        return new JournalEvent(betonQuest, journalChanger, notificationSender);
    }

    @NotNull
    private StaticEvent createStaticJournalDeleteEvent(final Instruction instruction) throws InstructionParseException {
        final JournalEvent journalDeleteEvent = createJournalDeleteEvent(instruction.copy());
        final String entryName = Utils.addPackage(instruction.getPackage(), instruction.getPart(2));
        return new SequentialStaticEvent(
                new OnlineProfileGroupStaticEventAdapter(PlayerConverter::getOnlineProfiles, journalDeleteEvent),
                new DatabaseSaverStaticEvent(saver, () -> new Saver.Record(UpdateType.REMOVE_ALL_ENTRIES, entryName))
        );
    }
}
