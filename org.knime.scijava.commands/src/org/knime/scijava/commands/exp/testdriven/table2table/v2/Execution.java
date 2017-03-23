package org.knime.scijava.commands.exp.testdriven.table2table.v2;

public interface Execution {

    /*
     * Execution Mode 1: Prototyping
     *
     * Tiling should be performed automatically if desired (user-option in
     * reader?!). As much lazy as possible (user-selection).
     *
     * Execution Mode 2: Batch Processing Make use of streaming if possible.
     * Prefer Lazy Execution. Element by Element if possible.
     *
     * Problems with Tiling: How to handle ImageJ2 plugins in the case of
     * tiling? Provide additional interfaces on this level?

     * Automatically discover: RAI inputs, RAI outputs and attach preview, viewer etc. Try to create computation Graph if possible!
     * Enable matching from Tiling to RAI!
     *
     */

}
