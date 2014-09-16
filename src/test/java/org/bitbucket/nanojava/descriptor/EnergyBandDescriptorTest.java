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
package org.bitbucket.nanojava.descriptor;

import junit.framework.Assert;

import org.bitbucket.nanojava.data.MaterialType;
import org.bitbucket.nanojava.data.Material;
import org.bitbucket.nanojava.data.measurement.EndPoints;
import org.bitbucket.nanojava.data.measurement.MeasurementValue;
import org.junit.Before;
import org.junit.Test;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.result.DoubleArrayResult;
import org.openscience.cdk.qsar.result.IDescriptorResult;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import com.github.jqudt.onto.units.LengthUnit;

public class EnergyBandDescriptorTest
extends MaterialDescriptorTest {

    @Before
    public void setUp() throws Exception {
        setDescriptor(EnergyBandDescriptor.class);
    }

    @Test
    public void testCalculate_ZnO() throws Exception {
        Material material = new Material(MaterialType.METALOXIDE);
        material.addAtomContainer(
            MolecularFormulaManipulator.getAtomContainer(
                "ZnO", DefaultChemObjectBuilder.getInstance()
            )
        );
        DescriptorValue value = descriptor.calculate(material);
        Assert.assertNotNull(value);
        IDescriptorResult result = value.getValue();
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof DoubleArrayResult);
        DoubleArrayResult daResult = (DoubleArrayResult)result;
        Assert.assertEquals(2, daResult.length());
        Assert.assertEquals(-3.7, daResult.get(0), 0.0001);
        Assert.assertEquals(-7.25, daResult.get(1), 0.0001);
    }

    @Test
    public void testCalculate_TestSize_TooSmall() throws Exception {
        Material material = new Material(MaterialType.METALOXIDE);
        material.addAtomContainer(
            MolecularFormulaManipulator.getAtomContainer(
                "ZnO", DefaultChemObjectBuilder.getInstance()
            )
        );
        material.setSize(new MeasurementValue(EndPoints.SIZE, 10.0, 5.0, LengthUnit.NM));
        DescriptorValue value = descriptor.calculate(material);
        Assert.assertNotNull(value);
        IDescriptorResult result = value.getValue();
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof DoubleArrayResult);
        DoubleArrayResult daResult = (DoubleArrayResult)result;
        Assert.assertEquals(2, daResult.length());
        Assert.assertEquals(Double.NaN, daResult.get(0), 0.0001);
        Assert.assertEquals(Double.NaN, daResult.get(1), 0.0001);
    }

    @Test
    public void testCalculate_TestSize_OK() throws Exception {
        Material material = new Material(MaterialType.METALOXIDE);
        material.addAtomContainer(
            MolecularFormulaManipulator.getAtomContainer(
                "ZnO", DefaultChemObjectBuilder.getInstance()
            )
        );
        material.setSize(new MeasurementValue(EndPoints.SIZE, 40.0, 5.0, LengthUnit.NM));
        DescriptorValue value = descriptor.calculate(material);
        Assert.assertNotNull(value);
        IDescriptorResult result = value.getValue();
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof DoubleArrayResult);
        DoubleArrayResult daResult = (DoubleArrayResult)result;
        Assert.assertEquals(2, daResult.length());
        Assert.assertEquals(-3.7, daResult.get(0), 0.0001);
        Assert.assertEquals(-7.25, daResult.get(1), 0.0001);
    }

    @Test
    public void testCalculate_IndiumOxide() throws Exception {
        Material material = new Material(MaterialType.METALOXIDE);
        material.addAtomContainer(
            MolecularFormulaManipulator.getAtomContainer(
                "In2O3", DefaultChemObjectBuilder.getInstance()
            )
        );
        material.setSize(new MeasurementValue(EndPoints.SIZE, 40.0, 5.0, LengthUnit.NM));
        DescriptorValue value = descriptor.calculate(material);
        Assert.assertNotNull(value);
        IDescriptorResult result = value.getValue();
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof DoubleArrayResult);
        DoubleArrayResult daResult = (DoubleArrayResult)result;
        Assert.assertEquals(2, daResult.length());
        Assert.assertEquals(-4.0, daResult.get(0), 0.0001);
        Assert.assertEquals(-6.8, daResult.get(1), 0.0001);
    }

    @Test
    public void testCalculate_IronOxides() throws Exception {
        Material oxide1 = new Material(MaterialType.METALOXIDE);
        oxide1.addAtomContainer(
            MolecularFormulaManipulator.getAtomContainer(
                "Fe2O3", DefaultChemObjectBuilder.getInstance()
            )
        );
        Material oxide2 = new Material(MaterialType.METALOXIDE);
        oxide2.addAtomContainer(
            MolecularFormulaManipulator.getAtomContainer(
                "Fe3O4", DefaultChemObjectBuilder.getInstance()
            )
        );
        
        DescriptorValue value1 = descriptor.calculate(oxide1);
        IDescriptorResult result1 = value1.getValue();
        DescriptorValue value2 = descriptor.calculate(oxide2);
        IDescriptorResult result2 = value2.getValue();
        DoubleArrayResult daResult1 = (DoubleArrayResult)result1;
        DoubleArrayResult daResult2 = (DoubleArrayResult)result2;
        Assert.assertNotSame(Double.NaN, daResult1.get(0));
        Assert.assertNotSame(Double.NaN, daResult1.get(1));
        Assert.assertNotSame(daResult1.get(0), daResult2.get(0));
        Assert.assertNotSame(daResult1.get(1), daResult2.get(1));
    }
}
