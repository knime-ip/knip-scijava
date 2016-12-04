package org.knime.scijava.commands;

import org.knime.scijava.commands.widget.ColumnSelectionWidget;
import org.scijava.plugin.Parameter;

/**
 * StyleHook constants for {@link Parameter#style()}.
 *
 * @author Christian Dietz, University of Konstanz
 */
public interface StyleHook {
    /**
     * Force the parameter to be filled by a input column. Will result in the
     * {@link ColumnSelectionWidget} being used as for it.
     */
    public final static String COLUMNSELECTION = "colSel";
}
