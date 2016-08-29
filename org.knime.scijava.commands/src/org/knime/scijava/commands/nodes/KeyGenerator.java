package org.knime.scijava.commands.nodes;

import org.knime.core.data.RowKey;

public interface KeyGenerator {

    public RowKey create();

    public void setCurrentKey(final RowKey curr);

}
