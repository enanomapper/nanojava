/* Copyright (C) 2011  Egon Willighagen <egonw@users.sf.net>
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.bitbucket.nanojava.data.measurement;

public class ErrorlessMeasurementValue extends Measurement implements IErrorlessMeasurementValue {

	private double value;

    public ErrorlessMeasurementValue(double value, Unit unit) {
        setValue(value, unit);
    }

    public ErrorlessMeasurementValue(double value, String unit) {
        setValue(value, unit);
    }

    public void setValue(double value, String unit) {
        for (Unit unitType : Unit.values()) {
            if (unitType.name().equals(unit)) {
                setValue(value, unitType);
                return;
            }
        }
        throw new IllegalArgumentException(
            "Unsupported Unit"
        );
    }

    public void setValue(double value, Unit unit) {
		this.value = value;
		super.unit = unit; 
	}

	public double getValue() {
		return this.value;
	}

    public String getString() {
        return "" + value + " " + unit;
    }

}
