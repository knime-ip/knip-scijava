package org.knime.scijava.commands.exp.node;

import org.knime.core.data.DataRow;

public interface SJProxy<T> {
    T update(DataRow row);
}
