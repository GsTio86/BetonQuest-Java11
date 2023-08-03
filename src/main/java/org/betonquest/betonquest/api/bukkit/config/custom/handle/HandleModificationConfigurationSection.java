package org.betonquest.betonquest.api.bukkit.config.custom.handle;

import org.betonquest.betonquest.api.bukkit.config.custom.ConfigurationSectionDecorator;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * This is an extension of the {@link ConfigurationSectionDecorator},
 * that handles all {@link ConfigurationSection} methods that do modifications.
 */
public class HandleModificationConfigurationSection extends ConfigurationSectionDecorator {

    /**
     * The {@link ConfigurationSectionModificationHandler} instance
     */
    protected final ConfigurationSectionModificationHandler handler;

    /**
     * Creates a new handler instance.
     *
     * @param original The original {@link ConfigurationSection} in which modifications will be handled
     * @param handler  The {@link ConfigurationSectionModificationHandler} that handles modifications
     */
    public HandleModificationConfigurationSection(final ConfigurationSection original, final ConfigurationSectionModificationHandler handler) {
        super(original);
        this.handler = handler;
    }

    @NotNull
    @Override
    public Map<String, Object> getValues(final boolean deep) {
        final Map<String, Object> values = original.getValues(deep);
        values.replaceAll((k, v) -> wrapModifiable(v));
        return values;
    }

    @Override
    public @Nullable
    Configuration getRoot() {
        return (Configuration) wrapModifiable(original.getRoot());
    }

    @Override
    public @Nullable
    ConfigurationSection getParent() {
        return (ConfigurationSection) wrapModifiable(original.getParent());
    }

    @Override
    public @Nullable
    Object get(@NotNull final String path) {
        return wrapModifiable(original.get(path));
    }

    @Override
    public @Nullable
    Object get(@NotNull final String path, @Nullable final Object def) {
        return wrapModifiable(original.get(path, def));
    }

    @Override
    public void set(@NotNull final String path, @Nullable final Object value) {
        handler.set(original, path, value);
    }

    @Override
    public @NotNull
    ConfigurationSection createSection(@NotNull final String path) {
        return handler.createSection(original, path);
    }

    @Override
    public @NotNull
    ConfigurationSection createSection(@NotNull final String path, @NotNull final Map<?, ?> map) {
        return handler.createSection(original, path, map);
    }

    @Override
    public <T> T getObject(@NotNull final String path, @NotNull final Class<T> clazz) {
        return clazz.cast(wrapModifiable(original.getObject(path, clazz)));
    }

    @Override
    public <T> T getObject(@NotNull final String path, @NotNull final Class<T> clazz, @Nullable final T def) {
        return clazz.cast(wrapModifiable(original.getObject(path, clazz, def)));
    }

    @Override
    public @Nullable
    ConfigurationSection getConfigurationSection(@NotNull final String path) {
        return (ConfigurationSection) wrapModifiable(original.getConfigurationSection(path));
    }

    @Override
    public @Nullable
    ConfigurationSection getDefaultSection() {
        return (ConfigurationSection) wrapModifiable(original.getDefaultSection());
    }

    @Override
    public void addDefault(@NotNull final String path, @Nullable final Object value) {
        handler.addDefault(original, path, value);
    }

    /**
     * Wraps a given object into an instance of this class.
     *
     * @param obj the raw object
     * @return the wrapped object
     */
    protected Object wrapModifiable(final Object obj) {
        if (obj instanceof HandleModificationConfigurationSection) {
            return obj;
        }
        if (obj instanceof Configuration) {
            return new HandleModificationConfiguration((Configuration) obj, (ConfigurationModificationHandler) handler);
        }
        if (obj instanceof ConfigurationSection) {
            return new HandleModificationConfigurationSection((ConfigurationSection) obj, handler);
        }
        return obj;
    }
}
