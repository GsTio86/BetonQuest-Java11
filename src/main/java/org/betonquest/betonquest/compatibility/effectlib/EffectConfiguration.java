package org.betonquest.betonquest.compatibility.effectlib;

import org.betonquest.betonquest.id.ConditionID;
import org.betonquest.betonquest.utils.location.CompoundLocation;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Set;


public class EffectConfiguration {
    public final String effectClass;
    public final List<CompoundLocation> locations;
    public final Set<Integer> npcs;
    public final List<ConditionID> conditions;
    public final ConfigurationSection settings;
    public final Integer conditionCheckInterval;

    /**
     * @param effectClass            the EffectLib effectClass
     * @param locations              the locations in the configurationSection
     * @param npcs                   the npcs in the configurationSection
     * @param conditions             the conditions when the effect should be shown
     * @param settings               the whole configuration settings
     * @param conditionCheckInterval the interval when the conditions should be checked
     */
    public EffectConfiguration(String effectClass, List<CompoundLocation> locations, Set<Integer> npcs,
                               List<ConditionID> conditions, ConfigurationSection settings,
                               Integer conditionCheckInterval) {
        this.effectClass = effectClass;
        this.locations = locations;
        this.npcs = npcs;
        this.conditions = conditions;
        this.settings = settings;
        this.conditionCheckInterval = conditionCheckInterval;
    }
}
