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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;
import org.xmlcml.cml.element.CMLList;
import org.xmlcml.cml.element.CMLMoleculeList;

import com.github.jqudt.onto.UnitFactory;
import com.github.jqudt.onto.units.EnergyUnit;
import com.github.jqudt.onto.units.LengthUnit;

import io.github.egonw.nanojava.data.Material;
import io.github.egonw.nanojava.data.MaterialBuilder;
import io.github.egonw.nanojava.data.MaterialType;
import io.github.egonw.nanojava.data.Morphology;
import io.github.egonw.nanojava.data.Spacegroup;
import io.github.egonw.nanojava.data.measurement.EndPoints;
import io.github.egonw.nanojava.data.measurement.ErrorlessMeasurementValue;
import io.github.egonw.nanojava.data.measurement.IErrorlessMeasurementValue;
import io.github.egonw.nanojava.data.measurement.MeasurementValue;
import io.github.egonw.nanojava.manipulator.SubstanceManipulator;
import nu.xom.Document;
import nu.xom.Serializer;

public class CDKSerializationTest {

	@Test
	public void roundTripLabels() {
		Material material = new Material("METALOXIDE");
        List<String> labels = new ArrayList<String>();
        labels.add("NM1"); labels.add("CeO2-15");
        material.setLabels(labels);
        CMLMoleculeList cmlMaterial = CDKSerializer.toCML(material);
		Assert.assertNotNull(cmlMaterial);
		Material roundTripped = CDKDeserializer.fromCML(cmlMaterial);
		Assert.assertNotNull(roundTripped);
        Assert.assertNotNull(roundTripped.getLabels());
        Assert.assertEquals(2, roundTripped.getLabels().size());
        Assert.assertTrue(roundTripped.getLabels().contains("NM1"));
        Assert.assertTrue(roundTripped.getLabels().contains("CeO2-15"));
	}

	@Test
	public void roundTripType() {
		Material material = new Material("METALOXIDE");
		CMLMoleculeList cmlMaterial = CDKSerializer.toCML(material);
		Assert.assertNotNull(cmlMaterial);
		Material roundTripped = CDKDeserializer.fromCML(cmlMaterial);
		Assert.assertNotNull(roundTripped);
		Assert.assertEquals(MaterialType.METALOXIDE, roundTripped.getType());

		material = new Material("GRAPHENE");
		cmlMaterial = CDKSerializer.toCML(material);
		Assert.assertNotNull(cmlMaterial);
		roundTripped = CDKDeserializer.fromCML(cmlMaterial);
		Assert.assertNotNull(roundTripped);
		Assert.assertEquals(MaterialType.GRAPHENE, roundTripped.getType());
	}

	@Test
	public void roundChemicalComposition() {
		Material material = new Material("METALOXIDE");
        material.addAtomContainer(
        	MolecularFormulaManipulator.getAtomContainer(
        		"CeO2", SilentChemObjectBuilder.getInstance()
        	)
        );
        CMLMoleculeList cmlMaterial = CDKSerializer.toCML(material);
		Assert.assertNotNull(cmlMaterial);
		Material roundTripped = CDKDeserializer.fromCML(cmlMaterial);
		Assert.assertNotNull(roundTripped);
        IMolecularFormula molForm = roundTripped.getChemicalComposition();
        Assert.assertNotNull(molForm);
        Assert.assertEquals("CeO2", MolecularFormulaManipulator.getString(molForm));
	}

	@Test
	public void roundTripSize() {
		Material material = new Material("METALOXIDE");
		material.setSize(new MeasurementValue(EndPoints.SIZE, 20.0, 7, LengthUnit.NM));
		CMLMoleculeList cmlMaterial = CDKSerializer.toCML(material);
		Assert.assertNotNull(cmlMaterial);
		Material roundTripped = CDKDeserializer.fromCML(cmlMaterial);
		Assert.assertNotNull(roundTripped);
		Assert.assertEquals(20.0, ((IErrorlessMeasurementValue)roundTripped.getSize()).getValue(), 0.1);
	}

