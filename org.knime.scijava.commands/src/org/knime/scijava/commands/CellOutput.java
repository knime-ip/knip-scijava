package org.knime.scijava.commands;

import org.knime.core.data.DataCell;

public interface CellOutput {
    void push(final DataCell[] cells);
}
