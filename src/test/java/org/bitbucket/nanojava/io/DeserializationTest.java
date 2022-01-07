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
package org.bitbucket.nanojava.io;

import java.io.IOException;
import java.util.List;

import org.bitbucket.nanojava.data.Material;
import org.bitbucket.nanojava.data.MaterialType;
import org.bitbucket.nanojava.data.Morphology;
import org.bitbucket.nanojava.data.measurement.IErrorlessMeasurementValue;
import org.bitbucket.nanojava.manipulator.SubstanceManipulator;
import org.junit.Assert;
import org.junit.Test;

import nu.xom.ParsingException;

public class DeserializationTest {

	@Test
	public void readType() throws ParsingException, IOException {
		String input = "<molecule xmlns=\"http://www.xml-cml.org/schema\" xmlns:nano=\"http://.../\" convention=\"nano:material\"><scalar dictRef=\"nano:type\" dataType=\"xsd:string\">GRAPHENE</scalar></molecule>";
		Material material = Deserializer.fromCMLString(input);
		Assert.assertNotNull(material);
		Assert.assertEquals(MaterialType.GRAPHENE, material.getType());
	}
	
	@Test
	public void readSize() throws ParsingException, IOException {
		String input = "<molecule xmlns=\"http://www.xml-cml.org/schema\" xmlns:nano=\"http://.../\" convention=\"nano:material\"><scalar dictRef=\"nano:type\" dataType=\"xsd:string\">METALOXIDE</scalar><property dictRef=\"nano:dimension\"><scalar units=\"qudt:nm\" dataType=\"xsd:double\">20.0</scalar></property></molecule>";
		Material material = Deserializer.fromCMLString(input);
		Assert.assertNotNull(material);
		Assert.assertEquals(20.0, ((IErrorlessMeasurementValue)material.getSize()).getValue(), 0.1);
	}
	
	@Test
	public void readZetaPotential() throws ParsingException, IOException {
		String input = "<molecule xmlns=\"http://www.xml-cml.org/schema\" xmlns:nano=\"http://.../\" convention=\"nano:material\"><scalar dictRef=\"nano:type\" dataType=\"xsd:string\">METALOXIDE</scalar><property dictRef=\"nano:zetaPotential\"><scalar units=\"qudt:eV\" dataType=\"xsd:double\">-45.0</scalar></property></molecule>";
		Material material = Deserializer.fromCMLString(input);
		Assert.assertNotNull(material);
		Assert.assertEquals(-45.0, ((IErrorlessMeasurementValue)material.getZetaPotential()).getValue(), 0.1);
	}
	
