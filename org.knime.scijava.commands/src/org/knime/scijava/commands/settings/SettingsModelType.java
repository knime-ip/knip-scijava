package org.knime.scijava.commands.settings;

import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.scijava.plugin.SingletonPlugin;

/**
 * Interface for SettingsModelTypes which set and get values from
 * {@link SettingsModel}s as Objects.
 *
 * This is a workaround for not being able to set values of a SettingsModel
 * without explicit SettingsModel instances.
 *
 * @author Jonathan Hale (University of Konstanz)
 *
 * @param <T>
 *            Type of the SettingsModel.
 * @param <V>
 *            Type of the Value to be set/get.
 *
 *            TODO: This sadly doesn't do the job. Sometimes it is not possible
 *            to set set value just from Object.
 */
public interface SettingsModelType<T extends SettingsModel, V>
        extends SingletonPlugin {

    /**
     * Create a new SettingsModel instance with type described by this
     * SettingsModelType.
     *
     * @param name
     *            Name of the SettingsModel.
     * @param defaultValue
     *            DefaultValue for this SettingsModel
     * @return
     */
    public T create(String name, V defaultValue);

    /**
     * Set the value of a SettingsModel.
     *
     * @param settingsModel
     *            SettingsModel to set the value of.
     * @param value
     *            value to set to.
     */
    public void setValue(T settingsModel, V value);

    /**
     * Get the value of a SettingsModel.
     *
     * @param settingsModel
     *            SettingsModel to get the value of.
     * @return the value of the SettingsModel.
     */
    public V getValue(T settingsModel);

    /**
     * Get the SettingsModel class this SettingsModelType can set and get values
     * of.
     *
     * @return a SettingsModel subclass.
     */
    public Class<T> getSettingsModelClass();

    /**
     * Get the class of the value which can be set to/get from the
     * SettingsModel.
     *
     * @return class of the value.
     */
    public Class<V> getValueClass();
}