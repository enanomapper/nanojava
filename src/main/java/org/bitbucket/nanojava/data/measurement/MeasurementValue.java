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

public class MeasurementValue extends Measurement implements IMeasurementValue {

	private double value;
    private double error;

    public MeasurementValue(double value, double error, Unit unit) {
        setValue(value, error, unit);
    }

    public MeasurementValue(double value, double error, String unit) {
        setValue(value, error, unit);
    }

    public void setValue(double value, double error, String unit) {
        Unit unitObj = UnitFactory.getInstance().getUnit(unit);
    	if (unitObj != null) {
    		setValue(value, error, unitObj);
    		return;
        }
        throw new IllegalArgumentException(
            "Unsupported Unit"
        );
    }

    public void setValue(double value, double error, Unit unit) {
		this.value = value;
		this.error = error;
		super.unit = unit; 
	}

	public double getValue() {
		return this.value;
	}

	public double getError() {
		return this.error;
	}

    public String getString() {
        return "" + value + " \u00b1 " + error + " " + unit;
    }

}
