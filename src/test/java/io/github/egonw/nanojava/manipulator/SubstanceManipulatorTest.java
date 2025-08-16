/* Copyright (C) 2011-2014  Egon Willighagen <egonw@users.sf.net>
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
package io.github.egonw.nanojava.manipulator;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.jqudt.onto.units.LengthUnit;

import io.github.egonw.nanojava.data.Material;
import io.github.egonw.nanojava.data.measurement.EndPoints;
import io.github.egonw.nanojava.data.measurement.ErrorlessMeasurementValue;

public class SubstanceManipulatorTest {

    @Test
    public void testAlwaysReturnsLabelList() throws Exception {
        Material nm = new Material("GRAPHENE");
        assertNotNull(SubstanceManipulator.getLabels(nm));
    }

    @Test
    public void testChemicalCompisitionWhenEmpty() throws Exception {
        Material nm = new Material("GRAPHENE");
        assertNull(SubstanceManipulator.getChemicalComposition(nm));
    }

    @Test
    public void testLabels() throws Exception {
        Material nm = new Material("GRAPHENE");
        List<String> labels = new ArrayList<String>();
        labels.add("NM1"); labels.add("CeO2-15");
        SubstanceManipulator.setLabels(nm, labels);
        assertNotNull(SubstanceManipulator.getLabels(nm));
        assertEquals(2, SubstanceManipulator.getLabels(nm).size());
        assertTrue(SubstanceManipulator.getLabels(nm).contains("NM1"));
    }

    @Test
    public void testMultipleSizes() throws Exception {
        Material nm = new Material("GRAPHENE");
        nm.addCharacterization(new ErrorlessMeasurementValue(EndPoints.DIAMETER_TEM, 20.0, LengthUnit.NM));
        nm.addCharacterization(new ErrorlessMeasurementValue(EndPoints.DIAMETER_DLS, 55.0, LengthUnit.NM));
        assertNotNull(SubstanceManipulator.getCharacterizations(nm));
        assertEquals(2, SubstanceManipulator.getCharacterizations(nm).size());
    }

    @Test
    public void testAsIndentedString_Material() throws Exception {
        Material nm = new Material("GRAPHENE");
        nm.addCharacterization(new ErrorlessMeasurementValue(EndPoints.DIAMETER_TEM, 20.0, LengthUnit.NM));
        nm.addCharacterization(new ErrorlessMeasurementValue(EndPoints.DIAMETER_DLS, 55.0, LengthUnit.NM));
        String indented = SubstanceManipulator.asIndentedString(nm);
        System.out.println(indented);
        assertTrue(indented.contains("<?xml"));
        assertTrue(indented.contains("NPO_1915"));
    }

}
