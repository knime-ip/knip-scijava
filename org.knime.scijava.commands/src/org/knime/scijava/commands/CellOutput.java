package org.knime.scijava.commands;

import org.knime.core.data.DataCell;

/**
 * Interface for classes which are able to receive cells as output.
 *
 * @author Christian Dietz, University of Konstanz
 */
public interface CellOutput {

    /**
     * Push a new row of output.
     *
     * @param cells
     *            The cells which make the output row
     */
    void push(final DataCell[] cells);
}
