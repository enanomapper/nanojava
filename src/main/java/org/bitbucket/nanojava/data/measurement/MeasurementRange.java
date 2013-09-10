/* Copyright (C) 2011-2013  Egon Willighagen <egonw@users.sf.net>
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

import com.github.jqudt.Unit;
import com.github.jqudt.onto.UnitFactory;


public class MeasurementRange extends Measurement implements IMeasurementRange {

	private double minimum;
    private double maximum;

    public MeasurementRange(IEndPoint endpoint, double minimum, double maximum, Unit unit) {
    	this.setEndPoint(endpoint);
        this.setValues(minimum, maximum, unit);
    }

    public MeasurementRange(IEndPoint endpoint, double minimum, double maximum, String unit) {
    	this.setEndPoint(endpoint);
        this.setValues(minimum, maximum, unit);
    }

    public void setValues(double minimum, double maximum, String unit) {
    	Unit unitObj = UnitFactory.getInstance().getUnit(unit);
    	if (unitObj != null) {
    		setValues(minimum, maximum, unitObj);
    		return;
        }
        throw new IllegalArgumentException(
            "Unsupported Unit"
        );
    }

    public void setValues(double minimum, double maximum, Unit unit) {
        this.minimum = minimum;
        this.maximum = maximum;
        super.unit = unit;
	}

	public double getMinimumValue() {
		return this.minimum;
	}

	public double getMaximumValue() {
		return this.maximum;
	}

    public String getString() {
        return "" + minimum + " - " + maximum + " " + unit;
    }

}
