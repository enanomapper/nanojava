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
package io.github.egonw.nanojava.descriptor;

import org.junit.Before;
import org.junit.Test;
import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.result.IDescriptorResult;
import org.openscience.cdk.qsar.result.IntegerResult;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import io.github.egonw.nanojava.data.Material;
import io.github.egonw.nanojava.data.MaterialType;
import junit.framework.Assert;

public class MetalPeriodDescriptorTest
extends MaterialDescriptorTest {

    @Before
    public void setUp() throws Exception {
        setDescriptor(MetalPeriodDescriptor.class);
    }

    @Test
    public void testCalculate_ZnO() throws Exception {
        Material material = new Material(MaterialType.METALOXIDE);
        material.addAtomContainer(
            MolecularFormulaManipulator.getAtomContainer(
                "ZnO", SilentChemObjectBuilder.getInstance()
            )
        );
        DescriptorValue value = descriptor.calculate(material);
        Assert.assertNotNull(value);
        IDescriptorResult result = value.getValue();
        Assert.assertNotNull(result);
        Assert.assertEquals(4, ((IntegerResult)result).intValue());
    }

    @Test
    public void testCalculate_IronOxide() throws Exception {
        Material material = new Material(MaterialType.METALOXIDE);
        material.addAtomContainer(
            MolecularFormulaManipulator.getAtomContainer(
                "Fe3O4", SilentChemObjectBuilder.getInstance()
            )
        );
        DescriptorValue value = descriptor.calculate(material);
        Assert.assertNotNull(value);
        IDescriptorResult result = value.getValue();
        Assert.assertNotNull(result);
        Assert.assertEquals(4, ((IntegerResult)result).intValue());
    }

    @Test
    public void testCalculate_ComplexOxid4() throws Exception {
        Material material = new Material(MaterialType.METALOXIDE);
        material.addAtomContainer(
            MolecularFormulaManipulator.getAtomContainer(
                "CuZnFe2O4", SilentChemObjectBuilder.getInstance()
            )
        );
        DescriptorValue value = descriptor.calculate(material);
        Assert.assertNotNull(value);
        IDescriptorResult result = value.getValue();
        Assert.assertNotNull(result);
        Assert.assertEquals((int)Double.NaN, ((IntegerResult)result).intValue());
    }

}