	@Test
	public void readList() throws ParsingException, IOException {
		String input = "<list xmlns=\"http://www.xml-cml.org/schema\"><molecule xmlns:nano=\"http://example.org/nano#\" xmlns:qudt=\"http://example.org/qudt#\" convention=\"nano:material\"><formula inline=\"Al2O3\" /><scalar dictRef=\"nano:type\" dataType=\"xsd:string\">METALOXIDE</scalar><property dictRef=\"nano:zetaPotential\"><scalar units=\"qudt:eV\" dataType=\"xsd:double\">-24.0</scalar></property><property xmlns:npo=\"http://purl.bioontology.org/ontology/npo#\" dictRef=\"npo:NPO_1915\"><scalar xmlns:ops=\"http://www.openphacts.org/units/\" units=\"ops:Nanometer\" dataType=\"xsd:double\">524.8</scalar></property><property xmlns:pato=\"http://purl.org/obo/owl/PATO#\" dictRef=\"pato:PATO_0001334\"><scalar xmlns:ops=\"http://www.openphacts.org/units/\" units=\"ops:Nanometer\" dataType=\"xsd:double\">14.7</scalar></property><property xmlns:npo=\"http://purl.bioontology.org/ontology/npo#\" dictRef=\"npo:NPO_1302\"><scalar xmlns:qudt=\"http://qudt.org/vocab/unit#\" units=\"qudt:ElectronVolt\" dataType=\"xsd:double\">-24.0</scalar></property></molecule><molecule xmlns:nano=\"http://example.org/nano#\" xmlns:qudt=\"http://example.org/qudt#\" convention=\"nano:material\"><formula inline=\"CeO2\" /><scalar dictRef=\"nano:type\" dataType=\"xsd:string\">METALOXIDE</scalar><property dictRef=\"nano:zetaPotential\"><scalar units=\"qudt:eV\" dataType=\"xsd:double\">-28.9</scalar></property><property xmlns:npo=\"http://purl.bioontology.org/ontology/npo#\" dictRef=\"npo:NPO_1915\"><scalar xmlns:ops=\"http://www.openphacts.org/units/\" units=\"ops:Nanometer\" dataType=\"xsd:double\">321.3</scalar></property><property xmlns:pato=\"http://purl.org/obo/owl/PATO#\" dictRef=\"pato:PATO_0001334\"><scalar xmlns:ops=\"http://www.openphacts.org/units/\" units=\"ops:Nanometer\" dataType=\"xsd:double\">12.8</scalar></property><property xmlns:npo=\"http://purl.bioontology.org/ontology/npo#\" dictRef=\"npo:NPO_1302\"><scalar xmlns:qudt=\"http://qudt.org/vocab/unit#\" units=\"qudt:ElectronVolt\" dataType=\"xsd:double\">-28.9</scalar></property></molecule><molecule xmlns:nano=\"http://example.org/nano#\" xmlns:qudt=\"http://example.org/qudt#\" convention=\"nano:material\"><formula inline=\"CoO\" /><scalar dictRef=\"nano:type\" dataType=\"xsd:string\">METALOXIDE</scalar><property dictRef=\"nano:zetaPotential\"><scalar units=\"qudt:eV\" dataType=\"xsd:double\">-25.5</scalar></property><property xmlns:npo=\"http://purl.bioontology.org/ontology/npo#\" dictRef=\"npo:NPO_1915\"><scalar xmlns:ops=\"http://www.openphacts.org/units/\" units=\"ops:Nanometer\" dataType=\"xsd:double\">378.3</scalar></property><property xmlns:pato=\"http://purl.org/obo/owl/PATO#\" dictRef=\"pato:PATO_0001334\"><scalar xmlns:ops=\"http://www.openphacts.org/units/\" units=\"ops:Nanometer\" dataType=\"xsd:double\">18.3</scalar></property><property xmlns:npo=\"http://purl.bioontology.org/ontology/npo#\" dictRef=\"npo:NPO_1302\"><scalar xmlns:qudt=\"http://qudt.org/vocab/unit#\" units=\"qudt:ElectronVolt\" dataType=\"xsd:double\">-25.5</scalar></property></molecule><molecule xmlns:nano=\"http://example.org/nano#\" xmlns:qudt=\"http://example.org/qudt#\" convention=\"nano:material\"><formula inline=\"Co3O4\" /><scalar dictRef=\"nano:type\" dataType=\"xsd:string\">METALOXIDE</scalar><property dictRef=\"nano:zetaPotential\"><scalar units=\"qudt:eV\" dataType=\"xsd:double\">-29.0</scalar></property><property xmlns:npo=\"http://purl.bioontology.org/ontology/npo#\" dictRef=\"npo:NPO_1915\"><scalar xmlns:ops=\"http://www.openphacts.org/units/\" units=\"ops:Nanometer\" dataType=\"xsd:double\">247.6</scalar></property><property xmlns:pato=\"http://purl.org/obo/owl/PATO#\" dictRef=\"pato:PATO_0001334\"><scalar xmlns:ops=\"http://www.openphacts.org/units/\" units=\"ops:Nanometer\" dataType=\"xsd:double\">10.0</scalar></property><property xmlns:npo=\"http://purl.bioontology.org/ontology/npo#\" dictRef=\"npo:NPO_1302\"><scalar xmlns:qudt=\"http://qudt.org/vocab/unit#\" units=\"qudt:ElectronVolt\" dataType=\"xsd:double\">-29.0</scalar></property></molecule><molecule xmlns:nano=\"http://example.org/nano#\" xmlns:qudt=\"http://example.org/qudt#\" convention=\"nano:material\"><formula inline=\"Cr2O3\" /><scalar dictRef=\"nano:type\" dataType=\"xsd:string\">METALOXIDE</scalar><property dictRef=\"nano:zetaPotential\"><scalar units=\"qudt:eV\" dataType=\"xsd:double\">-26.2</scalar></property><property xmlns:npo=\"http://purl.bioontology.org/ontology/npo#\" dictRef=\"npo:NPO_1915\"><scalar xmlns:ops=\"http://www.openphacts.org/units/\" units=\"ops:Nanometer\" dataType=\"xsd:double\">478.5</scalar></property><property xmlns:pato=\"http://purl.org/obo/owl/PATO#\" dictRef=\"pato:PATO_0001334\"><scalar xmlns:ops=\"http://www.openphacts.org/units/\" units=\"ops:Nanometer\" dataType=\"xsd:double\">71.8</scalar></property><property xmlns:npo=\"http://purl.bioontology.org/ontology/npo#\" dictRef=\"npo:NPO_1302\"><scalar xmlns:qudt=\"http://qudt.org/vocab/unit#\" units=\"qudt:ElectronVolt\" dataType=\"xsd:double\">-26.2</scalar></property></molecule><molecule xmlns:nano=\"http://example.org/nano#\" xmlns:qudt=\"http://example.org/qudt#\" convention=\"nano:material\"><formula inline=\"CuO\" /><scalar dictRef=\"nano:type\" dataType=\"xsd:string\">METALOXIDE</scalar><property dictRef=\"nano:zetaPotential\"><scalar units=\"qudt:eV\" dataType=\"xsd:double\">-26.9</scalar></property><property xmlns:npo=\"http://purl.bioontology.org/ontology/npo#\" dictRef=\"npo:NPO_1915\"><scalar xmlns:ops=\"http://www.openphacts.org/units/\" units=\"ops:Nanometer\" dataType=\"xsd:double\">289.5</scalar></property><property xmlns:pato=\"http://purl.org/obo/owl/PATO#\" dictRef=\"pato:PATO_0001334\"><scalar xmlns:ops=\"http://www.openphacts.org/units/\" units=\"ops:Nanometer\" dataType=\"xsd:double\">193.0</scalar></property><property xmlns:npo=\"http://purl.bioontology.org/ontology/npo#\" dictRef=\"npo:NPO_1302\"><scalar xmlns:qudt=\"http://qudt.org/vocab/unit#\" units=\"qudt:ElectronVolt\" dataType=\"xsd:double\">-26.9</scalar></property></molecule><molecule xmlns:nano=\"http://example.org/nano#\" xmlns:qudt=\"http://example.org/qudt#\" convention=\"nano:material\"><formula inline=\"Fe2O3\" /><scalar dictRef=\"nano:type\" dataType=\"xsd:string\">METALOXIDE</scalar><property dictRef=\"nano:zetaPotential\"><scalar units=\"qudt:eV\" dataType=\"xsd:double\">-24.1</scalar></property><property xmlns:npo=\"http://purl.bioontology.org/ontology/npo#\" dictRef=\"npo:NPO_1915\"><scalar xmlns:ops=\"http://www.openphacts.org/units/\" units=\"ops:Nanometer\" dataType=\"xsd:double\">385.2</scalar></property><property xmlns:pato=\"http://purl.org/obo/owl/PATO#\" dictRef=\"pato:PATO_0001334\"><scalar xmlns:ops=\"http://www.openphacts.org/units/\" units=\"ops:Nanometer\" dataType=\"xsd:double\">12.3</scalar></property><property xmlns:npo=\"http://purl.bioontology.org/ontology/npo#\" dictRef=\"npo:NPO_1302\"><scalar xmlns:qudt=\"http://qudt.org/vocab/unit#\" units=\"qudt:ElectronVolt\" dataType=\"xsd:double\">-24.1</scalar></property></molecule><molecule xmlns:nano=\"http://example.org/nano#\" xmlns:qudt=\"http://example.org/qudt#\" convention=\"nano:material\"><formula inline=\"Fe3O4\" /><scalar dictRef=\"nano:type\" dataType=\"xsd:string\">METALOXIDE</scalar><property dictRef=\"nano:zetaPotential\"><scalar units=\"qudt:eV\" dataType=\"xsd:double\">-27.0</scalar></property><property xmlns:npo=\"http://purl.bioontology.org/ontology/npo#\" dictRef=\"npo:NPO_1915\"><scalar xmlns:ops=\"http://www.openphacts.org/units/\" units=\"ops:Nanometer\" dataType=\"xsd:double\">831.7</scalar></property><property xmlns:pato=\"http://purl.org/obo/owl/PATO#\" dictRef=\"pato:PATO_0001334\"><scalar xmlns:ops=\"http://www.openphacts.org/units/\" units=\"ops:Nanometer\" dataType=\"xsd:double\">12.0</scalar></property><property xmlns:npo=\"http://purl.bioontology.org/ontology/npo#\" dictRef=\"npo:NPO_1302\"><scalar xmlns:qudt=\"http://qudt.org/vocab/unit#\" units=\"qudt:ElectronVolt\" dataType=\"xsd:double\">-27.0</scalar></property></molecule><molecule xmlns:nano=\"http://example.org/nano#\" xmlns:qudt=\"http://example.org/qudt#\" convention=\"nano:material\"><formula inline=\"Gd2O3\" /><scalar dictRef=\"nano:type\" dataType=\"xsd:string\">METALOXIDE</scalar><property dictRef=\"nano:zetaPotential\"><scalar units=\"qudt:eV\" dataType=\"xsd:double\">-34.7</scalar></property><property xmlns:npo=\"http://purl.bioontology.org/ontology/npo#\" dictRef=\"npo:NPO_1915\"><scalar xmlns:ops=\"http://www.openphacts.org/units/\" units=\"ops:Nanometer\" dataType=\"xsd:double\">726.7</scalar></property><property xmlns:pato=\"http://purl.org/obo/owl/PATO#\" dictRef=\"pato:PATO_0001334\"><scalar xmlns:ops=\"http://www.openphacts.org/units/\" units=\"ops:Nanometer\" dataType=\"xsd:double\">43.8</scalar></property><property xmlns:npo=\"http://purl.bioontology.org/ontology/npo#\" dictRef=\"npo:NPO_1302\"><scalar xmlns:qudt=\"http://qudt.org/vocab/unit#\" units=\"qudt:ElectronVolt\" dataType=\"xsd:double\">-34.7</scalar></property></molecule><molecule xmlns:nano=\"http://example.org/nano#\" xmlns:qudt=\"http://example.org/qudt#\" convention=\"nano:material\"><formula inline=\"HfO2\" /><scalar dictRef=\"nano:type\" dataType=\"xsd:string\">METALOXIDE</scalar><property dictRef=\"nano:zetaPotential\"><scalar units=\"qudt:eV\" dataType=\"xsd:double\">-24.3</scalar></property><property xmlns:npo=\"http://purl.bioontology.org/ontology/npo#\" dictRef=\"npo:NPO_1915\"><scalar xmlns:ops=\"http://www.openphacts.org/units/\" units=\"ops:Nanometer\" dataType=\"xsd:double\">349.9</scalar></property><property xmlns:pato=\"http://purl.org/obo/owl/PATO#\" dictRef=\"pato:PATO_0001334\"><scalar xmlns:ops=\"http://www.openphacts.org/units/\" units=\"ops:Nanometer\" dataType=\"xsd:double\">28.4</scalar></property><property xmlns:npo=\"http://purl.bioontology.org/ontology/npo#\" dictRef=\"npo:NPO_1302\"><scalar xmlns:qudt=\"http://qudt.org/vocab/unit#\" units=\"qudt:ElectronVolt\" dataType=\"xsd:double\">-24.3</scalar></property></molecule><molecule xmlns:nano=\"http://example.org/nano#\" xmlns:qudt=\"http://example.org/qudt#\" convention=\"nano:material\"><formula inline=\"In2O3\" /><scalar dictRef=\"nano:type\" dataType=\"xsd:string\">METALOXIDE</scalar><property dictRef=\"nano:zetaPotential\"><scalar units=\"qudt:eV\" dataType=\"xsd:double\">-35.5</scalar></property><property xmlns:npo=\"http://purl.bioontology.org/ontology/npo#\" dictRef=\"npo:NPO_1915\"><scalar xmlns:ops=\"http://www.openphacts.org/units/\" units=\"ops:Nanometer\" dataType=\"xsd:double\">303.2</scalar></property><property xmlns:pato=\"http://purl.org/obo/owl/PATO#\" dictRef=\"pato:PATO_0001334\"><scalar xmlns:ops=\"http://www.openphacts.org/units/\" units=\"ops:Nanometer\" dataType=\"xsd:double\">59.6</scalar></property><property xmlns:npo=\"http://purl.bioontology.org/ontology/npo#\" dictRef=\"npo:NPO_1302\"><scalar xmlns:qudt=\"http://qudt.org/vocab/unit#\" units=\"qudt:ElectronVolt\" dataType=\"xsd:double\">-35.5</scalar></property></molecule></list>";
		List<Material> materials = Deserializer.fromCMLListString(input);
		Assert.assertNotNull(materials);
		Assert.assertEquals(11, materials.size());
	}

