package org.knime.scijava.commands.exp.testdriven.table2table.v2;

public class Plugins {

    public interface KNIPNodes {
        // extension point for entire nodes
        // Map, FlatMap, Filter, Aggregate, & Co
    }

    public interface Externalizer {
        // extension point to register serializer/deserializer
    }

    public interface TypeRenderer {
        // Provides all information such that a certain type can be handled by
        // KNIP-Cells
        // ** renderer
    }

    public interface Kernel {
        // Extension point for kernel creator
    }

    public interface FeatureSet {
        // Extension point for feature node
    }

    public interface PixelFeatureSet {
        // Extension point for feature node
    }

    public interface Projector {
        // extension point for projection-method
    }

    public interface StructElement {
        // extension point for strucuting element
    }
}
