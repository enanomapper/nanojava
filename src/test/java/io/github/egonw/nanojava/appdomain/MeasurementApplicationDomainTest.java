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
package io.github.egonw.nanojava.appdomain;

import io.github.egonw.nanojava.data.measurement.EndPoints;
import io.github.egonw.nanojava.data.measurement.IMeasurement;
import io.github.egonw.nanojava.data.measurement.MeasurementRange;
import io.github.egonw.nanojava.data.measurement.MeasurementValue;
import org.junit.Assert;
import org.junit.Test;

public class MeasurementApplicationDomainTest {

    @Test
    public void testDefineFromRange() throws Exception {
        IMeasurement range = new MeasurementRange(EndPoints.ZETA_POTENTIAL, 4.5, 6.7, "http://qudt.org/vocab/unit#ElectronVolt");
        IMeasurement range2 = new MeasurementRange(EndPoints.ZETA_POTENTIAL, 3.5, 7.5, "http://qudt.org/vocab/unit#ElectronVolt");
        IMeasurement range3 = new MeasurementRange(EndPoints.ZETA_POTENTIAL, 4.0, 7.0, "http://qudt.org/vocab/unit#ElectronVolt");
        MeasurementApplicationDomain domain = new MeasurementApplicationDomain();
        domain.define(range, range2);
        Assert.assertTrue(domain.inDomain(range3));
    }

    @Test
    public void testDefineFromNotInRange() throws Exception {
        IMeasurement range = new MeasurementRange(EndPoints.ZETA_POTENTIAL, 4.5, 6.7, "http://qudt.org/vocab/unit#ElectronVolt");
        IMeasurement range2 = new MeasurementRange(EndPoints.ZETA_POTENTIAL, 3.5, 7.5, "http://qudt.org/vocab/unit#ElectronVolt");
        IMeasurement range3 = new MeasurementRange(EndPoints.ZETA_POTENTIAL, 4.0, 7.0, "http://qudt.org/vocab/unit#ElectronVolt");
        MeasurementApplicationDomain domain = new MeasurementApplicationDomain();
        domain.define(range, range3);
        Assert.assertFalse(domain.inDomain(range2));
    }

    @Test
    public void testValueInRange() throws Exception {
        IMeasurement range = new MeasurementRange(EndPoints.ZETA_POTENTIAL, 4.5, 6.7, "http://qudt.org/vocab/unit#ElectronVolt");
        IMeasurement range2 = new MeasurementRange(EndPoints.ZETA_POTENTIAL, 3.5, 7.5, "http://qudt.org/vocab/unit#ElectronVolt");
        IMeasurement value = new MeasurementValue(EndPoints.ZETA_POTENTIAL, 4.0, 1.0, "http://qudt.org/vocab/unit#ElectronVolt");
        MeasurementApplicationDomain domain = new MeasurementApplicationDomain();
        domain.define(range, range2);
        Assert.assertTrue(domain.inDomain(value));
    }

    @Test
    public void testValueNotInRange() throws Exception {
        IMeasurement range = new MeasurementRange(EndPoints.ZETA_POTENTIAL, 4.5, 6.7, "http://qudt.org/vocab/unit#ElectronVolt");
        IMeasurement range2 = new MeasurementRange(EndPoints.ZETA_POTENTIAL, 3.5, 7.5, "http://qudt.org/vocab/unit#ElectronVolt");
        IMeasurement value = new MeasurementValue(EndPoints.ZETA_POTENTIAL, 8.0, 1.0, "http://qudt.org/vocab/unit#ElectronVolt");
        MeasurementApplicationDomain domain = new MeasurementApplicationDomain();
        domain.define(range, range2);
        Assert.assertFalse(domain.inDomain(value));
    }

    @Test
    public void testAddRange() throws Exception {
        IMeasurement range = new MeasurementRange(EndPoints.ZETA_POTENTIAL, 4.5, 6.7, "http://qudt.org/vocab/unit#ElectronVolt");
        IMeasurement range2 = new MeasurementRange(EndPoints.ZETA_POTENTIAL, 3.5, 7.5, "http://qudt.org/vocab/unit#ElectronVolt");
        IMeasurement range3 = new MeasurementRange(EndPoints.ZETA_POTENTIAL, 5.0, 7.0, "http://qudt.org/vocab/unit#ElectronVolt");
        MeasurementApplicationDomain domain = new MeasurementApplicationDomain();
        domain.add(range);
        domain.add(range2);
        Assert.assertTrue(domain.inDomain(range3));
    }

}
