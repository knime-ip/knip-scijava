package org.knime.scijava.commands.exp.testdriven.table2table.v2;

import org.knime.core.data.image.ImageValue;

public class Values {

    // Types
    public interface RealIntervalValue {
        // NB: Defines an RealInterval
    }

    public interface IntervalValue {
        // NB: Defines an interval
    }

    public interface RAIValue extends IterableIntervalValue {
        // NB: keeps an RAI
    }

    public interface RealRAIValue extends IterableIntervalValue {
        // NB: keeps an RAI
    }

    public interface IterableIntervalValue {
        // NB: keeps an iterable
    }

    public interface RegionValue extends RAIValue {
        // NB: keeps some region
    }

    public interface ROIValue extends IterableIntervalValue {
        // NB: keeps some ROI. Combination of Region and RAI
    }

    public interface PointCloudValue extends RealRAIValue {
        // NB: Points can be iterated. Can also be rasterized.
    }

    public interface AffineTransformValue {
        // NB: Represents an affine transformation
    }

    // external integrations
    public interface OpenCVRAIValue extends RAIValue {
        // NB: cell wrapping OpenCV pointers.
    }

    public interface OpenImajValue extends RAIValue {
        // NB: cell wrapping OpenCV pointers.
    }

    public interface ImageRAIValue extends RAIValue, ImageValue {
        // NB: cell wrapping OpenCV pointers.
    }
}
