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
package org.bitbucket.nanojava.data;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.bitbucket.nanojava.data.measurement.EndPoints;
import org.bitbucket.nanojava.data.measurement.ErrorlessMeasurementValue;
import org.junit.Test;

import com.github.jqudt.onto.units.LengthUnit;

public class NanomaterialTest {

    @Test
    public void testAlwaysReturnsLabelList() throws Exception {
        Nanomaterial nm = new Nanomaterial("GRAPHENE");
        Assert.assertNotNull(nm.getLabels());
    }

    @Test
    public void testLabels() throws Exception {
        Nanomaterial nm = new Nanomaterial("GRAPHENE");
        List<String> labels = new ArrayList<String>();
        labels.add("NM1"); labels.add("CeO2-15");
        nm.setLabels(labels);
        Assert.assertNotNull(nm.getLabels());
        Assert.assertEquals(2, nm.getLabels().size());
        Assert.assertTrue(nm.getLabels().contains("NM1"));
    }

    @Test
    public void testMultipleSizes() throws Exception {
        Nanomaterial nm = new Nanomaterial("GRAPHENE");
        nm.addCharacterization(new ErrorlessMeasurementValue(EndPoints.DIAMETER_TEM, 20.0, LengthUnit.NM));
        nm.addCharacterization(new ErrorlessMeasurementValue(EndPoints.DIAMETER_DLS, 55.0, LengthUnit.NM));
        Assert.assertNotNull(nm.getCharacterizations());
        Assert.assertEquals(2, nm.getCharacterizations().size());
    }

}
