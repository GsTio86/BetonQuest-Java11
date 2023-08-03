package org.betonquest.betonquest.database;

/**
 * Key for the migration with the information about the name and version.
 *
 * @param namespace name of the plugin
 * @param version   version of the migration
 */
public class MigrationKey implements Comparable<MigrationKey> {
    private final String namespace;
    private final int version;

    public MigrationKey(String namespace, int version) {
        this.namespace = namespace;
        this.version = version;
    }

    public String getNamespace() {
        return namespace;
    }

    public int getVersion() {
        return version;
    }

    @Override
    public int compareTo(final MigrationKey key) {
        if (this.namespace.equals(key.namespace)) {
            return Integer.compare(this.version, key.version);
        }
        return this.namespace.compareTo(key.namespace);
    }
}