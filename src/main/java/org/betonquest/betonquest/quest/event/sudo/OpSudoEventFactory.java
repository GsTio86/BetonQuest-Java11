package org.betonquest.betonquest.quest.event.sudo;

import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.logger.BetonQuestLogger;
import org.betonquest.betonquest.api.quest.event.Event;
import org.betonquest.betonquest.api.quest.event.EventFactory;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.quest.event.OnlineProfileRequiredEvent;
import org.betonquest.betonquest.quest.event.PrimaryServerThreadEvent;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * Creates a new OpSudoEvent from an {@link Instruction}.
 */
public class OpSudoEventFactory implements EventFactory {

    /**
     * Custom {@link BetonQuestLogger} instance for this class.
     */
    private final BetonQuestLogger log;

    /**
     * Server to use for syncing to the primary server thread.
     */
    private final Server server;

    /**
     * Scheduler to use for syncing to the primary server thread.
     */
    private final BukkitScheduler scheduler;

    /**
     * Plugin to use for syncing to the primary server thread.
     */
    private final Plugin plugin;

    /**
     * Create the OpSudoEvent factory.
     *
     * @param log       the logger to use
     * @param server    server to use
     * @param scheduler scheduler scheduler to use
     * @param plugin    plugin to use
     */
    public OpSudoEventFactory(final BetonQuestLogger log, final Server server, final BukkitScheduler scheduler, final Plugin plugin) {
        this.log = log;
        this.server = server;
        this.scheduler = scheduler;
        this.plugin = plugin;
    }

    @Override
    public Event parseEvent(final Instruction instruction) throws InstructionParseException {
        final Command[] commands;
        final String string = instruction.getInstruction().trim();
        int index = string.indexOf("conditions:");
        index = index == -1 ? string.length() : index;
        final String[] rawCommands = string.substring(string.indexOf(' ') + 1, index).split("\\|");

        commands = new Command[rawCommands.length];
        for (int i = 0; i < rawCommands.length; i++) {
            commands[i] = new Command(rawCommands[i], BetonQuest.resolveVariables(rawCommands[i]));
        }
        return new PrimaryServerThreadEvent(
            new OnlineProfileRequiredEvent(
                log, new OpSudoEvent(commands, instruction.getPackage()), instruction.getPackage()),
            server, scheduler, plugin);
    }
}