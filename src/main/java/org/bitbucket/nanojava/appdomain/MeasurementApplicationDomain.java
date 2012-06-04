package org.bitbucket.nanojava.appdomain;

import org.bitbucket.nanojava.data.measurement.IMeasurement;
import org.bitbucket.nanojava.data.measurement.IMeasurementRange;
import org.bitbucket.nanojava.data.measurement.IMeasurementValue;

public class MeasurementApplicationDomain
implements IApplicationDomain<IMeasurement> {

    private double max = Double.NEGATIVE_INFINITY;
    private double min = Double.POSITIVE_INFINITY;

    public void define(IMeasurement... measurements) {
        for (IMeasurement measurement : measurements)
            add(measurement);
    }

    public boolean inDomain(IMeasurement measurement) {
        if (measurement instanceof IMeasurementRange) {
            IMeasurementRange range = (IMeasurementRange)measurement;
            if (range.getMaximumValue() == Double.NaN ||
                range.getMinimumValue() == Double.NaN) return false;
            return (range.getMaximumValue() <= max &&
                    range.getMinimumValue() >= min);
        } else if (measurement instanceof IMeasurementValue) {
            IMeasurementValue value = (IMeasurementValue)measurement;
            if (value.getValue() == Double.NaN) return false;
            return (value.getValue() <= max &&
                    value.getValue() >= min);
        }
        return false;
    }

    public void add(IMeasurement measurement) {
        if (measurement instanceof IMeasurementRange) {
            IMeasurementRange range = (IMeasurementRange)measurement;
            if (range.getMaximumValue() != Double.NaN)
                max = Math.max(range.getMaximumValue(), max);
            if (range.getMinimumValue() != Double.NaN)
                min = Math.min(range.getMinimumValue(), min);
        } else if (measurement instanceof IMeasurementValue) {
            IMeasurementValue value = (IMeasurementValue)measurement;
            if (value.getValue() != Double.NaN) {
                max = Math.max(value.getValue(), max);
                min = Math.min(value.getValue(), min);
            }
        }
    }

}
