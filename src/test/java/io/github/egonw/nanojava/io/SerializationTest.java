/* Copyright (C) 2012  Egon Willighagen <egonw@users.sf.net>
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
package io.github.egonw.nanojava.io;

import java.util.ArrayList;
import java.util.List;

import io.github.egonw.nanojava.data.MaterialType;
import io.github.egonw.nanojava.data.Material;
import io.github.egonw.nanojava.data.measurement.EndPoints;
import io.github.egonw.nanojava.data.measurement.ErrorlessMeasurementValue;
import io.github.egonw.nanojava.data.measurement.IErrorlessMeasurementValue;
import io.github.egonw.nanojava.data.measurement.MeasurementValue;
import org.junit.Assert;
import org.junit.Test;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;
import org.xmlcml.cml.element.CMLList;
import org.xmlcml.cml.element.CMLMolecule;

import com.github.jqudt.onto.UnitFactory;
import com.github.jqudt.onto.units.EnergyUnit;
import com.github.jqudt.onto.units.LengthUnit;

public class SerializationTest {

	@Test
	public void roundTripLabels() {
		Material material = new Material("METALOXIDE");
        List<String> labels = new ArrayList<String>();
        labels.add("NM1"); labels.add("CeO2-15");
        material.setLabels(labels);
		CMLMolecule cmlMaterial = Serializer.toCML(material);
		Assert.assertNotNull(cmlMaterial);
		Material roundTripped = Deserializer.fromCML(cmlMaterial);
		Assert.assertNotNull(roundTripped);
        Assert.assertNotNull(roundTripped.getLabels());
        Assert.assertEquals(2, roundTripped.getLabels().size());
        Assert.assertTrue(roundTripped.getLabels().contains("NM1"));
        Assert.assertTrue(roundTripped.getLabels().contains("CeO2-15"));
	}

	@Test
	public void roundTripType() {
		Material material = new Material("METALOXIDE");
		CMLMolecule cmlMaterial = Serializer.toCML(material);
		Assert.assertNotNull(cmlMaterial);
		Material roundTripped = Deserializer.fromCML(cmlMaterial);
		Assert.assertNotNull(roundTripped);
		Assert.assertEquals(MaterialType.METALOXIDE, roundTripped.getType());

		material = new Material("GRAPHENE");
		cmlMaterial = Serializer.toCML(material);
		Assert.assertNotNull(cmlMaterial);
		roundTripped = Deserializer.fromCML(cmlMaterial);
		Assert.assertNotNull(roundTripped);
		Assert.assertEquals(MaterialType.GRAPHENE, roundTripped.getType());
	}

	@Test
	public void roundChemicalComposition() {
		Material material = new Material("METALOXIDE");
        material.addAtomContainer(
        	MolecularFormulaManipulator.getAtomContainer(
        		"CeO2", DefaultChemObjectBuilder.getInstance()
        	)
        );
		CMLMolecule cmlMaterial = Serializer.toCML(material);
		Assert.assertNotNull(cmlMaterial);
		Material roundTripped = Deserializer.fromCML(cmlMaterial);
		Assert.assertNotNull(roundTripped);
        IMolecularFormula molForm = roundTripped.getChemicalComposition();
        Assert.assertNotNull(molForm);
        Assert.assertEquals("CeO2", MolecularFormulaManipulator.getString(molForm));
	}

	@Test
	public void roundTripSize() {
		Material material = new Material("METALOXIDE");
		material.setSize(new MeasurementValue(EndPoints.SIZE, 20.0, 7, LengthUnit.NM));
		CMLMolecule cmlMaterial = Serializer.toCML(material);
		Assert.assertNotNull(cmlMaterial);
		Material roundTripped = Deserializer.fromCML(cmlMaterial);
		Assert.assertNotNull(roundTripped);
		Assert.assertEquals(20.0, ((IErrorlessMeasurementValue)roundTripped.getSize()).getValue(), 0.1);
	}

	@Test
	public void roundTripSizes() {
		Material material = new Material("METALOXIDE");
		material.addCharacterization(new MeasurementValue(EndPoints.DIAMETER_TEM, 20.0, 7, LengthUnit.NM));
		material.addCharacterization(new MeasurementValue(EndPoints.DIAMETER_DLS, 55.3, 14.3, LengthUnit.NM));
		CMLMolecule cmlMaterial = Serializer.toCML(material);
		Assert.assertNotNull(cmlMaterial);
		Material roundTripped = Deserializer.fromCML(cmlMaterial);
		Assert.assertNotNull(roundTripped);
		Assert.assertEquals(20.0,
			((IErrorlessMeasurementValue)roundTripped.getCharacterizations().get(EndPoints.DIAMETER_TEM)).getValue(),
			0.1
		);
		Assert.assertEquals(55.3,
			((IErrorlessMeasurementValue)roundTripped.getCharacterizations().get(EndPoints.DIAMETER_DLS)).getValue(),
			0.1
		);
	}

	@Test
	public void roundTripPurity() {
		Material material = new Material("METALOXIDE");
		material.addCharacterization(new ErrorlessMeasurementValue(
			EndPoints.PURITY, 95,
			UnitFactory.getInstance().getUnit("http://qudt.org/vocab/unit#Percent")
		));
		CMLMolecule cmlMaterial = Serializer.toCML(material);
		Assert.assertNotNull(cmlMaterial);
		Material roundTripped = Deserializer.fromCML(cmlMaterial);
		Assert.assertNotNull(roundTripped);
		Assert.assertEquals(95,
			((IErrorlessMeasurementValue)roundTripped.getCharacterizations().get(EndPoints.PURITY)).getValue(),
			0.1
		);
	}

	@Test
	public void roundTripZetaPotential() {
		Material material = new Material("METALOXIDE");
		material.setZetaPotential(new MeasurementValue(EndPoints.ZETA_POTENTIAL, -45.0, 3, EnergyUnit.EV));
		CMLMolecule cmlMaterial = Serializer.toCML(material);
		Assert.assertNotNull(cmlMaterial);
		Material roundTripped = Deserializer.fromCML(cmlMaterial);
		Assert.assertNotNull(roundTripped);
		Assert.assertEquals(-45.0, ((IErrorlessMeasurementValue)roundTripped.getZetaPotential()).getValue(), 0.1);
	}

	@Test
	public void roundTripList() {
		List<Material> materials = new ArrayList<Material>();
		materials.add(new Material("GRAPHENE"));
		materials.add(new Material("METALOXIDE"));
		CMLList list = Serializer.toCML(materials);
		Assert.assertNotNull(list);
		List<Material> roundTripped = Deserializer.fromCML(list);
		Assert.assertNotNull(roundTripped);
		Assert.assertEquals(2, roundTripped.size());
		Assert.assertEquals(MaterialType.GRAPHENE, roundTripped.get(0).getType());
		Assert.assertEquals(MaterialType.METALOXIDE, roundTripped.get(1).getType());
	}
}