	@Test
	public void roundTripSizes() {
		Material material = new Material("METALOXIDE");
		material.addCharacterization(new MeasurementValue(EndPoints.DIAMETER_TEM, 20.0, 7, LengthUnit.NM));
		material.addCharacterization(new MeasurementValue(EndPoints.DIAMETER_DLS, 55.3, 14.3, LengthUnit.NM));
		CMLMoleculeList cmlMaterial = CDKSerializer.toCML(material);
		Assert.assertNotNull(cmlMaterial);
		Material roundTripped = CDKDeserializer.fromCML(cmlMaterial);
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
	public void roundTripSizes_Component() throws Exception {
		Material material = MaterialBuilder.type("METAL")
			.componentFromSMILES(1, "[Au]", "SPHERE", new ErrorlessMeasurementValue(EndPoints.DIAMETER, 3.0, LengthUnit.NM))
			.asMaterial();
		CMLMoleculeList cmlMaterial = CDKSerializer.toCML(material);
		Assert.assertNotNull(cmlMaterial);
		System.out.println(asIndentedString(cmlMaterial));
		Material roundTripped = CDKDeserializer.fromCML(cmlMaterial);
		Assert.assertNotNull(roundTripped);
		IAtomContainer component = roundTripped.getAtomContainer(0);
		Assert.assertNotNull(component);
		IErrorlessMeasurementValue diameter = (IErrorlessMeasurementValue)SubstanceManipulator.getMeasurement(component, EndPoints.DIAMETER);
		Assert.assertEquals(3.0, diameter.getValue(), 0.1);
		System.out.println("Unit: " + diameter.getUnit());
	}

	@Test
	public void roundComponentSpacegroup() throws Exception {
		Material material = MaterialBuilder.type("METAL")
			.componentFromSMILES(1, "O=[Si]=O", "SPHERE", "AMORPHOUS")
			.asMaterial();
		CMLMoleculeList cmlMaterial = CDKSerializer.toCML(material);
		Assert.assertNotNull(cmlMaterial);
		System.out.println(asIndentedString(cmlMaterial));
		Material roundTripped = CDKDeserializer.fromCML(cmlMaterial);
		Assert.assertNotNull(roundTripped);
		IAtomContainer component = roundTripped.getAtomContainer(0);
		Assert.assertNotNull(component);
		Spacegroup group = SubstanceManipulator.getSpacegroup(component);
		Assert.assertNotNull(group);
		Assert.assertEquals(Spacegroup.AMORPHOUS, group);
	}

	@Test
	public void roundTripPurity() {
		Material material = new Material("METALOXIDE");
		material.addCharacterization(new ErrorlessMeasurementValue(
			EndPoints.PURITY, 95,
			UnitFactory.getInstance().getUnit("http://qudt.org/vocab/unit#Percent")
		));
		CMLMoleculeList cmlMaterial = CDKSerializer.toCML(material);
		Assert.assertNotNull(cmlMaterial);
		Material roundTripped = CDKDeserializer.fromCML(cmlMaterial);
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
		CMLMoleculeList cmlMaterial = CDKSerializer.toCML(material);
		Assert.assertNotNull(cmlMaterial);
		Material roundTripped = CDKDeserializer.fromCML(cmlMaterial);
		Assert.assertNotNull(roundTripped);
		Assert.assertEquals(-45.0, ((IErrorlessMeasurementValue)roundTripped.getZetaPotential()).getValue(), 0.1);
	}

	@Test
	public void roundTripList() {
		List<Material> materials = new ArrayList<Material>();
		materials.add(new Material("GRAPHENE"));
		materials.add(new Material("METALOXIDE"));
		CMLList list = CDKSerializer.toCML(materials);
		Assert.assertNotNull(list);
		List<Material> roundTripped = CDKDeserializer.fromCML(list);
		Assert.assertNotNull(roundTripped);
		Assert.assertEquals(2, roundTripped.size());
		Assert.assertEquals(MaterialType.GRAPHENE, roundTripped.get(0).getType());
		Assert.assertEquals(MaterialType.METALOXIDE, roundTripped.get(1).getType());
	}

	@Test
	public void roundtripMorphology() throws Exception {
		Material material = MaterialBuilder.type("METAL")
			.componentFromSMILES(1, "[Au]", "SPHERE")
			.asMaterial();

		CMLMoleculeList cmlMaterial = CDKSerializer.toCML(material);
		Assert.assertNotNull(cmlMaterial);
		Material roundTripped = CDKDeserializer.fromCML(cmlMaterial);
		Assert.assertNotNull(roundTripped);
		System.out.println(asIndentedString(cmlMaterial));
		Assert.assertEquals(MaterialType.METAL, roundTripped.getType());
		Assert.assertEquals(Morphology.SPHERE, SubstanceManipulator.getMorphology(roundTripped.getAtomContainer(0)));
	}

	private String asIndentedString(CMLMoleculeList cmlMaterial) throws Exception {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		Serializer xomSerializer = new Serializer(output, "UTF-8");
		xomSerializer.setIndent(2);
		xomSerializer.write(new Document(cmlMaterial));
		return output.toString();
	}
}
