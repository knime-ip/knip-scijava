package org.knime.scijava.commands;

/**
 * Interface for classes which listen to pushes of output rows.
 *
 * @author Christian Dietz, University of Konstanz
 * @see CellOutput
 */
public interface MultiOutputListener {

    /**
     * Notify this listener that a new output row has been pushed.
     * @throws Exception
     */
    public void notifyListener() throws Exception;
}
