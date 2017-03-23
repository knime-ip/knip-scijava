package org.knime.scijava.commands.exp.testdriven.table2table.v2;

//TODO I've to work on this using use-cases!
public class Cells {

    /*
     * Reading in images: can result in a reference cell or, if desired, in a
     * persistent cell (internal format).
     *
     * Performing computations on an image: can result in lazy cell or
     * persistent cell, depending on user-selection. Lazy-Cell: old+cell + f(x)
     * is saved. Persistent: f(oldCell) is saved.
     *
     * Problems:
     * ** We have to implement every combination (Lazy+Ref, Persistent+Ref, ...)
     * ** We potentially cannot work lazily with SmallRAICells.
     *
     */

    // Reference Cells. Ideally, only SCIFIO would exist.
    public interface ReferenceCell {
        // Cell which has a lazy handle on some external file
    }

    public interface BDVReferenceCell extends InternalCell {
        // backed by FileStore, with random access etc.
    }

    public interface IlastikReferenceCell extends InternalCell {
        // backed by FileStore, with random access etc.
    }

    public interface SCIFIOReferenceCell extends ReferenceCell {
        // cell which lazily loads data from some data-source.
    }

    // Cells loading data from internal formats
    public interface InternalCell {
        // Cell which has a handle on local cell
    }

    public interface CompGraphRAICell extends InternalCell {
        // backed by FileStore, with random access.
        // provides access to image data only on demand.
        // can be decorated with conver
    }

    public interface PersistentRAICell extends InternalCell {
        // NB: ArrayImg,
    }

    public interface SmallRAICell extends InternalCell {
        // keeps small images < threshold as standard DataCell. More efficient
        // image I/O.
    }
}
