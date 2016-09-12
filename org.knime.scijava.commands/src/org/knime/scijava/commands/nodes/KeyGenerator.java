package org.knime.scijava.commands.nodes;

import org.knime.core.data.RowKey;

/**
 * Interface for classes able to create {@link RowKey}s.
 *
 * @author Christian Dietz, University of Konstanz
 */
public interface KeyGenerator {

    /**
     * Create a new {@link RowKey}.
     *
     * @return the created {@link RowKey}
     */
    public RowKey create();

    /**
     * Set current key. All following calls to {@link #create()} create keys
     * sequential to the current key.
     *
     * @param curr
     *            current row key
     */
    public void setCurrentKey(final RowKey curr);

}
