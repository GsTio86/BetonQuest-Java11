package org.betonquest.betonquest.quest.event.weather;

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.common.function.ConstantSelector;
import org.betonquest.betonquest.api.common.function.Selector;
import org.betonquest.betonquest.api.common.function.Selectors;
import org.betonquest.betonquest.api.logger.BetonQuestLoggerFactory;
import org.betonquest.betonquest.api.quest.event.Event;
import org.betonquest.betonquest.api.quest.event.EventFactory;
import org.betonquest.betonquest.api.quest.event.StaticEvent;
import org.betonquest.betonquest.api.quest.event.StaticEventFactory;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.quest.event.DoNothingStaticEvent;
import org.betonquest.betonquest.quest.event.NullStaticEventAdapter;
import org.betonquest.betonquest.quest.event.OnlineProfileRequiredEvent;
import org.betonquest.betonquest.quest.event.PrimaryServerThreadEvent;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * Factory to create weather events from {@link Instruction}s.
 */
public class WeatherEventFactory implements EventFactory, StaticEventFactory {
    /**
     * Logger factory to create a logger for events.
     */
    private final BetonQuestLoggerFactory loggerFactory;

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
     * Creates the weather event factory.
     *
     * @param loggerFactory       logger to use
     * @param server    server to use
     * @param scheduler scheduler to use
     * @param plugin    plugin to use
     */
    public WeatherEventFactory(final BetonQuestLoggerFactory loggerFactory, final Server server, final BukkitScheduler scheduler, final Plugin plugin) {
        this.loggerFactory = loggerFactory;
        this.server = server;
        this.scheduler = scheduler;
        this.plugin = plugin;
    }

    @Override
    public Event parseEvent(final Instruction instruction) throws InstructionParseException {
        final Weather weather = parseWeather(instruction.next());
        final Selector<World> worldSelector = parseWorld(instruction.getOptional("world"));
        return new PrimaryServerThreadEvent(
                new OnlineProfileRequiredEvent(
                    loggerFactory.create(WeatherEvent.class), new WeatherEvent(weather, worldSelector), instruction.getPackage()),
            server, scheduler, plugin);
    }

    @Override
    public StaticEvent parseStaticEvent(final Instruction instruction) throws InstructionParseException {
        if (instruction.copy().getOptional("world") == null) {
            return new DoNothingStaticEvent();
        } else {
            return new NullStaticEventAdapter(parseEvent(instruction));
        }
    }

    @NotNull
    private Weather parseWeather(final String weatherName) throws InstructionParseException {
        switch (weatherName.toLowerCase(Locale.ROOT)) {
            case "clear": return Weather.SUN;
            case "sun": return Weather.SUN;
            case "rain": return Weather.RAIN;
            case "storm": return Weather.STORM;
            default: throw new InstructionParseException("Unknown weather state (valid options are: sun, clear, rain, storm): " + weatherName);
        }
    }

    @NotNull
    private Selector<World> parseWorld(final String worldName) {
        if (worldName == null) {
            return Selectors.fromPlayer(Player::getWorld);
        } else {
            final World world = server.getWorld(worldName);
            return new ConstantSelector<>(world);
        }
    }
}