	@Test
	public void readMaterialFromMoleculeList() throws ParsingException, IOException {
		String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			+ "<moleculeList id=\"molSet1\" convention=\"nano:material\" xmlns=\"http://www.xml-cml.org/schema\" xmlns:nano=\"http://linkedchemistry.org/nano#\">\n"
			+ "  <molecule id=\"m1\">\n"
			+ "    <atomArray>\n"
			+ "      <atom id=\"a1\" elementType=\"Au\" formalCharge=\"0\" hydrogenCount=\"0\"/>\n"
			+ "    </atomArray>\n"
			+ "    <scalar dictRef=\"nano:order\" dataType=\"xsd:string\">1</scalar>\n"
			+ "    <scalar dictRef=\"nano:morphology\" dataType=\"xsd:string\">SPHERE</scalar>\n"
			+ "  </molecule>\n"
			+ "  <scalar dictRef=\"nano:type\" dataType=\"xsd:string\">METAL</scalar>\n"
			+ "</moleculeList>";
		Material material = CDKDeserializer.fromCMLString(input);
		Assert.assertNotNull(material);
		Assert.assertEquals(MaterialType.METAL, material.getType());
		Assert.assertEquals(Morphology.SPHERE, SubstanceManipulator.getMorphology(material.getAtomContainer(0)));
	}

}
