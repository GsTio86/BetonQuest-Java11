package org.betonquest.betonquest.modules.schedule;

import org.betonquest.betonquest.api.config.ConfigAccessor;
import org.betonquest.betonquest.api.logger.BetonQuestLogger;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

/**
 * Cache that holds the last execution time of schedules.
 * Only one instance may exist.
 */
public class LastExecutionCache {
    /**
     * The File where last executions should be cached.
     */
    public static final String CACHE_FILE = ".cache/schedules.yml";

    /**
     * Custom {@link BetonQuestLogger} instance for this class.
     */
    private final BetonQuestLogger log;

    /**
     * Config accessor for the cache.
     */
    private ConfigAccessor cache;

    /**
     * Create a new execution cache instance for a given schedule.
     *
     * @param log        the logger that will be used for logging
     * @param dataFolder the BetonQuest data folder
     */
    public LastExecutionCache(final BetonQuestLogger log, final File dataFolder) {
        this.log = log;
        try {
            final Path cacheFile = new File(dataFolder, CACHE_FILE).toPath();
            if (!Files.exists(cacheFile)) {
                Files.createDirectories(Optional.ofNullable(cacheFile.getParent()).orElseThrow());
                Files.createFile(cacheFile);
            }
            this.cache = ConfigAccessor.create(cacheFile.toFile());
            this.log.debug("Successfully loaded schedule cache.");
        } catch (final IOException | InvalidConfigurationException e) {
            this.log.error("Error while loading schedule cache: " + e.getMessage(), e);
        }
    }

    /**
     * reload execution cache in case it was modified externally.
     */
    public void reload() {
        try {
            if (cache == null) {
                log.error("Schedule cache not present!");
            } else {
                cache.reload();
                log.debug("Successfully reloaded schedule cache.");
            }
        } catch (final IOException e) {
            log.error("Could not reload schedule cache: " + e.getMessage(), e);
        }
    }

    /**
     * Save the last execution time of a schedule as raw string to the cache.
     *
     * @param schedule id of the schedule
     * @param rawTime  raw string to cache
     */
    public void cacheRawExecutionTime(final ScheduleID schedule, final String rawTime) {
        if (cache == null) {
            log.error("Schedule cache not present!");
            return;
        }
        cache.getConfig().set(schedule.getFullID(), rawTime);
        try {
            cache.save();
        } catch (final IOException e) {
            log.error("Could not save schedule cache: " + e.getMessage(), e);
        }
    }

    /**
     * Save the last execution time of a schedule to the cache in ISO-8601 format
     * (see {@link DateTimeFormatter#ISO_INSTANT}).
     *
     * @param schedule id of the schedule
     * @param time     time to cache as instant
     */
    public void cacheExecutionTime(final ScheduleID schedule, final Instant time) {
        cacheRawExecutionTime(schedule, time.toString());
    }

    /**
     * Get the cached last execution time of a schedule as raw string.
     *
     * @param schedule id of the schedule
     * @return optional containing the cached string, empty if nothing was cached
     */
    public Optional<String> getRawLastExecutionTime(final ScheduleID schedule) {
        if (cache == null) {
            log.error("Schedule cache not present!");
            return Optional.empty();
        }
        return Optional.ofNullable(cache.getConfig().getString(schedule.getFullID()));
    }

    /**
     * Get the cached last execution time of a schedule as Instant.
     *
     * @param schedule id of the schedule
     * @return optional containing the last execution time, empty if no execution time is cached
     * @throws DateTimeParseException if time couldn't be parsed using {@link DateTimeFormatter#ISO_INSTANT}
     */
    public Optional<Instant> getLastExecutionTime(final ScheduleID schedule) {
        return getRawLastExecutionTime(schedule).map(Instant::parse);
    }

    /**
     * Check if the execution time of a schedule was cached.
     *
     * @param scheduleID id of the schedule
     * @return true if cache contains time for that schedule, false otherwise
     */
    public boolean isCached(final ScheduleID scheduleID) {
        return getRawLastExecutionTime(scheduleID).isPresent();
    }

}
