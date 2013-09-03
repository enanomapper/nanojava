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
import org.bitbucket.nanojava.data.Nanomaterial;
import org.junit.Before;
import org.junit.Test;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.result.DoubleResult;
import org.openscience.cdk.qsar.result.IDescriptorResult;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

public class IonizationEthalpyMopac2012DescriptorTest
extends NanomaterialDescriptorTest {

    @Before
    public void setUp() throws Exception {
        setDescriptor(IonizationEthalpyMopac2012Descriptor.class);
    }

    @Test
    public void testCalculate_ZnO() throws Exception {
        Nanomaterial material = new Nanomaterial(MaterialType.METALOXIDE);
        material.setChemicalComposition(
            MolecularFormulaManipulator.getMolecularFormula(
                "ZnO", DefaultChemObjectBuilder.getInstance()
            )
        );
        DescriptorValue value = descriptor.calculate(material);
        Assert.assertNotNull(value);
        IDescriptorResult result = value.getValue();
        Assert.assertNotNull(result);
        Assert.assertEquals(662.43584, ((DoubleResult)result).doubleValue(), 0.0001);
    }

    @Test
    public void testCalculate_Fe2O3() throws Exception {
        Nanomaterial material = new Nanomaterial(MaterialType.METALOXIDE);
        material.setChemicalComposition(
            MolecularFormulaManipulator.getMolecularFormula(
                "Fe2O3", DefaultChemObjectBuilder.getInstance()
            )
        );
        DescriptorValue value = descriptor.calculate(material);
        Assert.assertNotNull(value);
        IDescriptorResult result = value.getValue();
        Assert.assertNotNull(result);
        Assert.assertEquals(1363.39566, ((DoubleResult)result).doubleValue(), 0.0001);
    }

    @Test
    public void testCalculate_Al2O3() throws Exception {
        Nanomaterial material = new Nanomaterial(MaterialType.METALOXIDE);
        material.setChemicalComposition(
            MolecularFormulaManipulator.getMolecularFormula(
                "Al2O3", DefaultChemObjectBuilder.getInstance()
            )
        );
        DescriptorValue value = descriptor.calculate(material);
        Assert.assertNotNull(value);
        IDescriptorResult result = value.getValue();
        Assert.assertNotNull(result);
        Assert.assertEquals(1187.82572, ((DoubleResult)result).doubleValue(), 0.0001);
    }

    @Test
    public void testCalculate_CuZnFe2O4() throws Exception {
        Nanomaterial material = new Nanomaterial(MaterialType.METALOXIDE);
        material.setChemicalComposition(
            MolecularFormulaManipulator.getMolecularFormula(
                "CuZnFe2O4", DefaultChemObjectBuilder.getInstance()
            )
        );
        DescriptorValue value = descriptor.calculate(material);
        Assert.assertNotNull(value);
        IDescriptorResult result = value.getValue();
        Assert.assertNotNull(result);
        Assert.assertEquals(662.43584, ((DoubleResult)result).doubleValue(), 0.0001);
    }

}
